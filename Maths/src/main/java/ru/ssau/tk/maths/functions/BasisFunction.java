package ru.ssau.tk.maths.functions;

// Класс для вычисления базисных функций B-сплайна
public class BasisFunction {

    // Рекурсивная формула Кокса–де Бура
    public static double N(int i, int k, double t, double[] knots) {
        if (k == 1) {
            if (knots[i] <= t && t < knots[i + 1]) return 1.0;
            else return 0.0;
        } else {
            double left = 0.0;
            double right = 0.0;

            double denom1 = knots[i + k - 1] - knots[i];
            if (denom1 != 0) {
                left = (t - knots[i]) / denom1 * N(i, k - 1, t, knots);
            }

            double denom2 = knots[i + k] - knots[i + 1];
            if (denom2 != 0) {
                right = (knots[i + k] - t) / denom2 * N(i + 1, k - 1, t, knots);
            }

            return left + right;
        }
    }
}
