package spring.dto;

public class CreateUserRequest {
    private String username;
    private String passwordHash;
    private String email;
    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    @Override
    public String toString() {
        return "CreateUserRequest{username='" + username + "', email='" + email + "'}";
    }
}