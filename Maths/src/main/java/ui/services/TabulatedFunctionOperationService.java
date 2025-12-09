// src/main/java/ui/services/TabulatedFunctionOperationService.java
package ui.services;

import functions.TabulatedFunction;
import functions.factory.TabulatedFunctionFactory;

public class TabulatedFunctionOperationService {
    private final TabulatedFunctionFactory factory;

    public TabulatedFunctionOperationService(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public TabulatedFunction add(TabulatedFunction f1, TabulatedFunction f2) {
        if (f1.getCount() != f2.getCount()) {
            throw new IllegalArgumentException("Функции должны иметь одинаковое количество точек");
        }

        double[] xValues = new double[f1.getCount()];
        double[] yValues = new double[f1.getCount()];

        for (int i = 0; i < f1.getCount(); i++) {
            if (Math.abs(f1.getX(i) - f2.getX(i)) > 1e-9) {
                throw new IllegalArgumentException("X значения функций должны совпадать");
            }

            xValues[i] = f1.getX(i);
            yValues[i] = f1.getY(i) + f2.getY(i);
        }

        return factory.create(xValues, yValues);
    }

    public TabulatedFunction subtract(TabulatedFunction f1, TabulatedFunction f2) {
        if (f1.getCount() != f2.getCount()) {
            throw new IllegalArgumentException("Функции должны иметь одинаковое количество точек");
        }

        double[] xValues = new double[f1.getCount()];
        double[] yValues = new double[f1.getCount()];

        for (int i = 0; i < f1.getCount(); i++) {
            if (Math.abs(f1.getX(i) - f2.getX(i)) > 1e-9) {
                throw new IllegalArgumentException("X значения функций должны совпадать");
            }

            xValues[i] = f1.getX(i);
            yValues[i] = f1.getY(i) - f2.getY(i);
        }

        return factory.create(xValues, yValues);
    }

    public TabulatedFunction multiply(TabulatedFunction f1, TabulatedFunction f2) {
        if (f1.getCount() != f2.getCount()) {
            throw new IllegalArgumentException("Функции должны иметь одинаковое количество точек");
        }

        double[] xValues = new double[f1.getCount()];
        double[] yValues = new double[f1.getCount()];

        for (int i = 0; i < f1.getCount(); i++) {
            if (Math.abs(f1.getX(i) - f2.getX(i)) > 1e-9) {
                throw new IllegalArgumentException("X значения функций должны совпадать");
            }

            xValues[i] = f1.getX(i);
            yValues[i] = f1.getY(i) * f2.getY(i);
        }

        return factory.create(xValues, yValues);
    }

    public TabulatedFunction divide(TabulatedFunction f1, TabulatedFunction f2) {
        if (f1.getCount() != f2.getCount()) {
            throw new IllegalArgumentException("Функции должны иметь одинаковое количество точек");
        }

        double[] xValues = new double[f1.getCount()];
        double[] yValues = new double[f1.getCount()];

        for (int i = 0; i < f1.getCount(); i++) {
            if (Math.abs(f1.getX(i) - f2.getX(i)) > 1e-9) {
                throw new IllegalArgumentException("X значения функций должны совпадать");
            }

            if (Math.abs(f2.getY(i)) < 1e-9) {
                throw new IllegalArgumentException("Деление на ноль в точке x = " + f2.getX(i));
            }

            xValues[i] = f1.getX(i);
            yValues[i] = f1.getY(i) / f2.getY(i);
        }

        return factory.create(xValues, yValues);
    }
}