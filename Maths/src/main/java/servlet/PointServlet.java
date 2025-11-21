package servlet;

import dao.impl.PointDaoImpl;
import dao.impl.AdvancedPointDaoImpl;
import dao.impl.FunctionDaoImpl;
import model.FunctionEntity;
import model.PointEntity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/points/*")
public class PointServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(PointServlet.class);
    private PointDaoImpl pointDao;
    private AdvancedPointDaoImpl advancedPointDao;
    private FunctionDaoImpl functionDao;
    private Connection conn;

    @Override
    public void init() throws ServletException {
        try {
            log.info("🚀 Initializing PointServlet...");
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/lab5",
                    "labuser",
                    "labpass");
            pointDao = new PointDaoImpl(conn);
            advancedPointDao = new AdvancedPointDaoImpl(conn);
            functionDao = new FunctionDaoImpl(conn);
            log.info("✅ PointServlet initialized successfully");
        } catch (Exception e) {
            log.error("❌ Failed to initialize PointServlet", e);
            throw new ServletException("Database connection failed", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("🔍 GET request for points: {}", request.getPathInfo());

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /api/points?functionId=... - получить точки функции
                getPointsByFunction(request, response);
            } else if (pathInfo.matches("/\\d+")) {
                // GET /api/points/{id} - получить точку по ID
                Long pointId = Long.parseLong(pathInfo.substring(1));
                getPointById(pointId, response);
            } else if (pathInfo.equals("/range")) {
                // GET /api/points/range?functionId=...&minX=...&maxX=...
                getPointsInRange(request, response);
            } else if (pathInfo.equals("/sorted")) {
                // GET /api/points/sorted?functionId=...&sortBy=...&ascending=...
                getSortedPoints(request, response);
            } else if (pathInfo.equals("/extremum")) {
                // GET /api/points/extremum?functionId=...&type=... - экстремумы
                getExtremumPoints(request, response);
            } else if (pathInfo.equals("/statistics")) {
                // GET /api/points/statistics?functionId=... - статистика
                getPointsStatistics(request, response);
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
        log.info("📝 POST request for points");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                // POST /api/points - создать новую точку
                createPoint(request, response);
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
        log.info("✏️ PUT request for points");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo != null && pathInfo.matches("/\\d+")) {
                // PUT /api/points/{id} - обновить точку
                Long pointId = Long.parseLong(pathInfo.substring(1));
                updatePoint(pointId, request, response);
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
        log.info("🗑️ DELETE request for points");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo != null && pathInfo.matches("/\\d+")) {
                // DELETE /api/points/{id} - удалить точку
                Long pointId = Long.parseLong(pathInfo.substring(1));
                deletePoint(pointId, response);
            } else {
                JsonResponseHelper.sendError(response, 404, "Endpoint not found");
            }
        } catch (Exception e) {
            log.error("❌ Error processing DELETE request", e);
            JsonResponseHelper.sendError(response, 500, "Internal server error: " + e.getMessage());
        }
    }

    private void getPointsByFunction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long functionId = RequestParser.parseLongParameter(request, "functionId");
        log.info("📊 Getting points for function ID: {}", functionId);

        if (functionId == null) {
            JsonResponseHelper.sendError(response, 400, "functionId parameter is required");
            return;
        }

        List<PointEntity> points = pointDao.findByFunction(functionId);
        log.info("✅ Found {} points for function {}", points.size(), functionId);
        JsonResponseHelper.sendSuccess(response, points);
    }

    private void getPointById(Long pointId, HttpServletResponse response) throws IOException {
        log.info("🔍 Getting point by ID: {}", pointId);
        // Для получения точки по ID нужно сначала найти её через все точки функций
        List<PointEntity> allPoints = getAllPoints();
        PointEntity point = allPoints.stream()
                .filter(p -> p.getId() == pointId)
                .findFirst()
                .orElse(null);

        if (point != null) {
            log.info("✅ Found point: ({}, {})", point.getxValue(), point.getyValue());
            JsonResponseHelper.sendSuccess(response, point);
        } else {
            log.warn("⚠️ Point not found with ID: {}", pointId);
            JsonResponseHelper.sendError(response, 404, "Point not found");
        }
    }

    private void getPointsInRange(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long functionId = RequestParser.parseLongParameter(request, "functionId");
        Double minX = parseDoubleParameter(request, "minX");
        Double maxX = parseDoubleParameter(request, "maxX");
        Double minY = parseDoubleParameter(request, "minY");
        Double maxY = parseDoubleParameter(request, "maxY");

        log.info("📈 Getting points in range - function: {}, X[{}-{}], Y[{}-{}]",
                functionId, minX, maxX, minY, maxY);

        if (functionId == null) {
            JsonResponseHelper.sendError(response, 400, "functionId parameter is required");
            return;
        }

        List<PointEntity> points;
        if (minX != null && maxX != null) {
            points = advancedPointDao.findByXValueRange(functionId, minX, maxX);
        } else if (minY != null && maxY != null) {
            points = advancedPointDao.findByYValueRange(functionId, minY, maxY);
        } else {
            JsonResponseHelper.sendError(response, 400, "Range parameters (minX/maxX or minY/maxY) are required");
            return;
        }

        log.info("✅ Found {} points in specified range", points.size());
        JsonResponseHelper.sendSuccess(response, points);
    }

    private void getSortedPoints(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long functionId = RequestParser.parseLongParameter(request, "functionId");
        String sortBy = RequestParser.parseStringParameter(request, "sortBy");
        Boolean ascending = Boolean.valueOf(RequestParser.parseStringParameter(request, "ascending"));

        if (functionId == null) {
            JsonResponseHelper.sendError(response, 400, "functionId parameter is required");
            return;
        }

        if (sortBy == null) {
            sortBy = "x";
        }
        if (ascending == null) {
            ascending = true;
        }

        log.info("📊 Getting sorted points for function {} by {} ({})", functionId, sortBy, ascending ? "ASC" : "DESC");

        List<PointEntity> points;
        if ("x".equalsIgnoreCase(sortBy)) {
            points = advancedPointDao.findByFunctionSortedByX(functionId, ascending);
        } else if ("y".equalsIgnoreCase(sortBy)) {
            points = advancedPointDao.findByFunctionSortedByY(functionId, ascending);
        } else {
            JsonResponseHelper.sendError(response, 400, "Invalid sortBy parameter. Use 'x' or 'y'");
            return;
        }

        log.info("✅ Found {} sorted points", points.size());
        JsonResponseHelper.sendSuccess(response, points);
    }

    private void getExtremumPoints(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long functionId = RequestParser.parseLongParameter(request, "functionId");
        String type = RequestParser.parseStringParameter(request, "type");

        log.info("📈 Getting extremum points for function {}: {}", functionId, type);

        if (functionId == null || type == null) {
            JsonResponseHelper.sendError(response, 400, "functionId and type parameters are required");
            return;
        }

        PointEntity extremumPoint;
        switch (type.toLowerCase()) {
            case "maxx":
                extremumPoint = advancedPointDao.findMaxXPoint(functionId);
                break;
            case "minx":
                extremumPoint = advancedPointDao.findMinXPoint(functionId);
                break;
            case "maxy":
                extremumPoint = advancedPointDao.findMaxYPoint(functionId);
                break;
            case "miny":
                extremumPoint = advancedPointDao.findMinYPoint(functionId);
                break;
            default:
                JsonResponseHelper.sendError(response, 400, "Invalid type. Use: maxX, minX, maxY, minY");
                return;
        }

        if (extremumPoint != null) {
            log.info("✅ Found {} point: ({}, {})", type, extremumPoint.getxValue(), extremumPoint.getyValue());
            JsonResponseHelper.sendSuccess(response, extremumPoint);
        } else {
            log.warn("⚠️ No {} point found for function {}", type, functionId);
            JsonResponseHelper.sendError(response, 404, type + " point not found");
        }
    }

    private void getPointsStatistics(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long functionId = RequestParser.parseLongParameter(request, "functionId");
        Double threshold = parseDoubleParameter(request, "threshold");

        log.info("📊 Getting points statistics for function {}", functionId);

        if (functionId == null) {
            JsonResponseHelper.sendError(response, 400, "functionId parameter is required");
            return;
        }

        List<PointEntity> allPoints = pointDao.findByFunction(functionId);
        Map<String, Object> statistics = new HashMap<>();

        // Базовая статистика
        statistics.put("totalPoints", allPoints.size());

        if (!allPoints.isEmpty()) {
            // Статистика по X
            statistics.put("minX", allPoints.stream().mapToDouble(PointEntity::getxValue).min().orElse(0));
            statistics.put("maxX", allPoints.stream().mapToDouble(PointEntity::getxValue).max().orElse(0));
            statistics.put("avgX", allPoints.stream().mapToDouble(PointEntity::getxValue).average().orElse(0));

            // Статистика по Y
            statistics.put("minY", allPoints.stream().mapToDouble(PointEntity::getyValue).min().orElse(0));
            statistics.put("maxY", allPoints.stream().mapToDouble(PointEntity::getyValue).max().orElse(0));
            statistics.put("avgY", allPoints.stream().mapToDouble(PointEntity::getyValue).average().orElse(0));

            // Дополнительная статистика
            if (threshold != null) {
                statistics.put("pointsAboveY", advancedPointDao.findPointsAboveY(functionId, threshold).size());
                statistics.put("pointsBelowY", advancedPointDao.findPointsBelowY(functionId, threshold).size());
            }
        }

        log.info("✅ Calculated statistics for {} points", allPoints.size());
        JsonResponseHelper.sendSuccess(response, statistics);
    }

    private void createPoint(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("📈 Creating new point");

        PointEntity newPoint = RequestParser.parseJsonRequest(request, PointEntity.class);

        if (newPoint.getFunctionId() == 0) {
            JsonResponseHelper.sendError(response, 400, "functionId is required");
            return;
        }

        pointDao.insert(newPoint);
        log.info("✅ Created new point: ({}, {}) for function {}",
                newPoint.getxValue(), newPoint.getyValue(), newPoint.getFunctionId());

        JsonResponseHelper.sendSuccess(response, newPoint);
    }

    private void updatePoint(Long pointId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("✏️ Updating point with ID: {}", pointId);

        // Находим точку для проверки существования
        List<PointEntity> allPoints = getAllPoints();
        PointEntity existingPoint = allPoints.stream()
                .filter(p -> p.getId() == pointId)
                .findFirst()
                .orElse(null);

        if (existingPoint == null) {
            JsonResponseHelper.sendError(response, 404, "Point not found");
            return;
        }

        PointEntity updatedPoint = RequestParser.parseJsonRequest(request, PointEntity.class);
        updatedPoint.setId(pointId);

        pointDao.update(updatedPoint);
        log.info("✅ Updated point with ID: {}", pointId);

        JsonResponseHelper.sendSuccess(response, updatedPoint);
    }

    private void deletePoint(Long pointId, HttpServletResponse response) throws IOException {
        log.info("🗑️ Deleting point with ID: {}", pointId);

        // Находим точку для проверки существования
        List<PointEntity> allPoints = getAllPoints();
        PointEntity existingPoint = allPoints.stream()
                .filter(p -> p.getId() == pointId)
                .findFirst()
                .orElse(null);

        if (existingPoint == null) {
            JsonResponseHelper.sendError(response, 404, "Point not found");
            return;
        }

        pointDao.delete(pointId);
        log.info("✅ Deleted point with ID: {}", pointId);

        JsonResponseHelper.sendSuccess(response, Map.of("message", "Point deleted successfully"));
    }

    // Вспомогательные методы
    private List<PointEntity> getAllPoints() {
        // Получаем все точки через все функции
        List<PointEntity> allPoints = new ArrayList<>();
        List<FunctionEntity> allFunctions = functionDao.findAll();

        for (FunctionEntity function : allFunctions) {
            allPoints.addAll(pointDao.findByFunction(function.getId()));
        }
        return allPoints;
    }

    private Double parseDoubleParameter(HttpServletRequest request, String paramName) {
        String paramValue = request.getParameter(paramName);
        if (paramValue != null && !paramValue.trim().isEmpty()) {
            try {
                return Double.parseDouble(paramValue);
            } catch (NumberFormatException e) {
                log.warn("⚠️ Invalid {} parameter: {}", paramName, paramValue);
            }
        }
        return null;
    }

    @Override
    public void destroy() {
        log.info("🛑 Destroying PointServlet...");
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