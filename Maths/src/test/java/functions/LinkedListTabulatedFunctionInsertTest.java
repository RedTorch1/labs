package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LinkedListTabulatedFunctionInsertTest {

    public void testInsertIntoEmptyList() {
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(new double[]{0,1}, new double[]{0,1});
        f.remove(0); // допустим, удалили, список пуст
        f.remove(0);

        f.insert(5.0, 10.0);
        assertEquals(1, f.getCount());
        assertEquals(5.0, f.getX(0), 0.0001);
        assertEquals(10.0, f.getY(0), 0.0001);
    }

    public void testInsertReplaceValue() {
        double[] xs = {0, 1, 2};
        double[] ys = {0, 1, 4};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(xs, ys);

        f.insert(1, 99);
        assertEquals(99, f.getY(1), 0.0001);
        assertEquals(3, f.getCount());
    }

    public void testInsertInMiddle() {
        double[] xs = {0, 2};
        double[] ys = {0, 4};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(xs, ys);

        f.insert(1, 1);
        assertEquals(3, f.getCount());
        assertEquals(1.0, f.getX(1), 0.0001);
        assertEquals(1.0, f.getY(1), 0.0001);
    }

    public void testInsertAtBeginning() {
        double[] xs = {2, 3};
        double[] ys = {4, 9};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(xs, ys);

        f.insert(1, 1);
        assertEquals(3, f.getCount());
        assertEquals(1.0, f.getX(0), 0.0001);
    }

    public void testInsertAtEnd() {
        double[] xs = {0, 1};
        double[] ys = {0, 1};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(xs, ys);

        f.insert(5, 25);
        assertEquals(3, f.getCount());
        assertEquals(5.0, f.getX(2), 0.0001);
        assertEquals(25.0, f.getY(2), 0.0001);
    }
}
