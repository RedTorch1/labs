package ru.ssau.tk.maths.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "point", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"function_id", "x_value"})
})
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // связь на функцию
    @ManyToOne
    @JoinColumn(name = "function_id", nullable = false)
    private Function function;

    // используем BigDecimal для точности хранения
    @Column(name = "x_value", nullable = false, precision = 20, scale = 10)
    private BigDecimal xvalue;

    @Column(name = "y_value", nullable = false, precision = 20, scale = 10)
    private BigDecimal yvalue;

    public Point() {}

    public Point(Function function, BigDecimal xValue, BigDecimal yValue) {
        this.function = function;
        this.xvalue = xValue;
        this.yvalue = yValue;
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Function getFunction() { return function; }
    public void setFunction(Function function) { this.function = function; }

    public BigDecimal getXValue() { return xvalue; }
    public void setXValue(BigDecimal xValue) { this.xvalue = xValue; }

    public BigDecimal getYValue() { return yvalue; }
    public void setYValue(BigDecimal yValue) { this.yvalue = yValue; }
}
