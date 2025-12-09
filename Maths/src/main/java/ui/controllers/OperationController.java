package ui.controllers;

import functions.TabulatedFunction;
import functions.factory.TabulatedFunctionFactory;
import io.FunctionsIO;
import ui.services.TabulatedFunctionOperationService;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/ui/operations")
@MultipartConfig
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

            if ("performOperation".equals(action)) {
                performOperation(request, response, operationService);
            } else if ("uploadFunction".equals(action)) {
                uploadFunctionForOperation(request, response, factory);
            } else {
                throw new IllegalArgumentException("Unknown action: " + action);
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"error\": \"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private void performOperation(HttpServletRequest request, HttpServletResponse response,
                                  TabulatedFunctionOperationService operationService)
            throws Exception {

        // Получаем данные функций из параметров
        String func1Data = request.getParameter("func1Data");
        String func2Data = request.getParameter("func2Data");
        String operation = request.getParameter("operation");

        if (func1Data == null || func2Data == null || operation == null) {
            throw new IllegalArgumentException("Missing required parameters");
        }

        // Парсим данные функций (ожидаем JSON)
        TabulatedFunctionFactory factory = operationService.getFactory();
        TabulatedFunction func1 = parseFunctionFromJson(func1Data, factory);
        TabulatedFunction func2 = parseFunctionFromJson(func2Data, factory);

        // Выполняем операцию
        TabulatedFunction result;
        switch (operation) {
            case "add":
                result = operationService.add(func1, func2);
                break;
            case "subtract":
                result = operationService.subtract(func1, func2);
                break;
            case "multiply":
                result = operationService.multiply(func1, func2);
                break;
            case "divide":
                result = operationService.divide(func1, func2);
                break;
            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }

        // Подготавливаем ответ
        Map<String, Object> resultData = new HashMap<>();
        resultData.put("success", true);
        resultData.put("operation", operation);
        resultData.put("pointsCount", result.getCount());
        resultData.put("leftBound", result.leftBound());
        resultData.put("rightBound", result.rightBound());

        double[] xValues = new double[result.getCount()];
        double[] yValues = new double[result.getCount()];
        for (int i = 0; i < result.getCount(); i++) {
            xValues[i] = result.getX(i);
            yValues[i] = result.getY(i);
        }

        resultData.put("xValues", xValues);
        resultData.put("yValues", yValues);

        // Отправляем ответ
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(convertMapToJson(resultData));
    }

    private TabulatedFunction parseFunctionFromJson(String json, TabulatedFunctionFactory factory)
            throws Exception {
        // Простой парсинг JSON
        json = json.trim();

        // Извлекаем xValues и yValues
        String xValuesStr = extractValue(json, "xValues");
        String yValuesStr = extractValue(json, "yValues");

        double[] xValues = parseJsonArray(xValuesStr);
        double[] yValues = parseJsonArray(yValuesStr);

        return factory.create(xValues, yValues);
    }

    private String extractValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey);
        if (start == -1) return null;

        start += searchKey.length();
        int end = json.indexOf(']', start);
        if (end == -1) return null;

        return json.substring(start, end + 1);
    }

    private double[] parseJsonArray(String arrayStr) {
        // Убираем квадратные скобки
        String content = arrayStr.substring(1, arrayStr.length() - 1).trim();
        if (content.isEmpty()) {
            return new double[0];
        }

        String[] parts = content.split(",");
        double[] result = new double[parts.length];

        for (int i = 0; i < parts.length; i++) {
            result[i] = Double.parseDouble(parts[i].trim());
        }

        return result;
    }

    private void uploadFunctionForOperation(HttpServletRequest request, HttpServletResponse response,
                                            TabulatedFunctionFactory factory)
            throws Exception {

        Part filePart = request.getPart("file");
        if (filePart == null) {
            throw new IllegalArgumentException("Файл не был загружен");
        }

        InputStream fileContent = filePart.getInputStream();
        TabulatedFunction function;

        String fileName = filePart.getSubmittedFileName();
        if (fileName.toLowerCase().endsWith(".dat")) {
            BufferedInputStream bufferedStream = new BufferedInputStream(fileContent);
            function = FunctionsIO.deserialize(bufferedStream);
        } else if (fileName.toLowerCase().endsWith(".txt")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileContent, "UTF-8"));
            function = FunctionsIO.readTabulatedFunction(reader, factory);
        } else {
            throw new IllegalArgumentException("Неподдерживаемый формат файла");
        }

        // Подготавливаем ответ
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("fileName", fileName);
        result.put("pointsCount", function.getCount());

        double[] xValues = new double[function.getCount()];
        double[] yValues = new double[function.getCount()];
        for (int i = 0; i < function.getCount(); i++) {
            xValues[i] = function.getX(i);
            yValues[i] = function.getY(i);
        }

        result.put("xValues", xValues);
        result.put("yValues", yValues);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(convertMapToJson(result));
    }

    private String convertMapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                json.append(",");
            }
            first = false;

            json.append("\"").append(entry.getKey()).append("\":");

            Object value = entry.getValue();
            if (value instanceof String) {
                json.append("\"").append(escapeJson((String) value)).append("\"");
            } else if (value instanceof Number) {
                json.append(value);
            } else if (value instanceof double[]) {
                double[] array = (double[]) value;
                json.append("[");
                for (int i = 0; i < array.length; i++) {
                    if (i > 0) json.append(",");
                    json.append(array[i]);
                }
                json.append("]");
            } else {
                json.append("\"").append(escapeJson(String.valueOf(value))).append("\"");
            }
        }

        json.append("}");
        return json.toString();
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}