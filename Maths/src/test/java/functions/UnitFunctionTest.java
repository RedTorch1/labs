package functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitFunctionTest {
    void testAlwaysOne() {
        UnitFunction f = new UnitFunction();

        assertEquals(1, f.apply(-999), 0.0001);
        assertEquals(1, f.apply(0), 0.0001);
        assertEquals(1, f.apply(999), 0.0001);
    }
}