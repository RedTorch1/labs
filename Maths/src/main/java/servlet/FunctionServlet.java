package servlet;

import dao.impl.FunctionDaoImpl;
import dao.impl.AdvancedFunctionDaoImpl;
import model.FunctionEntity;
import servlet.util.JsonResponseHelper;
import servlet.util.RequestParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

@WebServlet("/api/functions/*")
public class FunctionServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(FunctionServlet.class);
    private FunctionDaoImpl functionDao;
    private AdvancedFunctionDaoImpl advancedFunctionDao;
    private Connection conn;

    @Override
    public void init() throws ServletException {
        try {
            log.info("🚀 Initializing FunctionServlet...");
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/lab5",
                    "labuser",
                    "labpass");
            functionDao = new FunctionDaoImpl(conn);
            advancedFunctionDao = new AdvancedFunctionDaoImpl(conn);
            log.info("✅ FunctionServlet initialized successfully");
        } catch (Exception e) {
            log.error("❌ Failed to initialize FunctionServlet", e);
            throw new ServletException("Database connection failed", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("🔍 GET request for functions: {}", request.getPathInfo());

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /api/functions - получить все функции
                getAllFunctions(request, response);
            } else if (pathInfo.matches("/\\d+")) {
                // GET /api/functions/{id} - получить функцию по ID
                Long functionId = Long.parseLong(pathInfo.substring(1));
                getFunctionById(functionId, response);
            } else if (pathInfo.equals("/user")) {
                // GET /api/functions/user?userId=... - функции пользователя
                getFunctionsByUser(request, response);
            } else if (pathInfo.equals("/search")) {
                // GET /api/functions/search?name=...&expression=...
                searchFunctions(request, response);
            } else if (pathInfo.equals("/hierarchy")) {
                // GET /api/functions/hierarchy?userId=... - иерархия функций с точками
                getFunctionsHierarchy(request, response);
            } else if (pathInfo.equals("/sorted")) {
                // GET /api/functions/sorted?userId=...&sortBy=...&ascending=...
                getSortedFunctions(request, response);
            } else {
                JsonResponseHelper.sendError(response, 404, "Endpoint not found");
            }
        } catch (Exception e) {
            log.error("❌ Error processing GET request", e);
            JsonResponseHelper.sendError(response, 500, "Internal server error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("📝 POST request for functions");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                // POST /api/functions - создать новую функцию
                createFunction(request, response);
            } else {
                JsonResponseHelper.sendError(response, 404, "Endpoint not found");
            }
        } catch (Exception e) {
            log.error("❌ Error processing POST request", e);
            JsonResponseHelper.sendError(response, 500, "Internal server error: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("✏️ PUT request for functions");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo != null && pathInfo.matches("/\\d+")) {
                // PUT /api/functions/{id} - обновить функцию
                Long functionId = Long.parseLong(pathInfo.substring(1));
                updateFunction(functionId, request, response);
            } else {
                JsonResponseHelper.sendError(response, 404, "Endpoint not found");
            }
        } catch (Exception e) {
            log.error("❌ Error processing PUT request", e);
            JsonResponseHelper.sendError(response, 500, "Internal server error: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("🗑️ DELETE request for functions");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo != null && pathInfo.matches("/\\d+")) {
                // DELETE /api/functions/{id} - удалить функцию
                Long functionId = Long.parseLong(pathInfo.substring(1));
                deleteFunction(functionId, response);
            } else {
                JsonResponseHelper.sendError(response, 404, "Endpoint not found");
            }
        } catch (Exception e) {
            log.error("❌ Error processing DELETE request", e);
            JsonResponseHelper.sendError(response, 500, "Internal server error: " + e.getMessage());
        }
    }

    private void getAllFunctions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("📋 Getting all functions");
        List<FunctionEntity> functions = functionDao.findAll();
        log.info("✅ Found {} functions", functions.size());
        JsonResponseHelper.sendSuccess(response, functions);
    }

    private void getFunctionById(Long functionId, HttpServletResponse response) throws IOException {
        log.info("🔍 Getting function by ID: {}", functionId);
        FunctionEntity function = functionDao.findById(functionId);

        if (function != null) {
            log.info("✅ Found function: {}", function.getName());
            JsonResponseHelper.sendSuccess(response, function);
        } else {
            log.warn("⚠️ Function not found with ID: {}", functionId);
            JsonResponseHelper.sendError(response, 404, "Function not found");
        }
    }

    private void getFunctionsByUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long userId = RequestParser.parseLongParameter(request, "userId");
        log.info("👤 Getting functions for user ID: {}", userId);

        if (userId == null) {
            JsonResponseHelper.sendError(response, 400, "userId parameter is required");
            return;
        }

        List<FunctionEntity> functions = functionDao.findByUser(userId);
        log.info("✅ Found {} functions for user {}", functions.size(), userId);
        JsonResponseHelper.sendSuccess(response, functions);
    }

    private void searchFunctions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = RequestParser.parseStringParameter(request, "name");
        String expression = RequestParser.parseStringParameter(request, "expression");

        log.info("🔍 Searching functions - name: {}, expression: {}", name, expression);

        List<FunctionEntity> results;
        if (name != null && expression != null) {
            results = advancedFunctionDao.findByNameContaining(name);
            results.addAll(advancedFunctionDao.findByExpressionContaining(expression));
        } else if (name != null) {
            results = advancedFunctionDao.findByNameContaining(name);
        } else if (expression != null) {
            results = advancedFunctionDao.findByExpressionContaining(expression);
        } else {
            JsonResponseHelper.sendError(response, 400, "At least one search parameter (name or expression) is required");
            return;
        }

        log.info("✅ Found {} functions matching search criteria", results.size());
        JsonResponseHelper.sendSuccess(response, results);
    }

    private void getFunctionsHierarchy(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long userId = RequestParser.parseLongParameter(request, "userId");
        log.info("🏗️ Getting functions hierarchy for user ID: {}", userId);

        if (userId == null) {
            JsonResponseHelper.sendError(response, 400, "userId parameter is required");
            return;
        }

        Map<FunctionEntity, List<Object>> hierarchy = advancedFunctionDao.getUserFunctionsHierarchy(userId);
        log.info("✅ Built hierarchy with {} functions for user {}", hierarchy.size(), userId);
        JsonResponseHelper.sendSuccess(response, hierarchy);
    }

    private void getSortedFunctions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long userId = RequestParser.parseLongParameter(request, "userId");
        String sortBy = RequestParser.parseStringParameter(request, "sortBy");
        Boolean ascending = Boolean.valueOf(RequestParser.parseStringParameter(request, "ascending"));

        if (userId == null) {
            JsonResponseHelper.sendError(response, 400, "userId parameter is required");
            return;
        }

        if (sortBy == null) {
            sortBy = "name";
        }
        if (ascending == null) {
            ascending = true;
        }

        log.info("📈 Getting sorted functions for user {} by {} ({})", userId, sortBy, ascending ? "ASC" : "DESC");

        List<FunctionEntity> functions = advancedFunctionDao.findByUserSorted(userId, sortBy, ascending);
        log.info("✅ Found {} sorted functions", functions.size());
        JsonResponseHelper.sendSuccess(response, functions);
    }

    private void createFunction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("📊 Creating new function");

        FunctionEntity newFunction = RequestParser.parseJsonRequest(request, FunctionEntity.class);

        if (newFunction.getName() == null || newFunction.getExpression() == null || newFunction.getUserId() == 0) {
            JsonResponseHelper.sendError(response, 400, "Name, expression and userId are required");
            return;
        }

        functionDao.insert(newFunction);
        log.info("✅ Created new function: {} for user {}", newFunction.getName(), newFunction.getUserId());

        JsonResponseHelper.sendSuccess(response, newFunction);
    }

    private void updateFunction(Long functionId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("✏️ Updating function with ID: {}", functionId);

        FunctionEntity existingFunction = functionDao.findById(functionId);
        if (existingFunction == null) {
            JsonResponseHelper.sendError(response, 404, "Function not found");
            return;
        }

        FunctionEntity updatedFunction = RequestParser.parseJsonRequest(request, FunctionEntity.class);
        updatedFunction.setId(functionId);

        functionDao.update(updatedFunction);
        log.info("✅ Updated function with ID: {}", functionId);

        JsonResponseHelper.sendSuccess(response, updatedFunction);
    }

    private void deleteFunction(Long functionId, HttpServletResponse response) throws IOException {
        log.info("🗑️ Deleting function with ID: {}", functionId);

        FunctionEntity existingFunction = functionDao.findById(functionId);
        if (existingFunction == null) {
            JsonResponseHelper.sendError(response, 404, "Function not found");
            return;
        }

        functionDao.delete(functionId);
        log.info("✅ Deleted function with ID: {}", functionId);

        JsonResponseHelper.sendSuccess(response, Map.of("message", "Function deleted successfully"));
    }

    @Override
    public void destroy() {
        log.info("🛑 Destroying FunctionServlet...");
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                log.info("✅ Database connection closed");
            }
        } catch (Exception e) {
            log.error("❌ Error closing database connection", e);
        }
    }
}