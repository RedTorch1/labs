package ui.controllers;

import functions.*;
import functions.factory.*;
import io.FunctionsIO;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.util.*;

@WebServlet({"/ui/functions", "/ui/functions/*"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 10)
public class FunctionUIController extends HttpServlet {
    private TabulatedFunctionFactory factory;
    private Map<String, MathFunction> functionMap;

    @Override
    public void init() throws ServletException {
        super.init();

        System.out.println("Initializing FunctionUIController...");

        // Инициализация фабрики
        try {
            factory = new LinkedListTabulatedFunctionFactory();
            System.out.println("Factory initialized: " + factory.getClass().getName());
        } catch (Exception e) {
            System.err.println("Failed to initialize factory: " + e.getMessage());
            factory = createDefaultFactory();
        }

        // Инициализация карты функций
        functionMap = new LinkedHashMap<>();
        initializeFunctionMap();

        System.out.println("FunctionUIController initialized with " + functionMap.size() + " functions");
    }

    private TabulatedFunctionFactory createDefaultFactory() {
        return new TabulatedFunctionFactory() {
            @Override
            public TabulatedFunction create(double[] xValues, double[] yValues) {
                throw new UnsupportedOperationException("Default factory - not implemented");
            }

            @Override
            public TabulatedFunction create(MathFunction source, double xFrom, double xTo, int pointsCount) {
                throw new UnsupportedOperationException("Default factory - not implemented");
            }
        };
    }

    private void initializeFunctionMap() {
        Map<String, MathFunction> unsortedMap = new HashMap<>();

        unsortedMap.put("Квадратичная функция", new SqrFunction());
        unsortedMap.put("Тождественная функция", new IdentityFunction());
        unsortedMap.put("Постоянная функция (0)", new ConstantFunction(0));
        unsortedMap.put("Постоянная функция (1)", new ConstantFunction(1));
        unsortedMap.put("Синусоида", new MathFunction() {
            @Override
            public double apply(double x) {
                return Math.sin(x);
            }

            @Override
            public String toString() {
                return "Синусоида";
            }
        });

        List<String> sortedKeys = new ArrayList<>(unsortedMap.keySet());
        Collections.sort(sortedKeys);

        for (String key : sortedKeys) {
            functionMap.put(key, unsortedMap.get(key));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();

        try {
            if (path == null || path.equals("/")) {
                // Возвращаем JSON со списком функций для AJAX
                sendFunctionList(response);
            } else if (path.equals("/create-from-arrays")) {
                // Отображаем форму для создания из массивов
                request.getRequestDispatcher("/WEB-INF/ui/create-from-arrays.jsp").forward(request, response);
            } else if (path.equals("/create-from-function")) {
                // Отображаем форму для создания из функции
                request.getRequestDispatcher("/WEB-INF/ui/create-from-function.jsp").forward(request, response);
            } else if (path.equals("/download")) {
                // Новый endpoint для скачивания файла
                downloadFunction(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Path not found: " + path);
            }
        } catch (Exception e) {
            System.err.println("Error in doGet: " + e.getMessage());
            sendError(response, "Ошибка при обработке запроса", e, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String path = request.getPathInfo();

        try {
            if (path == null) {
                throw new IllegalArgumentException("Path is null");
            }

            if (path.equals("/create-from-arrays")) {
                createFromArrays(request, response);
            } else if (path.equals("/create-from-function")) {
                createFromFunction(request, response);
            } else if (path.equals("/upload")) {
                // Новый endpoint для загрузки файла
                uploadFunction(request, response);
            } else {
                throw new IllegalArgumentException("Unknown path: " + path);
            }
        } catch (Exception e) {
            System.err.println("Error in doPost: " + e.getMessage());
            sendError(response, "Ошибка при обработке запроса", e, HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void sendFunctionList(HttpServletResponse response) throws IOException {
        List<String> functionNames = new ArrayList<>(functionMap.keySet());

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        out.print("[");
        boolean first = true;
        for (String functionName : functionNames) {
            if (!first) {
                out.print(",");
            }
            first = false;
            out.print("\"" + escapeJson(functionName) + "\"");
        }
        out.print("]");
    }

    private void createFromArrays(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        System.out.println("=== createFromArrays called ===");

        try {
            // Получаем параметры возврата
            String returnTo = request.getParameter("returnTo");
            String panel = request.getParameter("panel");

            System.out.println("Return parameters - returnTo: " + returnTo + ", panel: " + panel);

            // Проверяем Content-Type
            String contentType = request.getContentType();
            System.out.println("Content-Type: " + contentType);

            Map<String, String> params;

            if (contentType != null && contentType.contains("multipart/form-data")) {
                // Для multipart данных (если отправляется через FormData)
                params = handleMultipartParams(request);
            } else if (contentType != null && contentType.contains("application/x-www-form-urlencoded")) {
                // Для обычных форм
                params = parseFormUrlEncodedFromRequest(request);
            } else {
                // Пробуем прочитать тело запроса
                params = parseRequestBody(request);
            }

            // Получаем количество точек
            String pointsCountStr = params.get("pointsCount");
            if (pointsCountStr == null || pointsCountStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Не указано количество точек");
            }

            int pointsCount = Integer.parseInt(pointsCountStr);
            System.out.println("Points count: " + pointsCount);

            // Собираем массивы
            double[] xValues = new double[pointsCount];
            double[] yValues = new double[pointsCount];

            for (int i = 0; i < pointsCount; i++) {
                String xKey = "x" + i;
                String yKey = "y" + i;

                String xValue = params.get(xKey);
                String yValue = params.get(yKey);

                System.out.println("Point " + i + ": " + xKey + "=" + xValue + ", " + yKey + "=" + yValue);

                if (xValue == null || xValue.trim().isEmpty()) {
                    throw new IllegalArgumentException("Точка " + (i + 1) + ": не указано значение X");
                }
                if (yValue == null || yValue.trim().isEmpty()) {
                    throw new IllegalArgumentException("Точка " + (i + 1) + ": не указано значение Y");
                }

                xValues[i] = Double.parseDouble(xValue.trim());
                yValues[i] = Double.parseDouble(yValue.trim());
            }

            // Проверка на возрастание X
            for (int i = 1; i < pointsCount; i++) {
                if (xValues[i] <= xValues[i - 1]) {
                    throw new IllegalArgumentException(
                            "Значения X должны быть строго возрастающими. " +
                                    "Ошибка в точках " + i + " и " + (i + 1)
                    );
                }
            }

            // Создание функции
            TabulatedFunction function = factory.create(xValues, yValues);

            // Используем новый метод отправки ответа
            sendSuccessResponse(response, function, null, returnTo, panel);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неверный формат числа: " + e.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании функции: " + e.getMessage());
        }
    }

    private Map<String, String> parseRequestBody(HttpServletRequest request) throws IOException {
        Map<String, String> params = new HashMap<>();

        // Пробуем прочитать тело запроса
        BufferedReader reader = request.getReader();
        StringBuilder bodyBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            bodyBuilder.append(line);
        }
        String body = bodyBuilder.toString();

        System.out.println("Request body: " + body);

        if (body != null && !body.isEmpty()) {
            return parseFormUrlEncoded(body);
        } else {
            // Если тело пустое, пробуем получить параметры через getParameter
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                params.put(paramName, request.getParameter(paramName));
            }
        }

        return params;
    }

    private Map<String, String> parseFormUrlEncodedFromRequest(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();

        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            params.put(paramName, request.getParameter(paramName));
        }

        return params;
    }

    private Map<String, String> handleMultipartParams(HttpServletRequest request)
            throws IOException, ServletException {
        Map<String, String> params = new HashMap<>();

        Collection<Part> parts = request.getParts();
        for (Part part : parts) {
            String name = part.getName();
            InputStream is = part.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String value = reader.readLine();
            if (value != null) {
                params.put(name, value);
            }
        }
        return params;
    }

    private Map<String, String> parseFormUrlEncoded(String body) {
        Map<String, String> params = new HashMap<>();

        if (body != null && !body.isEmpty()) {
            String[] pairs = body.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    try {
                        String key = java.net.URLDecoder.decode(keyValue[0], "UTF-8");
                        String value = java.net.URLDecoder.decode(keyValue[1], "UTF-8");
                        params.put(key, value);
                    } catch (UnsupportedEncodingException e) {
                        // Игнорируем, используем как есть
                        params.put(keyValue[0], keyValue[1]);
                    }
                }
            }
        }

        return params;
    }

    private void createFromFunction(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        System.out.println("=== createFromFunction called ===");

        try {
            // Получаем параметры возврата
            String returnTo = request.getParameter("returnTo");
            String panel = request.getParameter("panel");

            System.out.println("Return parameters - returnTo: " + returnTo + ", panel: " + panel);

            // Получаем параметры
            String functionName = request.getParameter("functionName");
            System.out.println("Function name: " + functionName);

            if (functionName == null || functionName.trim().isEmpty()) {
                throw new IllegalArgumentException("Не выбрана функция");
            }

            MathFunction mathFunction = functionMap.get(functionName);
            if (mathFunction == null) {
                throw new IllegalArgumentException("Функция не найдена: " + functionName);
            }

            double xFrom = parseDoubleParameter(request, "xFrom", "Начало интервала");
            double xTo = parseDoubleParameter(request, "xTo", "Конец интервала");
            int pointsCount = parseIntParameter(request, "pointsCount", "Количество точек");

            // Валидация
            if (xFrom >= xTo) {
                throw new IllegalArgumentException("Начало интервала должно быть меньше конца");
            }

            if (pointsCount < 2) {
                throw new IllegalArgumentException("Количество точек должно быть не менее 2");
            }

            // Создание функции через фабрику
            TabulatedFunction function = factory.create(mathFunction, xFrom, xTo, pointsCount);

            // Используем новый метод отправки ответа
            sendSuccessResponse(response, function, functionName, returnTo, panel);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неверный формат числа: " + e.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании функции: " + e.getMessage());
        }
    }

    private double parseDoubleParameter(HttpServletRequest request, String paramName, String fieldName) {
        String value = request.getParameter(paramName);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не указано");
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + ": неверный формат числа");
        }
    }

    private int parseIntParameter(HttpServletRequest request, String paramName, String fieldName) {
        String value = request.getParameter(paramName);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не указано");
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + ": неверный формат числа");
        }
    }

    private void sendJsonResponse(HttpServletResponse response, Map<String, Object> data) throws IOException {
        PrintWriter out = response.getWriter();

        out.print("{");
        boolean first = true;

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (!first) {
                out.print(",");
            }
            first = false;

            out.print("\"");
            out.print(entry.getKey());
            out.print("\":");

            Object value = entry.getValue();
            if (value instanceof String) {
                out.print("\"");
                out.print(escapeJson((String) value));
                out.print("\"");
            } else if (value instanceof Boolean) {
                out.print(value);
            } else if (value instanceof Number) {
                out.print(value);
            } else if (value instanceof double[]) {
                double[] array = (double[]) value;
                out.print("[");
                for (int i = 0; i < array.length; i++) {
                    if (i > 0) out.print(",");
                    out.print(array[i]);
                }
                out.print("]");
            } else {
                out.print("\"");
                out.print(escapeJson(String.valueOf(value)));
                out.print("\"");
            }
        }

        out.print("}");
    }

    private void sendError(HttpServletResponse response, String message, Exception e, int statusCode)
            throws IOException {
        response.setStatus(statusCode);

        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", message);
        error.put("details", e.getMessage());

        sendJsonResponse(response, error);
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private void sendSuccessResponse(HttpServletResponse response, TabulatedFunction function,
                                     String functionName, String returnTo, String panel)
            throws IOException {

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Функция успешно создана" + (functionName != null ? " из " + functionName : ""));
        result.put("pointsCount", function.getCount());
        result.put("leftBound", function.leftBound());
        result.put("rightBound", function.rightBound());

        // Добавляем данные функции для передачи
        double[] xValues = new double[function.getCount()];
        double[] yValues = new double[function.getCount()];

        for (int i = 0; i < function.getCount(); i++) {
            xValues[i] = function.getX(i);
            yValues[i] = function.getY(i);
        }

        result.put("xValues", xValues);
        result.put("yValues", yValues);

        // Если нужно вернуть данные в родительское окно
        if (returnTo != null && panel != null) {
            result.put("returnTo", returnTo);
            result.put("panel", panel);
        }

        sendJsonResponse(response, result);
    }

    // ========== МЕТОДЫ ДЛЯ РАБОТЫ С ФАЙЛАМИ ==========

    private void uploadFunction(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        System.out.println("=== uploadFunction called ===");

        try {
            // Получаем uploaded файл
            Part filePart = request.getPart("file");
            if (filePart == null) {
                throw new IllegalArgumentException("Файл не был загружен");
            }

            String fileName = filePart.getSubmittedFileName();
            System.out.println("Uploading file: " + fileName);

            // Определяем тип файла по расширению
            InputStream fileContent = filePart.getInputStream();
            TabulatedFunction function;

            if (fileName.toLowerCase().endsWith(".dat")) {
                // Бинарная сериализация через FunctionsIO
                BufferedInputStream bufferedStream = new BufferedInputStream(fileContent);
                function = FunctionsIO.deserialize(bufferedStream);
                System.out.println("Function deserialized from .dat file");

            } else if (fileName.toLowerCase().endsWith(".txt")) {
                // Текстовый формат через FunctionsIO.readTabulatedFunction
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileContent, "UTF-8"));
                function = FunctionsIO.readTabulatedFunction(reader, factory);
                System.out.println("Function read from .txt file");

            } else if (fileName.toLowerCase().endsWith(".json")) {
                // JSON формат (для совместимости)
                function = parseJsonToFunction(fileContent);
                System.out.println("Function parsed from JSON file");

            } else {
                throw new IllegalArgumentException("Неподдерживаемый формат файла. Используйте .dat, .txt или .json");
            }

            // Подготавливаем данные для отправки
            Map<String, Object> result = prepareFunctionData(function, "Функция успешно загружена из файла: " + fileName);

            // Добавляем параметры возврата
            String returnTo = request.getParameter("returnTo");
            String panel = request.getParameter("panel");
            if (returnTo != null && panel != null) {
                result.put("returnTo", returnTo);
                result.put("panel", panel);
            }

            sendJsonResponse(response, result);

        } catch (Exception e) {
            System.err.println("Error uploading file: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Ошибка при загрузке файла");
            error.put("details", e.getMessage());

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            sendJsonResponse(response, error);
        }
    }

    private TabulatedFunction parseJsonToFunction(InputStream inputStream) throws Exception {
        // Читаем JSON
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }

        String json = jsonBuilder.toString();
        System.out.println("Parsing JSON: " + json.substring(0, Math.min(200, json.length())) + "...");

        // Простой парсинг JSON без внешних библиотек
        json = json.trim();
        if (!json.startsWith("{") || !json.endsWith("}")) {
            throw new IllegalArgumentException("Invalid JSON format");
        }

        // Извлекаем массивы xValues и yValues
        String xValuesStr = extractJsonArray(json, "xValues");
        String yValuesStr = extractJsonArray(json, "yValues");

        if (xValuesStr == null || yValuesStr == null) {
            throw new IllegalArgumentException("JSON должен содержать xValues и yValues");
        }

        double[] xValues = parseJsonArray(xValuesStr);
        double[] yValues = parseJsonArray(yValuesStr);

        if (xValues.length != yValues.length) {
            throw new IllegalArgumentException("Массивы xValues и yValues должны быть одинаковой длины");
        }

        return factory.create(xValues, yValues);
    }

    private String extractJsonArray(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey);
        if (start == -1) return null;

        start += searchKey.length();
        int bracketStart = json.indexOf('[', start);
        if (bracketStart == -1) return null;

        int bracketCount = 1;
        int end = bracketStart + 1;

        while (end < json.length() && bracketCount > 0) {
            char c = json.charAt(end);
            if (c == '[') bracketCount++;
            if (c == ']') bracketCount--;
            end++;
        }

        return json.substring(bracketStart, end);
    }

    private double[] parseJsonArray(String arrayStr) {
        // Убираем квадратные скобки и разбиваем по запятым
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

    private void downloadFunction(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        System.out.println("=== downloadFunction called ===");

        try {
            // Получаем данные функции из параметров
            String xValuesParam = request.getParameter("xValues");
            String yValuesParam = request.getParameter("yValues");
            String format = request.getParameter("format");
            String fileName = request.getParameter("fileName");

            if (xValuesParam == null || yValuesParam == null) {
                throw new IllegalArgumentException("Не указаны данные функции");
            }

            // Парсим массивы
            double[] xValues = parseDoubleArray(xValuesParam);
            double[] yValues = parseDoubleArray(yValuesParam);

            if (xValues.length != yValues.length) {
                throw new IllegalArgumentException("Массивы X и Y должны быть одинаковой длины");
            }

            // Создаем функцию
            TabulatedFunction function = factory.create(xValues, yValues);

            // Настраиваем имя файла по умолчанию
            if (fileName == null || fileName.trim().isEmpty()) {
                fileName = "function";
            }

            if (format == null || format.equals("binary")) {
                // Бинарный формат (.dat) - используем FunctionsIO.serialize()
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition",
                        "attachment; filename=\"" + fileName + ".dat\"");

                // Сериализуем функцию через FunctionsIO
                BufferedOutputStream bufferedOut = new BufferedOutputStream(response.getOutputStream());
                FunctionsIO.serialize(bufferedOut, function);
                bufferedOut.flush();

            } else if (format.equals("txt")) {
                // Текстовый формат (.txt) - используем FunctionsIO.writeTabulatedFunction
                response.setContentType("text/plain;charset=UTF-8");
                response.setHeader("Content-Disposition",
                        "attachment; filename=\"" + fileName + ".txt\"");

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
                FunctionsIO.writeTabulatedFunction(writer, function);
                writer.flush();

            } else if (format.equals("json")) {
                // JSON формат (дополнительный)
                response.setContentType("application/json;charset=UTF-8");
                response.setHeader("Content-Disposition",
                        "attachment; filename=\"" + fileName + ".json\"");

                Map<String, Object> functionData = prepareFunctionData(function, null);
                PrintWriter out = response.getWriter();

                // Простой JSON сериализатор
                out.print("{\"xValues\":[");
                for (int i = 0; i < xValues.length; i++) {
                    if (i > 0) out.print(",");
                    out.print(xValues[i]);
                }
                out.print("],\"yValues\":[");
                for (int i = 0; i < yValues.length; i++) {
                    if (i > 0) out.print(",");
                    out.print(yValues[i]);
                }
                out.print("],\"pointsCount\":" + function.getCount() + ",");
                out.print("\"leftBound\":" + function.leftBound() + ",");
                out.print("\"rightBound\":" + function.rightBound() + ",");
                out.print("\"createdAt\":\"" + new Date().toString() + "\"}");
                out.flush();
            }

        } catch (Exception e) {
            System.err.println("Error downloading function: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ошибка при скачивании: " + e.getMessage());
        }
    }

    private Map<String, Object> prepareFunctionData(TabulatedFunction function, String message) {
        Map<String, Object> result = new HashMap<>();

        if (message != null) {
            result.put("success", true);
            result.put("message", message);
        }

        result.put("pointsCount", function.getCount());
        result.put("leftBound", function.leftBound());
        result.put("rightBound", function.rightBound());

        double[] xValues = new double[function.getCount()];
        double[] yValues = new double[function.getCount()];

        for (int i = 0; i < function.getCount(); i++) {
            xValues[i] = function.getX(i);
            yValues[i] = function.getY(i);
        }

        result.put("xValues", xValues);
        result.put("yValues", yValues);

        return result;
    }

    private double[] parseDoubleArray(String arrayString) {
        // Обрабатываем JSON массив или простой список
        String cleaned = arrayString.trim();

        if (cleaned.startsWith("[") && cleaned.endsWith("]")) {
            cleaned = cleaned.substring(1, cleaned.length() - 1);
        }

        if (cleaned.isEmpty()) {
            return new double[0];
        }

        String[] parts = cleaned.split(",");
        double[] result = new double[parts.length];

        for (int i = 0; i < parts.length; i++) {
            result[i] = Double.parseDouble(parts[i].trim());
        }

        return result;
    }
}