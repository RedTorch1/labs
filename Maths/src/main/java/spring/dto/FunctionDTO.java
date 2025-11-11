package spring.dto;

import java.time.LocalDateTime;

public class FunctionDTO {
    private Long id;
    private String name;
    private String expression;
    private Long userId;
    private LocalDateTime createdAt;
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getExpression() { return expression; }
    public void setExpression(String expression) { this.expression = expression; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    @Override
    public String toString() {
        return "FunctionDTO{id=" + id + ", name='" + name + "', expression='" + expression + "'}";
    }
}