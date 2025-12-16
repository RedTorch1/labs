package ru.ssau.tk.maths.functions;
//Класс вычисления производной
public class DerivativeFunction {
    public static final double DEFAULT_H = 1e-6; //Шаг

    public static double derivative(MathFunction function, double x, double h)
    {
        return (function.apply(x+h)-function.apply(x-h))/(2*h); //Производная функции с указанным шагом
    }
    public static double derivative(MathFunction function, double x)
    {
        return derivative(function, x, DEFAULT_H); //В случае не указывания шага, вызывает себя с дефолтным h
    }
}
