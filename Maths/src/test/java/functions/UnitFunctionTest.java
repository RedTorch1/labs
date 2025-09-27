package functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitFunctionTest {
    void testAlwaysOne() {
        UnitFunction f = new UnitFunction();

        assertEquals(1.0, f.apply(-999), 1e-9);
        assertEquals(1.0, f.apply(0), 1e-9);
        assertEquals(1.0, f.apply(999), 1e-9);
    }
}