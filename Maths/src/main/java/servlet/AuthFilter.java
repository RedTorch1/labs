package servlet;

import model.User;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        System.out.println("AuthFilter: " + method + " " + requestURI);

        // РАЗРЕШАЕМ доступ к эндпоинтам аутентификации без проверки
        // Это критически важно!
        if (requestURI.equals("/api/auth") ||
                requestURI.equals("/api/auth/") ||
                requestURI.equals("/api/auth/register")) {
            System.out.println("AuthFilter: Allowing access to auth endpoint without authentication");
            chain.doFilter(request, response);
            return;
        }

        // Разрешаем доступ к статическим ресурсам и UI страницам
        if (requestURI.startsWith("/css/") ||
                requestURI.startsWith("/js/") ||
                requestURI.startsWith("/images/") ||
                requestURI.startsWith("/ui/") ||
                requestURI.equals("/") ||
                requestURI.endsWith(".jsp")) {
            System.out.println("AuthFilter: Allowing access to static/UI resource");
            chain.doFilter(request, response);
            return;
        }

        // Для всех остальных запросов проверяем аутентификацию
        HttpSession session = httpRequest.getSession(false);
        boolean isAuthenticated = session != null && session.getAttribute("user") != null;

        // Также проверяем Basic Auth header
        if (!isAuthenticated) {
            String authHeader = httpRequest.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Basic ")) {
                // Если есть Basic Auth header, пропускаем
                // AuthServlet сам проверит валидность
                System.out.println("AuthFilter: Basic Auth header found, passing to servlet");
                chain.doFilter(request, response);
                return;
            }
        }

        if (!isAuthenticated) {
            System.out.println("Authentication failed for: " + requestURI);

            // Для API запросов возвращаем 401
            if (requestURI.startsWith("/api/")) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"error\": \"Authentication required\"}");
                return;
            } else {
                // Для UI запросов перенаправляем на страницу входа
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/ui/");
                return;
            }
        }

        // Пользователь аутентифицирован - пропускаем дальше
        chain.doFilter(request, response);
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