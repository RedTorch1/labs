package model;

public class FunctionEntity {
    private long id;
    private long userId;
    private String name;
    private String expression;

    public FunctionEntity() {}

    public FunctionEntity(long id, long userId, String name, String expression) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.expression = expression;
    }

    // getters/setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getExpression() { return expression; }
    public void setExpression(String expression) { this.expression = expression; }

    @Override
    public String toString() {
        return "FunctionEntity{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
