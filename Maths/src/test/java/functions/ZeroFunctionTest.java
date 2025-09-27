package functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZeroFunctionTest {
    void testAlwaysZero() {
        ZeroFunction f = new ZeroFunction();

        assertEquals(0, f.apply(-10), 0.0001);
        assertEquals(0, f.apply(0), 0.0001);
        assertEquals(0, f.apply(123.456), 0.0001);
    }
}