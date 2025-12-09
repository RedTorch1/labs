// src/main/java/ui/services/DifferentialOperator.java
package ui.services;

import functions.TabulatedFunction;
import functions.factory.TabulatedFunctionFactory;

public class DifferentialOperator {
    private final TabulatedFunctionFactory factory;

    public DifferentialOperator(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public TabulatedFunction differentiate(TabulatedFunction function) {
        int n = function.getCount();

        if (n < 2) {
            throw new IllegalArgumentException("Для дифференцирования нужно как минимум 2 точки");
        }

        double[] xValues = new double[n];
        double[] yValues = new double[n];

        // Первая точка (левая разность)
        xValues[0] = function.getX(0);
        yValues[0] = (function.getY(1) - function.getY(0)) / (function.getX(1) - function.getX(0));

        // Средние точки (центральная разность)
        for (int i = 1; i < n - 1; i++) {
            xValues[i] = function.getX(i);
            yValues[i] = (function.getY(i + 1) - function.getY(i - 1)) /
                    (function.getX(i + 1) - function.getX(i - 1));
        }

        // Последняя точка (правая разность)
        xValues[n - 1] = function.getX(n - 1);
        yValues[n - 1] = (function.getY(n - 1) - function.getY(n - 2)) /
                (function.getX(n - 1) - function.getX(n - 2));

        return factory.create(xValues, yValues);
    }
}