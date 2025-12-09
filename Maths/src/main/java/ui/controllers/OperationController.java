// src/main/java/ui/controllers/OperationController.java
package ui.controllers;

import functions.TabulatedFunction;
import functions.factory.TabulatedFunctionFactory;
import ui.services.TabulatedFunctionOperationService;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;

@WebServlet("/ui/operations")
public class OperationController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Получаем текущую фабрику из контекста
        TabulatedFunctionFactory factory = MainController.getCurrentFactory(getServletContext());

        // Создаем сервис операций
        TabulatedFunctionOperationService operationService = new TabulatedFunctionOperationService(factory);

        // Сохраняем в атрибутах запроса
        request.setAttribute("operationService", operationService);
        request.setAttribute("factoryType", MainController.getFactoryType(getServletContext()));

        // Отображаем страницу операций
        request.getRequestDispatcher("/WEB-INF/ui/operations.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Обработка AJAX запросов для операций
        String action = request.getParameter("action");

        try {
            // Получаем фабрику и сервис
            TabulatedFunctionFactory factory = MainController.getCurrentFactory(getServletContext());
            TabulatedFunctionOperationService operationService = new TabulatedFunctionOperationService(factory);

            // Здесь будет логика обработки операций с функциями
            // (нужно будет получать функции из сессии, выполнять операции и возвращать результат)

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": true, \"message\": \"Операция выполнена\"}");

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
        }
    }
}