package functions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LinkedListTabulatedFunctionTest {
    public void testConstructorFromArrays() {
        double[] xs={0,1,2};
        double[] ys={0,1,4};
        LinkedListTabulatedFunction f=new LinkedListTabulatedFunction(xs,ys);
        assertEquals(3,f.getCount());
        assertEquals(1,f.getX(1),0.0001);
        assertEquals(4,f.getY(2),0.0001);
    }
    public void testConstructorFromFunction() {
        MathFunction sqr = new SqrFunction();
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(sqr, 0, 2, 3);
        assertEquals(0, f.getY(0), 0.0001);
        assertEquals(1, f.getY(1), 0.0001);
        assertEquals(4, f.getY(2), 0.0001);
    }
    public void testInterpolation() {
        double[] xs = {0, 2};
        double[] ys = {0, 2};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(xs, ys);
        //Должно дать 1 при x=1 (середина)
        assertEquals(1, f.apply(1), 0.0001);
    }
    public void testExtrapolationLeft() {
        double[] xs = {0, 2};
        double[] ys = {0, 2};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(xs, ys);
        //Левее границы (x=-1), прямая продолжение y=x
        assertEquals(-1, f.apply(-1), 0.0001);
    }
    public void testExtrapolationRight() {
        double[] xs = {0, 2};
        double[] ys = {0, 2};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(xs, ys);
        //Правее границы (x=3), прямая продолжение y=x
        assertEquals(3, f.apply(3), 0.0001);
    }
    public void testSetY() {
        double[] xs = {0, 1, 2};
        double[] ys = {0, 1, 4};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(xs, ys);
        f.setY(1, 10);
        assertEquals(10, f.getY(1), 0.0001);
    }
    public void testIndexOfX() {
        double[] xs = {0, 1, 2};
        double[] ys = {0, 1, 4};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(xs, ys);
        assertEquals(1, f.indexOfX(1));
        assertEquals(-1, f.indexOfX(5));
    }
    public void testIndexOfY() {
        double[] xs = {0, 1, 2};
        double[] ys = {0, 1, 4};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(xs, ys);
        assertEquals(1, f.indexOfY(1));
        assertEquals(-1, f.indexOfY(5));
    }
    public void testLeftRightBound() {
        double[] xs = {0, 1, 2};
        double[] ys = {0, 1, 4};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(xs, ys);
        assertEquals(0, f.leftBound(), 0.0001);
        assertEquals(2, f.rightBound(), 0.0001);
    }
    public void testApplyWithTabulatedValues() {
        double[] xs = {0, 1, 2, 3};
        double[] ys = {0, 1, 4, 9};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(xs, ys);
        //Проверяем точные значения из таблицы
        assertEquals(0, f.apply(0), 0.0001);
        assertEquals(1, f.apply(1), 0.0001);
        assertEquals(4, f.apply(2), 0.0001);
        assertEquals(9, f.apply(3), 0.0001);
    }
    public void testApplyWithInterpolation() {
        double[] xs = {0, 2, 4};
        double[] ys = {0, 4, 16};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(xs, ys);
        //Проверяем интерполяцию
        assertEquals(1, f.apply(1), 0.0001);  // между 0 и 2
        assertEquals(9, f.apply(3), 0.0001);  // между 2 и 4
    }
    public void testSinglePointFunction() {
        double[] xs = {5.0};
        double[] ys = {10.0};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(xs, ys);
        //Для одной точки всегда возвращается одно значение
        assertEquals(10, f.apply(0), 0.0001);
        assertEquals(10, f.apply(5), 0.0001);
        assertEquals(10, f.apply(10), 0.0001);
    }
    public void testCompositeWithSqr() {
        double[] xs = {0, 1, 2};
        double[] ys = {0, 1, 2};
        MathFunction f = (MathFunction) new LinkedListTabulatedFunction(xs, ys);
        SqrFunction sqr = new SqrFunction();
        CompositeFunction composite = new CompositeFunction(f, sqr);
        assertEquals(0, composite.apply(0), 0.0001);
        assertEquals(1, composite.apply(1), 0.0001);
        assertEquals(4, composite.apply(2), 0.0001);
    }
    public void testCompositeWithConstant() {
        double[] xs = {0, 1, 2};
        double[] ys = {5, 5, 5};
        MathFunction f = (MathFunction) new LinkedListTabulatedFunction(xs, ys);
        SqrFunction sqr = new SqrFunction();
        CompositeFunction composite = new CompositeFunction(f, sqr);
        assertEquals(25, composite.apply(0), 0.0001);
        assertEquals(25, composite.apply(1), 0.0001);
        assertEquals(25, composite.apply(2), 0.0001);
    }
}