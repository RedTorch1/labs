package ru.ssau.tk.maths.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ui/manage-functions")
public class ManageFunctionsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ✅ Просто forward на JSP (данные из localStorage)
        req.getRequestDispatcher("/WEB-INF/ui/manage.jsp").forward(req, resp);
    }
}
