package functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstantFunctionTest {
    public void testApplyAlwaysReturnsConstant() {
        ConstantFunction f = new ConstantFunction(42);

        // Независимо от x должно быть одно и то же значение
        assertEquals(42, f.apply(-100), 0.0001);
        assertEquals(42, f.apply(0), 0.0001);
        assertEquals(42, f.apply(123.45), 0.0001);
    }

    public void testGetValueReturnsCorrectConstant() {
        ConstantFunction f = new ConstantFunction(Math.PI);

        // Метод getValue() должен возвращать то же самое,
        // что и apply(x)
        assertEquals(Math.PI, f.getValue(), 0.0001);
        assertEquals(Math.PI, f.apply(999), 0.0001);
    }
}