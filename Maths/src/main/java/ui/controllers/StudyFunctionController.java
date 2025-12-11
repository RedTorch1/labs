package ui.controllers;

import functions.*;
import functions.factory.*;
import io.FunctionsIO;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@WebServlet("/ui/study/*")
@MultipartConfig(maxFileSize = 1024 * 1024 * 10)
public class StudyFunctionController extends HttpServlet {

    private TabulatedFunctionFactory factory;

    @Override
    public void init() throws ServletException {
        super.init();
        factory = new LinkedListTabulatedFunctionFactory();
        System.out.println("StudyFunctionController initialized with " + factory.getClass().getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.startsWith("/download")) {
            handleDownload(request, response);
        } else {
            request.getRequestDispatcher("/WEB-INF/ui/study-function.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try {
            switch (action != null ? action : "") {
                case "apply":
                    handleApply(request, response);
                    break;
                case "upload":
                    handleUpload(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неизвестное действие");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Ошибка сервера: " + e.getMessage());
        }
    }

    private TabulatedFunction getCurrentFunction(HttpSession session) {
        TabulatedFunction function = (TabulatedFunction) session.getAttribute("studyFunction");
        if (function == null) {
            // Функция по умолчанию y = x^2
            double[] xValues = {-2, -1, 0, 1, 2};
            double[] yValues = {4, 1, 0, 1, 4};
            function = factory.create(xValues, yValues);
            session.setAttribute("studyFunction", function);
        }
        return function;
    }

    private void handleApply(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        TabulatedFunction function = getCurrentFunction(session);

        double x = Double.parseDouble(request.getParameter("x"));
        double y = function.apply(x);

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("{\"y\":" + y + "}");
    }

    private void handleUpload(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession();
        Part filePart = request.getPart("file");

        if (filePart == null) {
            sendError(response, "Файл не выбран");
            return;
        }

        try (InputStream inputStream = filePart.getInputStream()) {
            TabulatedFunction function;
            String fileName = filePart.getSubmittedFileName().toLowerCase();

            if (fileName.endsWith(".dat")) {
                function = FunctionsIO.deserialize(new BufferedInputStream(inputStream));
            } else if (fileName.endsWith(".txt")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                function = FunctionsIO.readTabulatedFunction(reader, factory);
            } else {
                sendError(response, "Поддерживаются только .dat и .txt файлы");
                return;
            }

            session.setAttribute("studyFunction", function);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Функция успешно загружена");
            result.put("pointsCount", function.getCount());

            sendJsonResponse(response, result);

        } catch (Exception e) {
            sendError(response, "Ошибка загрузки файла: " + e.getMessage());
        }
    }

    private void handleDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String format = request.getParameter("format");
        if (format == null) format = "dat";

        HttpSession session = request.getSession();
        TabulatedFunction function = getCurrentFunction(session);

        String fileName = "tabulated-function." + format;
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        if ("dat".equals(format)) {
            response.setContentType("application/octet-stream");
            FunctionsIO.serialize(new BufferedOutputStream(response.getOutputStream()), function);
        } else if ("txt".equals(format)) {
            response.setContentType("text/plain;charset=UTF-8");
            FunctionsIO.writeTabulatedFunction(
                    new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8")),
                    function
            );
        }
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("{\"success\":false,\"error\":\"" + message.replace("\"", "\\\"") + "\"}");
    }

    private void sendJsonResponse(HttpServletResponse response, Map<String, Object> data) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (!first) out.print(",");
            first = false;
            out.print("\"" + entry.getKey() + "\":");
            if (entry.getValue() instanceof Number) {
                out.print(entry.getValue());
            } else {
                out.print("\"" + entry.getValue() + "\"");
            }
        }
        out.print("}");
    }
}
