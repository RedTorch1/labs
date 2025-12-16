package ru.ssau.tk.maths.functions;

public class SqrFunction implements MathFunction {
    @Override
    public double apply(double x) {
        return x * x;
    }

    @Override
    public String toString() {
        return "Квадратичная функция";
    }
}