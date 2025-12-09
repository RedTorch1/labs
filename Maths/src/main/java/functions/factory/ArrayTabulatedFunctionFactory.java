// НОВЫЙ класс фабрики
package functions.factory;

import functions.MathFunction;
import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;

public class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {

    @Override
    public TabulatedFunction create(double[] xValues, double[] yValues) {
        return new ArrayTabulatedFunction(xValues, yValues);
    }

    @Override // Реализуем новый метод
    public TabulatedFunction create(MathFunction source, double xFrom, double xTo, int pointsCount) {
        if (pointsCount < 2) {
            throw new IllegalArgumentException("pointsCount must be at least 2");
        }
        if (xFrom >= xTo) {
            throw new IllegalArgumentException("xFrom must be less than xTo");
        }

        double[] xValues = new double[pointsCount];
        double[] yValues = new double[pointsCount];

        double step = (xTo - xFrom) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            xValues[i] = xFrom + i * step;
            yValues[i] = source.apply(xValues[i]);
        }

        return create(xValues, yValues);
    }
}