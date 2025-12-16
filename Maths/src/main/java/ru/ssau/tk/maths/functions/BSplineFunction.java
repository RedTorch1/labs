package ru.ssau.tk.maths.functions;

// Класс функции B-сплайна (по одной координате)
public class BSplineFunction implements MathFunction {

    private final double[] controlPoints; // контрольные точки (по Y)
    private final double[] knots;         // узловой вектор
    private final int degree;             // степень сплайна

    public BSplineFunction(double[] controlPoints, double[] knots, int degree) {
        this.controlPoints = controlPoints;
        this.knots = knots;
        this.degree = degree;
    }

    public double apply(double x) {
        double result = 0.0;
        int n = controlPoints.length;

        for (int i = 0; i < n; i++) {
            double coeff = BasisFunction.N(i, degree + 1, x, knots); // степень + 1
            result += coeff * controlPoints[i];
        }
        return result;
    }
}
