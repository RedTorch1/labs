// src/main/java/ui/controllers/IndexController.java
package ui.controllers;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;

@WebServlet({"/ui", "/ui/"})
public class IndexController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Перенаправляем на главную страницу
        response.sendRedirect(request.getContextPath() + "/ui/main");
    }
}