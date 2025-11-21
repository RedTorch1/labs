package servlet.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RequestParser {
    private static final Logger log = LoggerFactory.getLogger(RequestParser.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parseJsonRequest(HttpServletRequest request, Class<T> valueType) throws IOException {
        try {
            T result = objectMapper.readValue(request.getInputStream(), valueType);
            log.debug("📥 Parsed JSON request: {}", result);
            return result;
        } catch (IOException e) {
            log.error("❌ Error parsing JSON request for type: {}", valueType.getSimpleName(), e);
            throw e;
        }
    }

    public static Long parseLongParameter(HttpServletRequest request, String paramName) {
        String paramValue = request.getParameter(paramName);
        if (paramValue != null && !paramValue.trim().isEmpty()) {
            try {
                return Long.parseLong(paramValue);
            } catch (NumberFormatException e) {
                log.warn("⚠️ Invalid {} parameter: {}", paramName, paramValue);
            }
        }
        return null;
    }

    public static String parseStringParameter(HttpServletRequest request, String paramName) {
        String paramValue = request.getParameter(paramName);
        return (paramValue != null && !paramValue.trim().isEmpty()) ? paramValue.trim() : null;
    }
}