package ru.ssau.tk.maths.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/ui/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String returnTo = req.getParameter("returnTo");
        req.setAttribute("returnTo", returnTo);
        req.getRequestDispatcher("/WEB-INF/ui/register.jsp").forward(req, resp);
    }
}

