package ru.ssau.tk.maths.web;

import java.util.List;

public class FunctionData {
    private String name;
    private String expression;
    private List<PointData> points;
    private Integer userId;

    // Геттеры/сеттеры
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getExpression() { return expression; }
    public void setExpression(String expression) { this.expression = expression; }

    public List<PointData> getPoints() { return points; }
    public void setPoints(List<PointData> points) { this.points = points; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}
