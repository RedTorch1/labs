package ru.ssau.tk.maths.dto;

import java.math.BigDecimal;

public class PointDto {
    private BigDecimal x;
    private BigDecimal y;

    public PointDto() {}
    public PointDto(BigDecimal x, BigDecimal y) { this.x = x; this.y = y; }
    public BigDecimal getX() { return x; }
    public void setX(BigDecimal x) { this.x = x; }
    public BigDecimal getY() { return y; }
    public void setY(BigDecimal y) { this.y = y; }
}
