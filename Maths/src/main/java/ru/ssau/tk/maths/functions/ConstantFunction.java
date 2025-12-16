package ru.ssau.tk.maths.functions;

public class ConstantFunction implements MathFunction {
    private final double constant;

    public ConstantFunction(double constant) {
        this.constant = constant;
    }

    @Override
    public double apply(double x) {
        return constant;
    }

    @Override
    public String toString() {
        return "Постоянная функция (" + constant + ")";
    }
}