package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.impl.FunctionDaoImpl;
import dao.impl.PointDaoImpl;
import model.FunctionEntity;
import model.PointEntity;
import servlet.util.JsonResponseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

@WebServlet("/api/functions/save-from-function")
public class SaveFromFunctionServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(SaveFromFunctionServlet.class);
    private FunctionDaoImpl functionDao;
    private PointDaoImpl pointDao;
    private Connection conn;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        try {
            log.info("🚀 Initializing SaveFromFunctionServlet...");
            Class.forName("org.postgresql.Driver");

            String dbUrl = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/lab5");
            String dbUser = System.getenv().getOrDefault("DB_USER", "postgres");
            String dbPassword = System.getenv().getOrDefault("DB_PASSWORD", "postgres");

            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            functionDao = new FunctionDaoImpl(conn);
            pointDao = new PointDaoImpl(conn);
            log.info("✅ SaveFromFunctionServlet initialized successfully");
        } catch (Exception e) {
            log.error("❌ Failed to initialize SaveFromFunctionServlet", e);
            throw new ServletException("Database connection failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        log.info("📝 POST request to save function from mathematical function");

        try {
            // Читаем данные запроса
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                requestBody.append(line);
            }

            log.info("Request body: {}", requestBody.toString());

            Map<String, Object> data = objectMapper.readValue(requestBody.toString(), Map.class);

            String name = (String) data.get("name");
            String expression = (String) data.get("expression");
            Object userIdObj = data.get("userId");
            List<Map<String, Object>> points = (List<Map<String, Object>>) data.get("points");

            log.info("Parsed data - name: {}, expression: {}, userId: {}, points count: {}",
                    name, expression, userIdObj, points != null ? points.size() : 0);

            if (name == null || name.trim().isEmpty()) {
                JsonResponseHelper.sendError(response, 400, "Название функции обязательно");
                return;
            }

            if (expression == null || expression.trim().isEmpty()) {
                JsonResponseHelper.sendError(response, 400, "Выражение функции обязательно");
                return;
            }

            if (points == null || points.isEmpty()) {
                JsonResponseHelper.sendError(response, 400, "Точки функции обязательны");
                return;
            }

            // Определяем ID пользователя
            long userId;
            if (userIdObj instanceof Number) {
                userId = ((Number) userIdObj).longValue();
            } else if (userIdObj instanceof String) {
                try {
                    userId = Long.parseLong((String) userIdObj);
                } catch (NumberFormatException e) {
                    userId = 333290L; // fallback для тестирования
                }
            } else {
                userId = 333290L; // fallback для тестирования
            }

            log.info("Using userId: {}", userId);

            // Проверяем уникальность названия (опционально, можно убрать если проверка на клиенте)
            boolean isNameUnique = true;
            try {
                List<FunctionEntity> userFunctions = functionDao.findByUser(userId);
                for (FunctionEntity func : userFunctions) {
                    if (func.getName().equalsIgnoreCase(name.trim())) {
                        isNameUnique = false;
                        break;
                    }
                }
            } catch (Exception e) {
                log.warn("Error checking name uniqueness, continuing anyway", e);
            }

            if (!isNameUnique) {
                JsonResponseHelper.sendError(response, 409, "Функция с названием \"" + name + "\" уже существует");
                return;
            }

            // Создаем функцию
            FunctionEntity function = new FunctionEntity();
            function.setUserId(userId);
            function.setName(name.trim());
            function.setExpression(expression.trim());

            log.info("Inserting function: {}", function);
            functionDao.insert(function);

            // Ищем созданную функцию
            List<FunctionEntity> userFunctions = functionDao.findByUser(userId);
            FunctionEntity savedFunction = null;

            for (FunctionEntity f : userFunctions) {
                if (f.getName().equals(function.getName()) &&
                        f.getUserId() == userId &&
                        f.getExpression().equals(function.getExpression())) {
                    savedFunction = f;
                    break;
                }
            }

            if (savedFunction == null) {
                log.error("Function not found after insertion");
                JsonResponseHelper.sendError(response, 500, "Ошибка при сохранении функции");
                return;
            }

            log.info("Saved function ID: {}", savedFunction.getId());

            // Сохраняем точки
            int savedPoints = 0;
            for (Map<String, Object> pointData : points) {
                try {
                    PointEntity point = new PointEntity();
                    point.setFunctionId(savedFunction.getId());

                    Object xObj = pointData.get("x");
                    Object yObj = pointData.get("y");

                    if (xObj instanceof Number) {
                        point.setxValue(((Number) xObj).doubleValue());
                    } else if (xObj instanceof String) {
                        point.setxValue(Double.parseDouble((String) xObj));
                    }

                    if (yObj instanceof Number) {
                        point.setyValue(((Number) yObj).doubleValue());
                    } else if (yObj instanceof String) {
                        point.setyValue(Double.parseDouble((String) yObj));
                    }

                    pointDao.insert(point);
                    savedPoints++;

                } catch (Exception e) {
                    log.error("Error saving point: {}", pointData, e);
                }
            }

            log.info("Saved {} points for function {}", savedPoints, savedFunction.getId());

            // Возвращаем успешный ответ
            Map<String, Object> responseData = Map.of(
                    "success", true,
                    "id", savedFunction.getId(),
                    "name", savedFunction.getName(),
                    "expression", savedFunction.getExpression(),
                    "userId", savedFunction.getUserId(),
                    "pointsCount", savedPoints
            );

            log.info("Sending response: {}", responseData);
            JsonResponseHelper.sendSuccess(response, responseData);

        } catch (Exception e) {
            log.error("❌ Error in doPost", e);
            JsonResponseHelper.sendError(response, 500, "Ошибка сервера: " + e.getMessage());
        }
    }

    @Override
    public void destroy() {
        log.info("🛑 Destroying SaveFromFunctionServlet...");
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