package operations;

import functions.*;
import functions.factory.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TabulatedDifferentialOperatorTest {

    @Test
    void testDeriveLinearFunctionWithArrayFactory() {
        //f(x) = 2x + 1, производная должна быть 2
        double[] xValues = {0, 1, 2, 3, 4};
        double[] yValues = {1, 3, 5, 7, 9};
        ArrayTabulatedFunction linearFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedDifferentialOperator diffOp = new TabulatedDifferentialOperator(new ArrayTabulatedFunctionFactory());
        TabulatedFunction derivative = diffOp.derive(linearFunc);
        assertNotNull(derivative);
        assertTrue(derivative instanceof ArrayTabulatedFunction);
        assertEquals(5, derivative.getCount());
        //Проверяем значения производной (должны быть близки к 2)
        double expectedDerivative = 2.0;
        double tolerance = 0.1; //допуск для численного дифференцирования
        for (int i = 0; i < derivative.getCount(); i++) {
            assertEquals(expectedDerivative, derivative.getY(i), tolerance,
                    "Производная в точке x=" + derivative.getX(i) + " должна быть близка к 2");
        }
    }
    @Test
    void testDeriveQuadraticFunctionWithLinkedListFactory() {
        // f(x) = x^2, производная должна быть 2x
        double[] xValues = {0, 1, 2, 3, 4};
        double[] yValues = {0, 1, 4, 9, 16};
        LinkedListTabulatedFunction quadraticFunc = new LinkedListTabulatedFunction(xValues, yValues);
        TabulatedDifferentialOperator diffOp = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());
        TabulatedFunction derivative = diffOp.derive(quadraticFunc);
        assertNotNull(derivative);
        assertTrue(derivative instanceof LinkedListTabulatedFunction);
        assertEquals(5, derivative.getCount());
        //Проверяем значения производной с учетом методов численного дифференцирования
        double tolerance = 0.2;
        //Первая точка: forward difference (f(x1)-f(x0))/(x1-x0) = (1-0)/(1-0) = 1.0
        assertEquals(1.0, derivative.getY(0), tolerance, "В первой точке используется forward difference");
        //Внутренние точки: central difference
        assertEquals(2.0, derivative.getY(1), tolerance, "Во второй точке (x=1) производная должна быть 2.0");
        assertEquals(4.0, derivative.getY(2), tolerance, "В третьей точке (x=2) производная должна быть 4.0");
        assertEquals(6.0, derivative.getY(3), tolerance, "В четвертой точке (x=3) производная должна быть 6.0");
        //Последняя точка: backward difference (f(x4)-f(x3))/(x4-x3) = (16-9)/(4-3) = 7.0
        assertEquals(7.0, derivative.getY(4), tolerance, "В последней точке используется backward difference");
    }
    @Test
    void testDeriveWithDifferentFactories() {
        double[] xValues = {0, 1, 2, 3};
        double[] yValues = {0, 1, 4, 9};
        //Тест с ArrayTabulatedFunctionFactory
        TabulatedDifferentialOperator arrayDiffOp = new TabulatedDifferentialOperator(new ArrayTabulatedFunctionFactory());
        ArrayTabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction arrayDerivative = arrayDiffOp.derive(arrayFunc);
        assertNotNull(arrayDerivative);
        assertTrue(arrayDerivative instanceof ArrayTabulatedFunction);
        assertEquals(4, arrayDerivative.getCount());
        //Тест с LinkedListTabulatedFunctionFactory
        TabulatedDifferentialOperator linkedListDiffOp = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());
        LinkedListTabulatedFunction linkedListFunc = new LinkedListTabulatedFunction(xValues, yValues);
        TabulatedFunction linkedListDerivative = linkedListDiffOp.derive(linkedListFunc);
        assertNotNull(linkedListDerivative);
        assertTrue(linkedListDerivative instanceof LinkedListTabulatedFunction);
        assertEquals(4, linkedListDerivative.getCount());
        //Проверяем, что значения производных одинаковы независимо от фабрики
        for (int i = 0; i < arrayDerivative.getCount(); i++) {
            assertEquals(arrayDerivative.getY(i), linkedListDerivative.getY(i), 1e-10,
                    "Значения производных должны совпадать для разных фабрик в точке x=" + arrayDerivative.getX(i));
        }
    }
    @Test
    void testFactoryGetterAndSetter() {
        TabulatedDifferentialOperator diffOp = new TabulatedDifferentialOperator();
        //Проверяем фабрику по умолчанию
        assertTrue(diffOp.getFactory() instanceof ArrayTabulatedFunctionFactory);
        //Устанавливаем новую фабрику
        TabulatedFunctionFactory newFactory = new LinkedListTabulatedFunctionFactory();
        diffOp.setFactory(newFactory);
        //Проверяем, что фабрика изменилась
        assertSame(newFactory, diffOp.getFactory());
        assertTrue(diffOp.getFactory() instanceof LinkedListTabulatedFunctionFactory);
    }
    @Test
    void testConstructorWithFactory() {
        TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        TabulatedDifferentialOperator diffOp = new TabulatedDifferentialOperator(factory);
        assertSame(factory, diffOp.getFactory());
        assertTrue(diffOp.getFactory() instanceof LinkedListTabulatedFunctionFactory);
    }

    @Test
    void testDeriveWithTwoPoints() {
        //Минимальное количество точек (2)
        double[] xValues = {0, 1};
        double[] yValues = {1, 3}; // f(x) = 2x + 1
        ArrayTabulatedFunction func = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedDifferentialOperator diffOp = new TabulatedDifferentialOperator();
        TabulatedFunction derivative = diffOp.derive(func);
        assertNotNull(derivative);
        assertEquals(2, derivative.getCount());
        //Для двух точек производная вычисляется как (y1-y0)/(x1-x0)
        double expectedDerivative = (yValues[1] - yValues[0]) / (xValues[1] - xValues[0]);
        assertEquals(expectedDerivative, derivative.getY(0), 1e-10);
        assertEquals(expectedDerivative, derivative.getY(1), 1e-10);
    }
    @Test
    void testDeriveWithConstantFunction() {
        //f(x) = 5 (константа), производная должна быть 0
        double[] xValues = {0, 1, 2, 3, 4};
        double[] yValues = {5, 5, 5, 5, 5};
        ArrayTabulatedFunction constantFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedDifferentialOperator diffOp = new TabulatedDifferentialOperator();
        TabulatedFunction derivative = diffOp.derive(constantFunc);
        assertNotNull(derivative);
        //Проверяем, что производная близка к 0
        for (int i = 0; i < derivative.getCount(); i++) {
            assertEquals(0.0, derivative.getY(i), 0.1,
                    "Производная константной функции должна быть близка к 0 в точке x=" + derivative.getX(i));
        }
    }
}