package concurrent;

import functions.*;
import functions.factory.ArrayTabulatedFunctionFactory;
import functions.factory.LinkedListTabulatedFunctionFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SynchronizedTabulatedFunctionTest {
    @Test
    void testGetCount() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        assertEquals(3, syncFunction.getCount());
    }
    @Test
    void testGetX() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        assertEquals(1.0, syncFunction.getX(0), 1e-10);
        assertEquals(2.0, syncFunction.getX(1), 1e-10);
        assertEquals(3.0, syncFunction.getX(2), 1e-10);
    }
    @Test
    void testGetY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        assertEquals(10.0, syncFunction.getY(0), 1e-10);
        assertEquals(20.0, syncFunction.getY(1), 1e-10);
        assertEquals(30.0, syncFunction.getY(2), 1e-10);
    }
    @Test
    void testSetY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        syncFunction.setY(1, 25.0);
        assertEquals(25.0, syncFunction.getY(1), 1e-10);
        assertEquals(25.0, baseFunction.getY(1), 1e-10); // Проверяем, что изменилась и базовая функция
    }
    @Test
    void testIndexOfX() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        assertEquals(0, syncFunction.indexOfX(1.0));
        assertEquals(1, syncFunction.indexOfX(2.0));
        assertEquals(2, syncFunction.indexOfX(3.0));
        assertEquals(-1, syncFunction.indexOfX(4.0));
    }
    @Test
    void testIndexOfY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        assertEquals(0, syncFunction.indexOfY(10.0));
        assertEquals(1, syncFunction.indexOfY(20.0));
        assertEquals(2, syncFunction.indexOfY(30.0));
        assertEquals(-1, syncFunction.indexOfY(40.0));
    }
    @Test
    void testLeftAndRightBound() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        assertEquals(1.0, syncFunction.leftBound(), 1e-10);
        assertEquals(3.0, syncFunction.rightBound(), 1e-10);
    }
    @Test
    void testApply() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        assertEquals(10.0, syncFunction.apply(1.0), 1e-10);
        assertEquals(20.0, syncFunction.apply(2.0), 1e-10);
        assertEquals(30.0, syncFunction.apply(3.0), 1e-10);
        //Тестируем интерполяцию
        double interpolated = syncFunction.apply(1.5);
        assertEquals(15.0, interpolated, 1e-10);
    }
    @Test
    void testIterator() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        int index = 0;
        for (Point point : syncFunction) {
            assertEquals(xValues[index], point.x, 1e-10);
            assertEquals(yValues[index], point.y, 1e-10);
            index++;
        }
        assertEquals(3, index);
    }
    @Test
    void testToString() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        String result = syncFunction.toString();
        assertNotNull(result);
        assertTrue(result.contains("1.0"));
        assertTrue(result.contains("10.0"));
        assertTrue(result.contains("2.0"));
        assertTrue(result.contains("20.0"));
    }
}