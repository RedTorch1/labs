package functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstantFunctionTest {
    public void testApplyAlwaysReturnsConstant() {
        ConstantFunction f = new ConstantFunction(42.0);

        // Независимо от x должно быть одно и то же значение
        assertEquals(42.0, f.apply(-100.0), 1e-9);
        assertEquals(42.0, f.apply(0.0), 1e-9);
        assertEquals(42.0, f.apply(123.45), 1e-9);
    }

    public void testGetValueReturnsCorrectConstant() {
        ConstantFunction f = new ConstantFunction(Math.PI);

        // Метод getValue() должен возвращать то же самое,
        // что и apply(x)
        assertEquals(Math.PI, f.getValue(), 1e-9);
        assertEquals(Math.PI, f.apply(999), 1e-9);
    }
}