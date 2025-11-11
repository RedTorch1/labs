package spring.dto;

public class CreateFunctionRequest {
    private String name;
    private String expression;
    private Long userId;
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getExpression() { return expression; }
    public void setExpression(String expression) { this.expression = expression; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    @Override
    public String toString() {
        return "CreateFunctionRequest{name='" + name + "', expression='" + expression + "'}";
    }
}