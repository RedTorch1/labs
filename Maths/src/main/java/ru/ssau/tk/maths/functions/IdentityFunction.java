package ru.ssau.tk.maths.functions;

public class IdentityFunction implements MathFunction {
    @Override
    public double apply(double x) {
        return x;
    }

    @Override
    public String toString() {
        return "Тождественная функция";
    }
}