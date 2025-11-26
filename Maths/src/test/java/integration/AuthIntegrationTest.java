package integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Base64;
import java.time.LocalDateTime;

public class AuthIntegrationTest {

    private static final String BASE_URL = "http://localhost:8080/lab6";
    private HttpClient client;

    @BeforeEach
    void setUp() {
        client = HttpClient.newHttpClient();
    }

    private String createBasicAuthHeader(String username, String password) {
        String credentials = username + ":" + password;
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes());
        return "Basic " + encoded;
    }

    @Test
    void testUnauthorizedAccess() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/users"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(401, response.statusCode(), "Should return 401 Unauthorized without auth");
    }

    @Test
    void testViewerRoleCanRead() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/users"))
                .header("Authorization", createBasicAuthHeader("viewer", "viewer123"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "VIEWER should be able to read users");
        assertTrue(response.body().contains("status"), "Response should contain status field");
    }

    @Test
    void testUserRegistration() throws Exception {
        // Используем уникальное имя пользователя для каждого теста
        String timestamp = LocalDateTime.now().toString().replaceAll("[^0-9]", "");
        String uniqueUsername = "testuser_" + timestamp;

        String jsonBody = String.format(
                "{\"username\": \"%s\", \"password\": \"testpass123\", \"role\": \"USER\"}",
                uniqueUsername
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/auth/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Registration response: " + response.statusCode() + " - " + response.body());

        assertEquals(200, response.statusCode(), "User registration should work. Response: " + response.body());
        assertTrue(response.body().contains("registered successfully") || response.body().contains("success"),
                "Response should indicate success. Actual: " + response.body());
    }
}