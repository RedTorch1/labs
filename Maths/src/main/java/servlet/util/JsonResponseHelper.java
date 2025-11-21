package servlet.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonResponseHelper {
    private static final Logger log = LoggerFactory.getLogger(JsonResponseHelper.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendSuccess(HttpServletResponse response, Object data) throws IOException {
        sendJsonResponse(response, 200, "Success", data);
    }

    public static void sendError(HttpServletResponse response, int statusCode, String message) throws IOException {
        sendJsonResponse(response, statusCode, message, null);
    }

    public static void sendJsonResponse(HttpServletResponse response, int statusCode, String message, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", statusCode);
        responseMap.put("message", message);
        if (data != null) {
            responseMap.put("data", data);
        }

        String jsonResponse = objectMapper.writeValueAsString(responseMap);
        response.getWriter().write(jsonResponse);

        log.debug("📤 Sent JSON response: status={}, message={}", statusCode, message);
    }
}