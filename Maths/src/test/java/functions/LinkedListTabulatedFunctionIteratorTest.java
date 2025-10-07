package functions;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LinkedListTabulatedFunctionIteratorTest {
    public void testIteratorWithWhileLoop() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        Iterator<Point> iterator = function.iterator();
        int pointCount = 0;

        //Тестирование с помощью while цикла
        while (iterator.hasNext()) {
            Point point = iterator.next();
            assertEquals(xValues[pointCount], point.x, 1e-10);
            assertEquals(yValues[pointCount], point.y, 1e-10);
            pointCount++;
        }

        assertEquals(4, pointCount);

        //Проверка, что после прохода итератор больше не имеет элементов
        assertFalse(iterator.hasNext());

        //Проверка исключения при попытке получить следующий элемент
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }
    public void testIteratorWithForEachLoop() {
        double[] xValues = {0.5, 1.5, 2.5};
        double[] yValues = {5.0, 15.0, 25.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        int pointCount = 0;

        //Тестирование с помощью for-each цикла
        for (Point point : function) {
            assertEquals(xValues[pointCount], point.x, 1e-10);
            assertEquals(yValues[pointCount], point.y, 1e-10);
            pointCount++;
        }

        assertEquals(3, pointCount);
    }
    public void testIteratorWithSinglePoint() {
        //Создаем функцию с помощью конструктора MathFunction с одинаковыми границами
        MathFunction source = new ConstantFunction(5.0);
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(source, 2.0, 2.0, 3);

        Iterator<Point> iterator = function.iterator();
        int pointCount = 0;

        while (iterator.hasNext()) {
            Point point = iterator.next();
            assertEquals(2.0, point.x, 1e-10);
            assertEquals(5.0, point.y, 1e-10);
            pointCount++;
        }

        assertEquals(3, pointCount);
    }
    public void testIteratorOrder() {
        double[] xValues = {1.0, 3.0, 5.0};
        double[] yValues = {2.0, 6.0, 10.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        Iterator<Point> iterator = function.iterator();

        // Проверяем порядок точек
        Point first = iterator.next();
        assertEquals(1.0, first.x, 1e-10);
        assertEquals(2.0, first.y, 1e-10);

        Point second = iterator.next();
        assertEquals(3.0, second.x, 1e-10);
        assertEquals(6.0, second.y, 1e-10);

        Point third = iterator.next();
        assertEquals(5.0, third.x, 1e-10);
        assertEquals(10.0, third.y, 1e-10);

        assertFalse(iterator.hasNext());
    }
    public void testMultipleIteratorsIndependent() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        Iterator<Point> iterator1 = function.iterator();
        Iterator<Point> iterator2 = function.iterator();

        //Оба итератора должны работать независимо
        Point point1 = iterator1.next();
        Point point2 = iterator2.next();

        assertEquals(point1.x, point2.x, 1e-10);
        assertEquals(point1.y, point2.y, 1e-10);

        //Продолжаем с первым итератором
        point1 = iterator1.next();
        //Второй итератор все еще на первом элементе
        point2 = iterator2.next();

        assertEquals(2.0, point1.x, 1e-10);
        assertEquals(1.0, point2.x, 1e-10);
    }
}