package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.impl.FunctionDaoImpl;
import dao.impl.PointDaoImpl;
import model.FunctionEntity;
import model.PointEntity;
import servlet.util.JsonResponseHelper;

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

@WebServlet("/api/functions/save-from-arrays")
public class SaveFromArraysServlet extends HttpServlet {
    private FunctionDaoImpl functionDao;
    private PointDaoImpl pointDao;
    private Connection conn;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");

            String dbUrl = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/lab5");
            String dbUser = System.getenv().getOrDefault("DB_USER", "postgres");
            String dbPassword = System.getenv().getOrDefault("DB_PASSWORD", "postgres");

            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            functionDao = new FunctionDaoImpl(conn);
            pointDao = new PointDaoImpl(conn);
        } catch (Exception e) {
            throw new ServletException("Database connection failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Проверяем авторизацию (базовая аутентификация)
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Basic ")) {
                JsonResponseHelper.sendError(response, 401, "Требуется авторизация");
                return;
            }

            // Читаем данные запроса
            String requestBody = request.getReader().lines()
                    .reduce("", (accumulator, actual) -> accumulator + actual);

            Map<String, Object> data = objectMapper.readValue(requestBody, Map.class);

            String name = (String) data.get("name");
            String expression = (String) data.get("expression");
            Number userIdNum = (Number) data.get("userId");
            List<Map<String, Number>> points = (List<Map<String, Number>>) data.get("points");

            if (name == null || expression == null || userIdNum == null || points == null || points.isEmpty()) {
                JsonResponseHelper.sendError(response, 400, "Не все обязательные поля заполнены");
                return;
            }

            long userId = userIdNum.longValue();

            // Создаем функцию
            FunctionEntity function = new FunctionEntity();
            function.setUserId(userId);
            function.setName(name);
            function.setExpression(expression);

            functionDao.insert(function);

            // Получаем ID созданной функции
            List<FunctionEntity> userFunctions = functionDao.findByUser(userId);
            FunctionEntity savedFunction = userFunctions.stream()
                    .filter(f -> f.getName().equals(name) && f.getExpression().equals(expression))
                    .findFirst()
                    .orElse(null);

            if (savedFunction == null) {
                JsonResponseHelper.sendError(response, 500, "Ошибка при сохранении функции");
                return;
            }

            // Сохраняем точки
            for (Map<String, Number> pointData : points) {
                PointEntity point = new PointEntity();
                point.setFunctionId(savedFunction.getId());
                point.setxValue(pointData.get("x").doubleValue());
                point.setyValue(pointData.get("y").doubleValue());
                pointDao.insert(point);
            }

            // Возвращаем успешный ответ
            Map<String, Object> responseData = Map.of(
                    "success", true,
                    "id", savedFunction.getId(),
                    "name", savedFunction.getName(),
                    "expression", savedFunction.getExpression(),
                    "userId", savedFunction.getUserId(),
                    "pointsCount", points.size()
            );

            JsonResponseHelper.sendSuccess(response, responseData);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponseHelper.sendError(response, 500, "Ошибка сервера: " + e.getMessage());
        }
    }

    @Override
    public void destroy() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}