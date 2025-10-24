package concurrent;

import functions.*;
import functions.factory.ArrayTabulatedFunctionFactory;
import functions.factory.LinkedListTabulatedFunctionFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class SynchronizedTabulatedFunctionTest {
    @Test
    void testGetCount() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        assertEquals(3, syncFunction.getCount());
    }
    @Test
    void testGetX() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        assertEquals(1.0, syncFunction.getX(0), 1e-10);
        assertEquals(2.0, syncFunction.getX(1), 1e-10);
        assertEquals(3.0, syncFunction.getX(2), 1e-10);
    }
    @Test
    void testGetY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        assertEquals(10.0, syncFunction.getY(0), 1e-10);
        assertEquals(20.0, syncFunction.getY(1), 1e-10);
        assertEquals(30.0, syncFunction.getY(2), 1e-10);
    }
    @Test
    void testSetY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        syncFunction.setY(1, 25.0);
        assertEquals(25.0, syncFunction.getY(1), 1e-10);
        assertEquals(25.0, baseFunction.getY(1), 1e-10); // Проверяем, что изменилась и базовая функция
    }
    @Test
    void testIndexOfX() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        assertEquals(0, syncFunction.indexOfX(1.0));
        assertEquals(1, syncFunction.indexOfX(2.0));
        assertEquals(2, syncFunction.indexOfX(3.0));
        assertEquals(-1, syncFunction.indexOfX(4.0));
    }
    @Test
    void testIndexOfY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        assertEquals(0, syncFunction.indexOfY(10.0));
        assertEquals(1, syncFunction.indexOfY(20.0));
        assertEquals(2, syncFunction.indexOfY(30.0));
        assertEquals(-1, syncFunction.indexOfY(40.0));
    }
    @Test
    void testLeftAndRightBound() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        assertEquals(1.0, syncFunction.leftBound(), 1e-10);
        assertEquals(3.0, syncFunction.rightBound(), 1e-10);
    }
    @Test
    void testApply() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        assertEquals(10.0, syncFunction.apply(1.0), 1e-10);
        assertEquals(20.0, syncFunction.apply(2.0), 1e-10);
        assertEquals(30.0, syncFunction.apply(3.0), 1e-10);
        //Тестируем интерполяцию
        double interpolated = syncFunction.apply(1.5);
        assertEquals(15.0, interpolated, 1e-10);
    }
    @Test
    void testIterator() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        int index = 0;
        for (Point point : syncFunction) {
            assertEquals(xValues[index], point.x, 1e-10);
            assertEquals(yValues[index], point.y, 1e-10);
            index++;
        }
        assertEquals(3, index);
    }
    @Test
    void testToString() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        String result = syncFunction.toString();
        assertNotNull(result);
        assertTrue(result.contains("1.0"));
        assertTrue(result.contains("10.0"));
        assertTrue(result.contains("2.0"));
        assertTrue(result.contains("20.0"));
    }
    @Test
    void testIteratorBasicFunctionality() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        Iterator<Point> iterator = syncFunction.iterator();
        //Проверяем последовательный обход
        assertTrue(iterator.hasNext(), "Iterator should have first element");
        Point point1 = iterator.next();
        assertEquals(1.0, point1.x, 1e-10);
        assertEquals(10.0, point1.y, 1e-10);
        assertTrue(iterator.hasNext(), "Iterator should have second element");
        Point point2 = iterator.next();
        assertEquals(2.0, point2.x, 1e-10);
        assertEquals(20.0, point2.y, 1e-10);
        assertTrue(iterator.hasNext(), "Iterator should have third element");
        Point point3 = iterator.next();
        assertEquals(3.0, point3.x, 1e-10);
        assertEquals(30.0, point3.y, 1e-10);
        assertTrue(iterator.hasNext(), "Iterator should have fourth element");
        Point point4 = iterator.next();
        assertEquals(4.0, point4.x, 1e-10);
        assertEquals(40.0, point4.y, 1e-10);
        //После последнего элемента
        assertFalse(iterator.hasNext(), "Iterator should not have more elements after last");
    }
    @Test
    void testIteratorWithForEachLoop() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        int index = 0;
        for (Point point : syncFunction) {
            assertEquals(xValues[index], point.x, 1e-10);
            assertEquals(yValues[index], point.y, 1e-10);
            index++;
        }
        assertEquals(3, index);
    }
    @Test
    void testIteratorNoSuchElementException() {
        //Используем минимально допустимое количество точек - 2
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        Iterator<Point> iterator = syncFunction.iterator();
        //Читаем все элементы
        Point point1 = iterator.next();
        assertEquals(1.0, point1.x, 1e-10);
        assertEquals(10.0, point1.y, 1e-10);

        Point point2 = iterator.next();
        assertEquals(2.0, point2.x, 1e-10);
        assertEquals(20.0, point2.y, 1e-10);

        // Пытаемся прочитать за пределами - должно бросить исключение
        assertFalse(iterator.hasNext(), "Should not have next after reading all elements");
        assertThrows(NoSuchElementException.class, iterator::next,
                "Should throw NoSuchElementException when no more elements");
    }
    @Test
    void testIteratorWithEmptyFunction() {
        // Используем минимально допустимое количество точек - 2
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);

        Iterator<Point> iterator = syncFunction.iterator();

        // Должны быть 2 элемента
        assertTrue(iterator.hasNext(), "Should have first element");
        iterator.next();
        assertTrue(iterator.hasNext(), "Should have second element");
        iterator.next();
        assertFalse(iterator.hasNext(), "Should not have more elements after reading all");
    }
    @Test
    void testIteratorWithSinglePointFunction() {
        //Для тестирования граничных случаев используем LinkedListTabulatedFunction
        //который может работать с 2 точками (минимум)
        double[] xValues = {1.0, 2.0}; // Минимум 2 точки
        double[] yValues = {10.0, 20.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        Iterator<Point> iterator = syncFunction.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Point point = iterator.next();
            assertNotNull(point);
            count++;
        }
        assertEquals(2, count, "Should iterate over exactly 2 points");
    }
    @Test
    void testIteratorIndependenceFromSourceModification() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        //Получаем итератор
        Iterator<Point> iterator = syncFunction.iterator();
        //Модифицируем исходные данные
        syncFunction.setY(1, 999.0);
        syncFunction.setY(2, 888.0);
        //Итератор должен работать с копией, сделанной до модификации
        Point point1 = iterator.next();
        assertEquals(1.0, point1.x, 1e-10);
        assertEquals(10.0, point1.y, 1e-10); // Старое значение, не 999
        Point point2 = iterator.next();
        assertEquals(2.0, point2.x, 1e-10);
        assertEquals(20.0, point2.y, 1e-10); // Старое значение, не 999
        Point point3 = iterator.next();
        assertEquals(3.0, point3.x, 1e-10);
        assertEquals(30.0, point3.y, 1e-10); // Старое значение, не 888
    }
    @Test
    void testMultipleIteratorsIndependent() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        //Создаем два итератора
        Iterator<Point> iterator1 = syncFunction.iterator();
        Iterator<Point> iterator2 = syncFunction.iterator();
        //Работаем с первым итератором
        Point point1FromIter1 = iterator1.next();
        Point point2FromIter1 = iterator1.next();
        //Работаем со вторым итератором независимо
        Point point1FromIter2 = iterator2.next();
        Point point2FromIter2 = iterator2.next();
        //Оба итератора должны вернуть одинаковые данные
        assertEquals(point1FromIter1.x, point1FromIter2.x, 1e-10);
        assertEquals(point1FromIter1.y, point1FromIter2.y, 1e-10);
        assertEquals(point2FromIter1.x, point2FromIter2.x, 1e-10);
        assertEquals(point2FromIter1.y, point2FromIter2.y, 1e-10);
        //Оба итератора должны завершиться
        assertFalse(iterator1.hasNext());
        assertFalse(iterator2.hasNext());
    }
    @Test
    void testIteratorWithLargeDataset() {
        //Создаем функцию с большим количеством точек
        int pointCount = 100;
        double[] xValues = new double[pointCount];
        double[] yValues = new double[pointCount];
        for (int i = 0; i < pointCount; i++) {
            xValues[i] = i;
            yValues[i] = i * 10.0;
        }
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        int count = 0;
        for (Point point : syncFunction) {
            assertEquals(count, point.x, 1e-10);
            assertEquals(count * 10.0, point.y, 1e-10);
            count++;
        }
        assertEquals(pointCount, count);
    }
}