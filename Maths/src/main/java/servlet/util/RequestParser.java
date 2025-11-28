package servlet.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestParser {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T parseJsonRequest(HttpServletRequest request, Class<T> clazz) throws IOException {
        return mapper.readValue(request.getInputStream(), clazz);
    }

    public static String parseStringParameter(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        return (value != null && !value.trim().isEmpty()) ? value.trim() : null;
    }

    public static Long parseLongParameter(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        if (value != null && !value.trim().isEmpty()) {
            try {
                return Long.parseLong(value.trim());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}