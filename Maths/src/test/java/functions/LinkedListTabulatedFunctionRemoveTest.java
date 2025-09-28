package functions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LinkedListTabulatedFunctionRemoveTest {
    public void testRemoveFromBeginning() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);
        function.remove(0);
        assertEquals(3, function.getCount());
        assertEquals(2.0, function.getX(0), 1e-10);
        assertEquals(20.0, function.getY(0), 1e-10);
        assertEquals(3.0, function.getX(1), 1e-10);
        assertEquals(4.0, function.getX(2), 1e-10);
    }
    public void testRemoveFromEnd() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);
        function.remove(3);
        assertEquals(3, function.getCount());
        assertEquals(1.0, function.getX(0), 1e-10);
        assertEquals(2.0, function.getX(1), 1e-10);
        assertEquals(3.0, function.getX(2), 1e-10);
        assertEquals(30.0, function.getY(2), 1e-10);
    }
    public void testRemoveFromMiddle() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);
        function.remove(1);
        assertEquals(3, function.getCount());
        assertEquals(1.0, function.getX(0), 1e-10);
        assertEquals(3.0, function.getX(1), 1e-10);
        assertEquals(4.0, function.getX(2), 1e-10);

        assertEquals(10.0, function.getY(0), 1e-10);
        assertEquals(30.0, function.getY(1), 1e-10);
        assertEquals(40.0, function.getY(2), 1e-10);
    }
    public void testRemoveMultipleNodes() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0, 50.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);
        function.remove(1); //удаляем второй элемент (x=2.0)
        function.remove(2); //удаляем теперь третий элемент (x=4.0)
        assertEquals(3, function.getCount());
        assertEquals(1.0, function.getX(0), 1e-10);
        assertEquals(3.0, function.getX(1), 1e-10);
        assertEquals(5.0, function.getX(2), 1e-10);
    }
    public void testRemoveHeadUpdatesCorrectly() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);
        //Чек головы
        assertEquals(1.0, function.leftBound(), 1e-10);
        function.remove(0); //отрезаем голову
        assertEquals(2, function.getCount());
        assertEquals(2.0, function.leftBound(), 1e-10); // новая голова
        assertEquals(3.0, function.rightBound(), 1e-10);
    }
}