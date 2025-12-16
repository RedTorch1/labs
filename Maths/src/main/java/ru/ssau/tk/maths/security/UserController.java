package ru.ssau.tk.maths.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> request) {
        System.out.println("REQUEST BODY: " + request);  // Лог для отладки

        String username = request.get("username");
        String password = request.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "username и password обязательны"));
        }

        Map<String, String> response = new HashMap<>();
        response.put("success", "Пользователь " + username + " создан!");
        response.put("id", "123");  // Fake ID

        System.out.println("SUCCESS: " + username);  // Лог успеха
        return ResponseEntity.ok(response);
    }
}
