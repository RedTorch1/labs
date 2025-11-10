package model;

public class PointEntity {
    private long id;
    private long functionId;
    private double xValue;
    private double yValue;

    public PointEntity() {}

    public PointEntity(long id, long functionId, double xValue, double yValue) {
        this.id = id;
        this.functionId = functionId;
        this.xValue = xValue;
        this.yValue = yValue;
    }

    // getters/setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getFunctionId() { return functionId; }
    public void setFunctionId(long functionId) { this.functionId = functionId; }

    public double getxValue() { return xValue; }
    public void setxValue(double xValue) { this.xValue = xValue; }

    public double getyValue() { return yValue; }
    public void setyValue(double yValue) { this.yValue = yValue; }

    @Override
    public String toString() {
        return "PointEntity{" + "id=" + id + ", x=" + xValue + ", y=" + yValue + '}';
    }
}
