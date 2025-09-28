package functions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArrayTabulatedFunctionInsertTest {
    public void testInsertAtBeginning() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        function.insert(0.5, 5.0);
        assertEquals(4, function.getCount());
        assertEquals(0.5, function.getX(0), 1e-10);
        assertEquals(5.0, function.getY(0), 1e-10);
        assertEquals(1.0, function.getX(1), 1e-10);
        assertEquals(10.0, function.getY(1), 1e-10);
    }
    public void testInsertAtEnd() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        function.insert(4.0, 40.0);
        assertEquals(4, function.getCount());
        assertEquals(3.0, function.getX(2), 1e-10);
        assertEquals(30.0, function.getY(2), 1e-10);
        assertEquals(4.0, function.getX(3), 1e-10);
        assertEquals(40.0, function.getY(3), 1e-10);
    }
    public void testInsertInMiddle() {
        double[] xValues = {1.0, 3.0, 5.0};
        double[] yValues = {10.0, 30.0, 50.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        function.insert(2.0, 20.0);
        assertEquals(4, function.getCount());
        assertEquals(1.0, function.getX(0), 1e-10);
        assertEquals(2.0, function.getX(1), 1e-10);
        assertEquals(3.0, function.getX(2), 1e-10);
        assertEquals(5.0, function.getX(3), 1e-10);
        assertEquals(10.0, function.getY(0), 1e-10);
        assertEquals(20.0, function.getY(1), 1e-10);
        assertEquals(30.0, function.getY(2), 1e-10);
        assertEquals(50.0, function.getY(3), 1e-10);
    }
    public void testInsertExistingX() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        function.insert(2.0, 25.0);
        //Количество точек не должно измениться
        assertEquals(3, function.getCount());
        //Значение y должно обновиться
        assertEquals(25.0, function.getY(1), 1e-10);
        //Остальные значения должны остаться прежними
        assertEquals(10.0, function.getY(0), 1e-10);
        assertEquals(30.0, function.getY(2), 1e-10);
    }
}