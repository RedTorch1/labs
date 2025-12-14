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
    @Column(name = "x_value")
    private BigDecimal xvalue;

    @Column(name = "y_value")
    private BigDecimal yvalue;

    public Point() {}

    public Point(Function function, BigDecimal xvalue, BigDecimal yvalue) {
        this.function = function;
        this.xvalue = xvalue;
        this.yvalue = yvalue;
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Function getFunction() { return function; }
    public void setFunction(Function function) { this.function = function; }

    public BigDecimal getxvalue() { return xvalue; }
    public void setxvalue(BigDecimal xvalue) { this.xvalue = xvalue; }

    public BigDecimal getyvalue() { return yvalue; }
    public void setyvalue(BigDecimal yvalue) { this.yvalue = yvalue; }
}
