package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayTabulatedFunctionRemoveTest {

    public void testRemoveMiddle() {
        double[] xs = {0, 1, 2};
        double[] ys = {0, 1, 4};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(xs, ys);

        f.remove(1); // удаляем точку (1,1)

        assertEquals(2, f.getCount());
        assertEquals(0, f.getX(0), 0.0001);
        assertEquals(2, f.getX(1), 0.0001);
    }

    public void testRemoveFirst() {
        double[] xs = {0, 1, 2};
        double[] ys = {0, 1, 4};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(xs, ys);

        f.remove(0);

        assertEquals(2, f.getCount());
        assertEquals(1, f.getX(0), 0.0001);
    }

    public void testRemoveLast() {
        double[] xs = {0, 1, 2};
        double[] ys = {0, 1, 4};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(xs, ys);

        f.remove(2);

        assertEquals(2, f.getCount());
        assertEquals(1, f.getX(1), 0.0001);
    }

    public void testRemoveOnlyOnePointThrows() {
        double[] xs = {0, 1};
        double[] ys = {0, 1};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(xs, ys);

        f.remove(0);
        assertThrows(IllegalStateException.class, () -> f.remove(0));
    }
}
