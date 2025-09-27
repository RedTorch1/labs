package functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZeroFunctionTest {
    void testAlwaysZero() {
        ZeroFunction f = new ZeroFunction();

        assertEquals(0.0, f.apply(-10), 1e-9);
        assertEquals(0.0, f.apply(0), 1e-9);
        assertEquals(0.0, f.apply(123.456), 1e-9);
    }
}