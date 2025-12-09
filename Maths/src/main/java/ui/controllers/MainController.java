// src/main/java/ui/controllers/MainController.java
package ui.controllers;

import functions.factory.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;

@WebServlet({"/ui/main", "/ui/settings"})
public class MainController extends HttpServlet {

    // Храним выбранную фабрику в контексте приложения
    @Override
    public void init() throws ServletException {
        super.init();

        // Инициализируем фабрику по умолчанию
        TabulatedFunctionFactory defaultFactory = new ArrayTabulatedFunctionFactory();
        getServletContext().setAttribute("currentFactory", defaultFactory);
        getServletContext().setAttribute("factoryType", "array");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null || path.equals("/") || path.equals("/main")) {
            // Главная страница
            request.getRequestDispatcher("/WEB-INF/ui/main.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path != null && path.equals("/settings")) {
            // Обработка сохранения настроек
            String factoryType = request.getParameter("factoryType");

            TabulatedFunctionFactory factory;
            if ("linkedlist".equals(factoryType)) {
                factory = new LinkedListTabulatedFunctionFactory();
            } else {
                factory = new ArrayTabulatedFunctionFactory();
            }

            // Сохраняем в контексте приложения
            getServletContext().setAttribute("currentFactory", factory);
            getServletContext().setAttribute("factoryType", factoryType);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": true, \"message\": \"Настройки сохранены\"}");
        }
    }

    // Метод для получения текущей фабрики (может использоваться другими сервлетами)
    public static TabulatedFunctionFactory getCurrentFactory(ServletContext context) {
        return (TabulatedFunctionFactory) context.getAttribute("currentFactory");
    }

    public static String getFactoryType(ServletContext context) {
        return (String) context.getAttribute("factoryType");
    }
}