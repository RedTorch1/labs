package functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BSplineFunctionTest {

    void testSimpleSpline() {
        double[] controlPoints = {0.0, 2.0, 3.0, 0.0};
        double[] knots = {0, 0, 0, 1, 2, 3, 3, 3};
        int degree = 3;

        BSplineFunction spline = new BSplineFunction(controlPoints, knots, degree);

        double y0 = spline.apply(0.0);
        double yMid = spline.apply(1.5);
        double yEnd = spline.apply(3.0 - 1e-9);

        // В начале кривая уходит в первую точку
        assertEquals(0.0, y0, 1e-9);

        // В середине должно быть что-то положительное
        assertTrue(yMid > 0);

        // В конце снова тянется к последней контрольной точке
        assertTrue(Math.abs(yEnd) < 1e-6);
    }

    void testNonNegativeValues() {
        double[] controlPoints = {0.0, 2.0, 3.0, 0.0};
        double[] knots = {0, 0, 0, 1, 2, 3, 3, 3};
        int degree = 3;

        BSplineFunction spline = new BSplineFunction(controlPoints, knots, degree);

        for (double t = 0.0; t <= 3.0; t += 0.1) {
            double y = spline.apply(t);
            // Значения не должны выходить слишком далеко за диапазон [0,3]
            assertTrue(y >= -1e-6 && y <= 3.0 + 1e-6);
        }
    }
}
