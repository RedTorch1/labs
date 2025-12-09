package ui.controllers;

import functions.*;
import functions.factory.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.util.*;

@WebServlet({"/ui/functions", "/ui/functions/*"})
@MultipartConfig
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
            } else {
                throw new IllegalArgumentException("Unknown path: " + path);
            }
        } catch (Exception e) {
            System.err.println("Error in doPost: " + e.getMessage());
            sendError(response, "Ошибка при создании функции", e, HttpServletResponse.SC_BAD_REQUEST);
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
}