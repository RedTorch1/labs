package spring.dto;

public class PointDTO {
    private Long id;
    private Long functionId;
    private Double xValue;
    private Double yValue;
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getFunctionId() { return functionId; }
    public void setFunctionId(Long functionId) { this.functionId = functionId; }
    public Double getXValue() { return xValue; }
    public void setXValue(Double xValue) { this.xValue = xValue; }
    public Double getYValue() { return yValue; }
    public void setYValue(Double yValue) { this.yValue = yValue; }
    @Override
    public String toString() {
        return "PointDTO{id=" + id + ", functionId=" + functionId + ", x=" + xValue + ", y=" + yValue + "}";
    }
}