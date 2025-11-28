package servlet;

import servlet.util.AuthHelper;
import servlet.util.JsonResponseHelper;
import servlet.util.RequestParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class AuthServlet extends HttpServlet {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthServlet.class);

    @Override
    public void init() throws ServletException {
        log.info("🚀 Initializing AuthServlet...");
        log.info("✅ AuthServlet initialized successfully");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("🔐 POST request for auth: {}", request.getPathInfo());

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                // POST /api/auth/login - аутентификация
                login(request, response);
            } else if (pathInfo.equals("/register")) {
                // POST /api/auth/register - регистрация
                register(request, response);
            } else {
                JsonResponseHelper.sendError(response, 404, "Endpoint not found");
            }
        } catch (Exception e) {
            log.error("❌ Error processing POST request", e);
            JsonResponseHelper.sendError(response, 500, "Internal server error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("🔍 GET request for auth: {}", request.getPathInfo());

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /api/auth/me - информация о текущем пользователе
                getCurrentUser(request, response);
            } else if (pathInfo.equals("/users")) {
                // GET /api/auth/users - список пользователей (только для админа)
                getUsers(request, response);
            } else {
                JsonResponseHelper.sendError(response, 404, "Endpoint not found");
            }
        } catch (Exception e) {
            log.error("❌ Error processing GET request", e);
            JsonResponseHelper.sendError(response, 500, "Internal server error: " + e.getMessage());
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("🔑 Login attempt");

        try {
            // Парсим JSON из тела запроса
            Map<String, String> credentials = RequestParser.parseJsonRequest(request, Map.class);
            String username = credentials.get("username");
            String password = credentials.get("password");

            if (username == null || password == null) {
                JsonResponseHelper.sendError(response, 400, "Username and password are required");
                return;
            }

            // Проверяем учетные данные
            AuthHelper.UserCredentials user = AuthHelper.authenticate(request);
            if (user != null && user.username.equals(username)) {
                log.info("✅ Login successful for user: {}", username);
                Map<String, Object> result = new HashMap<>();
                result.put("message", "Login successful");
                result.put("user", Map.of(
                        "username", user.username,
                        "role", user.role
                ));
                JsonResponseHelper.sendSuccess(response, result);
            } else {
                log.warn("⚠️ Login failed for user: {}", username);
                JsonResponseHelper.sendError(response, 401, "Invalid credentials");
            }

        } catch (Exception e) {
            log.error("❌ Error in login", e);
            JsonResponseHelper.sendError(response, 500, "Error during login: " + e.getMessage());
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("👤 Registration attempt");

        try {
            // Парсим JSON из тела запроса
            Map<String, String> userData = RequestParser.parseJsonRequest(request, Map.class);
            String username = userData.get("username");
            String password = userData.get("password");
            String role = userData.get("role");

            if (username == null || password == null) {
                JsonResponseHelper.sendError(response, 400, "Username and password are required");
                return;
            }

            // Регистрируем пользователя
            boolean success = AuthHelper.registerUser(username, password, role);
            if (success) {
                log.info("✅ Registration successful for user: {}", username);
                JsonResponseHelper.sendSuccess(response, Map.of("message", "User registered successfully"));
            } else {
                log.warn("⚠️ Registration failed for user: {}", username);
                JsonResponseHelper.sendError(response, 400, "User already exists");
            }

        } catch (Exception e) {
            log.error("❌ Error in register", e);
            JsonResponseHelper.sendError(response, 500, "Error during registration: " + e.getMessage());
        }
    }

    private void getCurrentUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("🔍 Getting current user info");

        try {
            AuthHelper.UserCredentials user = AuthHelper.authenticate(request);
            if (user != null) {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("username", user.username);
                userInfo.put("role", user.role);
                JsonResponseHelper.sendSuccess(response, userInfo);
            } else {
                JsonResponseHelper.sendError(response, 401, "Not authenticated");
            }

        } catch (Exception e) {
            log.error("❌ Error in getCurrentUser", e);
            JsonResponseHelper.sendError(response, 500, "Error getting user info: " + e.getMessage());
        }
    }

    private void getUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("📋 Getting users list");

        try {
            AuthHelper.UserCredentials currentUser = AuthHelper.authenticate(request);
            if (currentUser == null) {
                JsonResponseHelper.sendError(response, 401, "Not authenticated");
                return;
            }

            // Только админ может видеть список пользователей
            if (!AuthHelper.ROLE_ADMIN.equals(currentUser.role)) {
                JsonResponseHelper.sendError(response, 403, "Insufficient permissions");
                return;
            }

            Map<String, String> users = AuthHelper.getUsers();
            JsonResponseHelper.sendSuccess(response, users);

        } catch (Exception e) {
            log.error("❌ Error in getUsers", e);
            JsonResponseHelper.sendError(response, 500, "Error getting users list: " + e.getMessage());
        }
    }

    @Override
    public void destroy() {
        log.info("🛑 Destroying AuthServlet...");
    }
}