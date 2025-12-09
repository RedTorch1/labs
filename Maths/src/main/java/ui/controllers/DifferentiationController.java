// src/main/java/ui/controllers/DifferentiationController.java
package ui.controllers;

import functions.TabulatedFunction;
import functions.factory.TabulatedFunctionFactory;
import ui.services.DifferentialOperator;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;

@WebServlet("/ui/differentiation")
public class DifferentiationController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        TabulatedFunctionFactory factory = MainController.getCurrentFactory(getServletContext());
        DifferentialOperator differentialOperator = new DifferentialOperator(factory);

        request.setAttribute("differentialOperator", differentialOperator);
        request.setAttribute("factoryType", MainController.getFactoryType(getServletContext()));

        request.getRequestDispatcher("/WEB-INF/ui/differentiation.jsp").forward(request, response);
    }
}