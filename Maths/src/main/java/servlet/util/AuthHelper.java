package servlet.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class AuthHelper {

    // Роли и их права доступа
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_VIEWER = "VIEWER";

    // In-memory хранилище пользователей (в реальном приложении - база данных)
    private static final Map<String, UserCredentials> users = new HashMap<>();

    static {
        // Инициализация тестовых пользователей
        users.put("admin", new UserCredentials("admin", "admin123", ROLE_ADMIN));
        users.put("user1", new UserCredentials("user1", "user123", ROLE_USER));
        users.put("viewer", new UserCredentials("viewer", "viewer123", ROLE_VIEWER));
    }

    public static class UserCredentials {
        public String username;
        public String password;
        public String role;

        public UserCredentials(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
    }

    // Проверка Basic Auth
    public static UserCredentials authenticate(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return null;
        }

        try {
            String base64Credentials = authHeader.substring("Basic ".length());
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(decodedBytes);

            String[] values = credentials.split(":", 2);
            if (values.length != 2) {
                return null;
            }

            String username = values[0];
            String password = values[1];

            UserCredentials user = users.get(username);
            if (user != null && user.password.equals(password)) {
                return user;
            }

        } catch (Exception e) {
            System.err.println("Auth error: " + e.getMessage());
        }

        return null;
    }

    // Проверка прав доступа
    public static boolean hasPermission(String role, String method, String path) {
        if (ROLE_ADMIN.equals(role)) {
            return true; // Админ имеет доступ ко всему
        }

        if (ROLE_USER.equals(role)) {
            // USER может читать всё и создавать свои данные
            return !path.contains("/admin") &&
                    !method.equals("DELETE") &&
                    !path.matches("/api/users/\\d+") && // Не может изменять/удалять других пользователей
                    !path.equals("/api/users/create-admin");
        }

        if (ROLE_VIEWER.equals(role)) {
            // VIEWER может только читать
            return method.equals("GET") && !path.contains("/admin");
        }

        return false;
    }

    // Отправка ошибки аутентификации
    public static void sendAuthError(HttpServletResponse response) throws IOException {
        response.setHeader("WWW-Authenticate", "Basic realm=\"Lab6 API\"");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
    }

    // Отправка ошибки доступа
    public static void sendForbiddenError(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Insufficient permissions");
    }

    // Регистрация нового пользователя
    public static boolean registerUser(String username, String password, String role) {
        if (users.containsKey(username)) {
            return false; // Пользователь уже существует
        }

        if (!ROLE_ADMIN.equals(role) && !ROLE_USER.equals(role) && !ROLE_VIEWER.equals(role)) {
            role = ROLE_USER; // По умолчанию USER
        }

        users.put(username, new UserCredentials(username, password, role));
        System.out.println("Registered new user: " + username + " with role: " + role);
        return true;
    }

    // Получение списка пользователей (только для админа)
    public static Map<String, String> getUsers() {
        Map<String, String> userList = new HashMap<>();
        for (Map.Entry<String, UserCredentials> entry : users.entrySet()) {
            userList.put(entry.getKey(), entry.getValue().role);
        }
        return userList;
    }
}