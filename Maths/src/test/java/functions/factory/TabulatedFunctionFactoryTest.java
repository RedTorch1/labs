package functions.factory;
import functions.TabulatedFunction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TabulatedFunctionFactoryTest {
    @Test
    void testFactoryPolymorphism() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        //Тестируем Array фабрику
        TabulatedFunctionFactory arrayFactory = new ArrayTabulatedFunctionFactory();
        TabulatedFunction arrayFunction = arrayFactory.create(xValues, yValues);
        assertEquals("functions.ArrayTabulatedFunction", arrayFunction.getClass().getName());
        //Тестируем LinkedList фабрику
        TabulatedFunctionFactory linkedListFactory = new LinkedListTabulatedFunctionFactory();
        TabulatedFunction linkedListFunction = linkedListFactory.create(xValues, yValues);
        assertEquals("functions.LinkedListTabulatedFunction", linkedListFunction.getClass().getName());
        //Обе функции должны работать одинаково с точки зрения интерфейса
        assertEquals(3, arrayFunction.getCount());
        assertEquals(3, linkedListFunction.getCount());
        assertEquals(1.0, arrayFunction.getX(0));
        assertEquals(1.0, linkedListFunction.getX(0));
        assertEquals(9.0, arrayFunction.getY(2));
        assertEquals(9.0, linkedListFunction.getY(2));
    }
}