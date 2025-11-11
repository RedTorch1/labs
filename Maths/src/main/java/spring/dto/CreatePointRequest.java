package spring.dto;

public class CreatePointRequest {
    private Long functionId;
    private Double xValue;
    private Double yValue;

    // Getters and Setters
    public Long getFunctionId() { return functionId; }
    public void setFunctionId(Long functionId) { this.functionId = functionId; }

    public Double getXValue() { return xValue; }
    public void setXValue(Double xValue) { this.xValue = xValue; }

    public Double getYValue() { return yValue; }
    public void setYValue(Double yValue) { this.yValue = yValue; }

    @Override
    public String toString() {
        return "CreatePointRequest{functionId=" + functionId + ", x=" + xValue + ", y=" + yValue + "}";
    }
}