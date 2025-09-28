package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayTabulatedFunctionTest {

    public void testConstructorFromArrays() {
        double[] xs = {0, 1, 2};
        double[] ys = {0, 1, 4};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(xs, ys);

        assertEquals(3, f.getCount());
        assertEquals(1, f.getX(1), 0.0001);
        assertEquals(4, f.getY(2), 0.0001);
    }

    public void testConstructorFromFunction() {
        MathFunction sqr = new SqrFunction();
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(sqr, 0, 2, 3);

        assertEquals(0, f.getY(0), 0.0001);
        assertEquals(1, f.getY(1), 0.0001);
        assertEquals(4, f.getY(2), 0.0001);
    }

    public void testInterpolation() {
        double[] xs = {0, 2};
        double[] ys = {0, 2};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(xs, ys);

        // Должно дать 1 при x=1 (середина)
        assertEquals(1, f.apply(1), 0.0001);
    }

    public void testExtrapolationLeft() {
        double[] xs = {0, 2};
        double[] ys = {0, 2};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(xs, ys);

        // Левее границы (x=-1), прямая продолжение y=x
        assertEquals(-1, f.apply(-1), 0.0001);
    }

    public void testExtrapolationRight() {
        double[] xs = {0, 2};
        double[] ys = {0, 2};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(xs, ys);

        // Правее границы (x=3), прямая продолжение y=x
        assertEquals(3, f.apply(3), 0.0001);
    }
}
