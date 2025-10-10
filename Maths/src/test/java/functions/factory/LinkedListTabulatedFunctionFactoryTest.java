package functions.factory;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LinkedListTabulatedFunctionFactoryTest {
    @Test
    void testCreate() {
        TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {4.0, 5.0, 6.0};
        TabulatedFunction function = factory.create(xValues, yValues);
        assertNotNull(function);
        assertTrue(function instanceof LinkedListTabulatedFunction);
        //Проверяем корректность созданной функции
        assertEquals(3, function.getCount());
        assertEquals(1.0, function.getX(0));
        assertEquals(4.0, function.getY(0));
        assertEquals(3.0, function.getX(2));
        assertEquals(6.0, function.getY(2));
    }
    @Test
    void testCreateWithDifferentArrays() {
        TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        double[] xValues = {0.0, 1.5, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction function = factory.create(xValues, yValues);
        assertNotNull(function);
        assertTrue(function instanceof LinkedListTabulatedFunction);
        assertEquals(1.5, function.getX(1));
        assertEquals(20.0, function.getY(1));
    }
}