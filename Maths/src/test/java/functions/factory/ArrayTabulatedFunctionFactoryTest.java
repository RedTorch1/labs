package functions.factory;

import functions.ArrayTabulatedFunction;
import functions.TabulatedFunction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArrayTabulatedFunctionFactoryTest {
    @Test
    void testCreate() {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {4.0, 5.0, 6.0};
        TabulatedFunction function = factory.create(xValues, yValues);
        assertNotNull(function);
        assertTrue(function instanceof ArrayTabulatedFunction);
        //Проверяем корректность созданной функции
        assertEquals(3, function.getCount());
        assertEquals(1.0, function.getX(0));
        assertEquals(4.0, function.getY(0));
        assertEquals(3.0, function.getX(2));
        assertEquals(6.0, function.getY(2));
    }
    @Test
    void testCreateWithDifferentArrays() {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        double[] xValues = {0.0, 1.5, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction function = factory.create(xValues, yValues);
        assertNotNull(function);
        assertTrue(function instanceof ArrayTabulatedFunction);
        assertEquals(1.5, function.getX(1));
        assertEquals(20.0, function.getY(1));
    }
}