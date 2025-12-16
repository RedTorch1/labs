package ru.ssau.tk.maths.servlet;

import com.google.gson.Gson;
import ru.ssau.tk.maths.model.User;
import ru.ssau.tk.maths.service.UserService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/api/users/*")
public class UserServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (path == null || "/".equals(path)) {
            // GET /api/users → все пользователи
            List<User> users = UserService.getAllUsers();
            resp.getWriter().write(gson.toJson(users));

        } else if ("/search".equals(path)) {
            // GET /api/users/search?username=admin
            String username = req.getParameter("username");
            List<User> users = UserService.findByUsername(username);
            resp.getWriter().write(gson.toJson(users));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // POST /api/users/signup
        req.setCharacterEncoding("UTF-8");
        BufferedReader reader = req.getReader();
        String json = reader.lines().collect(Collectors.joining());

        try {
            User user = gson.fromJson(json, User.class);
            UserService.createUser(user);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(Map.of("success", true, "message", "Пользователь создан")));

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(Map.of("error", e.getMessage())));
        }
    }
}
