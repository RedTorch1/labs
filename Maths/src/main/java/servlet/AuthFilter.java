package servlet;

import servlet.util.AuthHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        System.out.println("AuthFilter: " + method + " " + path);

        // Публичные endpoints (не требуют аутентификации)
        if (isPublicEndpoint(path, method)) {
            chain.doFilter(request, response);
            return;
        }

        // Проверка аутентификации
        AuthHelper.UserCredentials user = AuthHelper.authenticate(httpRequest);
        if (user == null) {
            System.out.println("Authentication failed for: " + path);
            AuthHelper.sendAuthError(httpResponse);
            return;
        }

        // Проверка прав доступа
        if (!AuthHelper.hasPermission(user.role, method, path)) {
            System.out.println("Access denied for user: " + user.username + " to " + method + " " + path);
            AuthHelper.sendForbiddenError(httpResponse);
            return;
        }

        System.out.println("Access granted for user: " + user.username + " with role: " + user.role + " to " + method + " " + path);

        // Добавляем информацию о пользователе в запрос
        request.setAttribute("currentUser", user.username);
        request.setAttribute("currentUserRole", user.role);

        chain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String path, String method) {
        // Endpoints для регистрации и health check
        return (path.equals("/lab6/api/auth/register") && method.equals("POST")) ||
                (path.equals("/lab6/api/health") && method.equals("GET"));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AuthFilter initialized");
    }

    @Override
    public void destroy() {
        System.out.println("AuthFilter destroyed");
    }
}