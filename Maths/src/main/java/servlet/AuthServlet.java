package servlet;

import jakarta.servlet.annotation.WebServlet;
import servlet.util.AuthHelper;
import servlet.util.JsonResponseHelper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/register")) {
            registerUser(request, response);
        } else if (pathInfo.equals("/create-admin")) {
            createAdminUser(request, response);
        } else {
            JsonResponseHelper.sendError(response, 404, "Endpoint not found");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/users")) {
            // Только админы могут просматривать список пользователей
            String currentRole = (String) request.getAttribute("currentUserRole");
            if (!AuthHelper.ROLE_ADMIN.equals(currentRole)) {
                AuthHelper.sendForbiddenError(response);
                return;
            }

            Map<String, String> users = AuthHelper.getUsers();
            JsonResponseHelper.sendSuccess(response, users);

        } else if (pathInfo.equals("/profile")) {
            // Информация о текущем пользователе
            String currentUser = (String) request.getAttribute("currentUser");
            String currentRole = (String) request.getAttribute("currentUserRole");

            Map<String, String> profile = Map.of(
                    "username", currentUser,
                    "role", currentRole
            );

            JsonResponseHelper.sendSuccess(response, profile);

        } else {
            JsonResponseHelper.sendError(response, 404, "Endpoint not found");
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (username == null || password == null) {
            JsonResponseHelper.sendError(response, 400, "Username and password are required");
            return;
        }

        boolean success = AuthHelper.registerUser(username, password, role);

        if (success) {
            JsonResponseHelper.sendSuccess(response, Map.of(
                    "message", "User registered successfully",
                    "username", username,
                    "role", role != null ? role : "USER"
            ));
        } else {
            JsonResponseHelper.sendError(response, 400, "Username already exists");
        }
    }

    private void createAdminUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // Только существующие админы могут создавать новых админов
        String currentRole = (String) request.getAttribute("currentUserRole");
        if (!AuthHelper.ROLE_ADMIN.equals(currentRole)) {
            AuthHelper.sendForbiddenError(response);
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null) {
            JsonResponseHelper.sendError(response, 400, "Username and password are required");
            return;
        }

        boolean success = AuthHelper.registerUser(username, password, AuthHelper.ROLE_ADMIN);

        if (success) {
            JsonResponseHelper.sendSuccess(response, Map.of(
                    "message", "Admin user created successfully",
                    "username", username
            ));
        } else {
            JsonResponseHelper.sendError(response, 400, "Username already exists");
        }
    }
}