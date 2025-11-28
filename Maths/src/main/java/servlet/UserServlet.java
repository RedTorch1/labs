package servlet;

import dao.impl.UserDaoImpl;
import model.User;
import servlet.util.JsonResponseHelper;
import servlet.util.RequestParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class UserServlet extends HttpServlet {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserServlet.class);
    private UserDaoImpl userDao;
    private Connection conn;

    @Override
    public void init() throws ServletException {
        try {
            log.info("🚀 Initializing UserServlet...");

            // Явно загружаем драйвер
            try {
                Class.forName("org.postgresql.Driver");
                log.info("✅ PostgreSQL Driver loaded");
            } catch (ClassNotFoundException e) {
                log.error("❌ PostgreSQL Driver not found");
                throw new ServletException("PostgreSQL Driver not found", e);
            }

            String dbUrl = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/lab5");
            String dbUser = System.getenv().getOrDefault("DB_USER", "postgres");
            String dbPassword = System.getenv().getOrDefault("DB_PASSWORD", "postgres");

            log.info("🔗 Connecting to: {}", dbUrl);
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            log.info("✅ Database connection successful!");

            userDao = new UserDaoImpl(conn);
            log.info("✅ UserDaoImpl created successfully");

            log.info("✅ UserServlet initialized successfully");
        } catch (Exception e) {
            log.error("❌ Failed to initialize UserServlet: {}", e.getMessage());
            throw new ServletException("Database connection failed", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("🔍 GET request for users: {}", request.getPathInfo());

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /api/users - получить всех пользователей
                getAllUsers(request, response);
            } else if (pathInfo.matches("/\\d+")) {
                // GET /api/users/{id} - получить пользователя по ID
                Long userId = Long.parseLong(pathInfo.substring(1));
                getUserById(userId, response);
            } else if (pathInfo.equals("/search")) {
                // GET /api/users/search?username=... - поиск по имени
                searchUsers(request, response);
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
        log.info("📝 POST request for users");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                // POST /api/users - создать нового пользователя
                createUser(request, response);
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
        log.info("✏️ PUT request for users");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo != null && pathInfo.matches("/\\d+")) {
                // PUT /api/users/{id} - обновить пользователя
                Long userId = Long.parseLong(pathInfo.substring(1));
                updateUser(userId, request, response);
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
        log.info("🗑️ DELETE request for users");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo != null && pathInfo.matches("/\\d+")) {
                // DELETE /api/users/{id} - удалить пользователя
                Long userId = Long.parseLong(pathInfo.substring(1));
                deleteUser(userId, response);
            } else {
                JsonResponseHelper.sendError(response, 404, "Endpoint not found");
            }
        } catch (Exception e) {
            log.error("❌ Error processing DELETE request", e);
            JsonResponseHelper.sendError(response, 500, "Internal server error: " + e.getMessage());
        }
    }

    private void getAllUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("📋 Getting all users");
        try {
            List<User> users = userDao.findAll();
            log.info("✅ Found {} users", users.size());
            JsonResponseHelper.sendSuccess(response, users);
        } catch (Exception e) {
            log.error("❌ Error in getAllUsers", e);
            JsonResponseHelper.sendError(response, 500, "Error getting users: " + e.getMessage());
        }
    }

    private void getUserById(Long userId, HttpServletResponse response) throws IOException {
        log.info("🔍 Getting user by ID: {}", userId);
        try {
            User user = userDao.findById(userId);
            if (user != null) {
                log.info("✅ Found user: {}", user.getUsername());
                JsonResponseHelper.sendSuccess(response, user);
            } else {
                log.warn("⚠️ User not found with ID: {}", userId);
                JsonResponseHelper.sendError(response, 404, "User not found");
            }
        } catch (Exception e) {
            log.error("❌ Error in getUserById", e);
            JsonResponseHelper.sendError(response, 500, "Error getting user: " + e.getMessage());
        }
    }

    private void searchUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = RequestParser.parseStringParameter(request, "username");
        log.info("🔍 Searching users with username: {}", username);

        if (username == null) {
            JsonResponseHelper.sendError(response, 400, "Username parameter is required");
            return;
        }

        try {
            dao.impl.AdvancedUserDaoImpl advancedUserDao = new dao.impl.AdvancedUserDaoImpl(conn);
            List<model.User> users = advancedUserDao.findByUsernameContaining(username);
            log.info("✅ Found {} users matching '{}'", users.size(), username);
            JsonResponseHelper.sendSuccess(response, users);
        } catch (Exception e) {
            log.error("❌ Error in searchUsers", e);
            JsonResponseHelper.sendError(response, 500, "Error searching users: " + e.getMessage());
        }
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("👤 Creating new user");

        try {
            User newUser = RequestParser.parseJsonRequest(request, User.class);

            if (newUser.getUsername() == null || newUser.getPasswordHash() == null) {
                JsonResponseHelper.sendError(response, 400, "Username and passwordHash are required");
                return;
            }

            userDao.insert(newUser);
            log.info("✅ Created new user: {}", newUser.getUsername());
            JsonResponseHelper.sendSuccess(response, newUser);
        } catch (Exception e) {
            log.error("❌ Error in createUser", e);
            JsonResponseHelper.sendError(response, 500, "Error creating user: " + e.getMessage());
        }
    }

    private void updateUser(Long userId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("✏️ Updating user with ID: {}", userId);

        try {
            User existingUser = userDao.findById(userId);
            if (existingUser == null) {
                JsonResponseHelper.sendError(response, 404, "User not found");
                return;
            }

            User updatedUser = RequestParser.parseJsonRequest(request, User.class);
            updatedUser.setId(userId);

            userDao.update(updatedUser);
            log.info("✅ Updated user with ID: {}", userId);
            JsonResponseHelper.sendSuccess(response, updatedUser);
        } catch (Exception e) {
            log.error("❌ Error in updateUser", e);
            JsonResponseHelper.sendError(response, 500, "Error updating user: " + e.getMessage());
        }
    }

    private void deleteUser(Long userId, HttpServletResponse response) throws IOException {
        log.info("🗑️ Deleting user with ID: {}", userId);

        try {
            User existingUser = userDao.findById(userId);
            if (existingUser == null) {
                JsonResponseHelper.sendError(response, 404, "User not found");
                return;
            }

            userDao.delete(userId);
            log.info("✅ Deleted user with ID: {}", userId);
            JsonResponseHelper.sendSuccess(response, Map.of("message", "User deleted successfully"));
        } catch (Exception e) {
            log.error("❌ Error in deleteUser", e);
            JsonResponseHelper.sendError(response, 500, "Error deleting user: " + e.getMessage());
        }
    }

    @Override
    public void destroy() {
        log.info("🛑 Destroying UserServlet...");
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