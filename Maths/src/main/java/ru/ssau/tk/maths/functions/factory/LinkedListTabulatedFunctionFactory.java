package ru.ssau.tk.maths.functions.factory;

import ru.ssau.tk.maths.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.maths.functions.MathFunction;
import ru.ssau.tk.maths.functions.TabulatedFunction;

public class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {

    @Override
    public TabulatedFunction create(double[] xValues, double[] yValues) {
        return new LinkedListTabulatedFunction(xValues, yValues);
    }

    @Override
    public TabulatedFunction create(MathFunction source, double xFrom, double xTo, int pointsCount) {
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        if (xFrom >= xTo) {
            throw new IllegalArgumentException("Начало интервала должно быть меньше конца");
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