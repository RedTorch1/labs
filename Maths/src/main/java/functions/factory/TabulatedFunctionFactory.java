// ДОПОЛНЯЕМ существующий интерфейс
package functions.factory;

import functions.MathFunction;
import functions.TabulatedFunction;

public interface TabulatedFunctionFactory {
    TabulatedFunction create(double[] xValues, double[] yValues);

    // НОВЫЙ метод для задания
    TabulatedFunction create(MathFunction source, double xFrom, double xTo, int pointsCount);
}