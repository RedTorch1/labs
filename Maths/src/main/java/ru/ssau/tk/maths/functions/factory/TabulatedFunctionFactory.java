// ДОПОЛНЯЕМ существующий интерфейс
package ru.ssau.tk.maths.functions.factory;

import ru.ssau.tk.maths.functions.MathFunction;
import ru.ssau.tk.maths.functions.TabulatedFunction;

public interface TabulatedFunctionFactory {
    TabulatedFunction create(double[] xValues, double[] yValues);

    // НОВЫЙ метод для задания
    TabulatedFunction create(MathFunction source, double xFrom, double xTo, int pointsCount);
}