package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AbstractTabulatedFunctionToStringTest {
    @Test
    void testArrayTabulatedFunctionToString() {
        double[] xValues = {0.0, 0.5, 1.0};
        double[] yValues = {0.0, 0.25, 1.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        String result = function.toString();
        String expected = "ArrayTabulatedFunction size = 3\n" +
                "[0.0; 0.0]\n" +
                "[0.5; 0.25]\n" +
                "[1.0; 1.0]\n";
        assertEquals(expected, result);
    }
    @Test
    void testLinkedListTabulatedFunctionToString() {
        double[] xValues = {0.0, 0.5, 1.0};
        double[] yValues = {0.0, 0.25, 1.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);
        String result = function.toString();
        String expected = "LinkedListTabulatedFunction size = 3\n" +
                "[0.0; 0.0]\n" +
                "[0.5; 0.25]\n" +
                "[1.0; 1.0]\n";
        assertEquals(expected, result);
    }
    @Test
    void testToStringWithTwoPoints() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {2.0, 4.0};
        ArrayTabulatedFunction arrayFunction = new ArrayTabulatedFunction(xValues, yValues);
        LinkedListTabulatedFunction linkedListFunction = new LinkedListTabulatedFunction(xValues, yValues);
        String arrayResult = arrayFunction.toString();
        String linkedListResult = linkedListFunction.toString();
        String expectedArray = "ArrayTabulatedFunction size = 2\n" +
                "[1.0; 2.0]\n" +
                "[2.0; 4.0]\n";
        String expectedLinkedList = "LinkedListTabulatedFunction size = 2\n" +
                "[1.0; 2.0]\n" +
                "[2.0; 4.0]\n";
        assertEquals(expectedArray, arrayResult);
        assertEquals(expectedLinkedList, linkedListResult);
    }
    @Test
    void testToStringWithMultiplePoints() {
        double[] xValues = {-2.0, -1.0, 0.0, 1.0, 2.0};
        double[] yValues = {4.0, 1.0, 0.0, 1.0, 4.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        String result = function.toString();
        String expected = "ArrayTabulatedFunction size = 5\n" +
                "[-2.0; 4.0]\n" +
                "[-1.0; 1.0]\n" +
                "[0.0; 0.0]\n" +
                "[1.0; 1.0]\n" +
                "[2.0; 4.0]\n";
        assertEquals(expected, result);
    }
    @Test
    void testToStringWithDecimalValues() {
        double[] xValues = {0.1, 0.2, 0.3};
        double[] yValues = {1.5, 2.5, 3.5};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);
        String result = function.toString();
        String expected = "LinkedListTabulatedFunction size = 3\n" +
                "[0.1; 1.5]\n" +
                "[0.2; 2.5]\n" +
                "[0.3; 3.5]\n";
        assertEquals(expected, result);
    }
    @Test
    void testToStringWithNegativeValues() {
        double[] xValues = {-3.0, -2.0, -1.0};
        double[] yValues = {-9.0, -4.0, -1.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        String result = function.toString();
        String expected = "ArrayTabulatedFunction size = 3\n" +
                "[-3.0; -9.0]\n" +
                "[-2.0; -4.0]\n" +
                "[-1.0; -1.0]\n";
        assertEquals(expected, result);
    }
    @Test
    void testToStringFormatConsistency() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction arrayFunction = new ArrayTabulatedFunction(xValues, yValues);
        LinkedListTabulatedFunction linkedListFunction = new LinkedListTabulatedFunction(xValues, yValues);
        String arrayResult = arrayFunction.toString();
        String linkedListResult = linkedListFunction.toString();
        //Проверяем, что формат одинаковый для обоих типов функций
        assertTrue(arrayResult.startsWith("ArrayTabulatedFunction size = 3"));
        assertTrue(linkedListResult.startsWith("LinkedListTabulatedFunction size = 3"));
        //Проверяем, что точки отформатированы одинаково
        assertTrue(arrayResult.contains("[1.0; 10.0]"));
        assertTrue(linkedListResult.contains("[1.0; 10.0]"));
        assertTrue(arrayResult.contains("[2.0; 20.0]"));
        assertTrue(linkedListResult.contains("[2.0; 20.0]"));
        assertTrue(arrayResult.contains("[3.0; 30.0]"));
        assertTrue(linkedListResult.contains("[3.0; 30.0]"));
    }
    @Test
    void testToStringWithLargeValues() {
        double[] xValues = {1000.0, 2000.0};
        double[] yValues = {5000.0, 10000.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);
        String result = function.toString();
        String expected = "LinkedListTabulatedFunction size = 2\n" +
                "[1000.0; 5000.0]\n" +
                "[2000.0; 10000.0]\n";
        assertEquals(expected, result);
    }
    @Test
    void testToStringWithVeryCloseValues() {
        double[] xValues = {0.0, 0.001, 0.002};
        double[] yValues = {0.0, 0.000001, 0.000004};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        String result = function.toString();
        String expected = "ArrayTabulatedFunction size = 3\n" +
                "[0.0; 0.0]\n" +
                "[0.001; 1.0E-6]\n" +
                "[0.002; 4.0E-6]\n";
        assertEquals(expected, result);
    }
    @Test
    void testToStringWithConstructorFromMathFunction() {
        // Тестируем функции, созданные из MathFunction
        MathFunction source = new SqrFunction();
        ArrayTabulatedFunction arrayFunction = new ArrayTabulatedFunction(source, 0, 2, 3);
        LinkedListTabulatedFunction linkedListFunction = new LinkedListTabulatedFunction(source, 0, 2, 3);
        String arrayResult = arrayFunction.toString();
        String linkedListResult = linkedListFunction.toString();
        //Проверяем базовую структуру
        assertTrue(arrayResult.startsWith("ArrayTabulatedFunction size = 3"));
        assertTrue(linkedListResult.startsWith("LinkedListTabulatedFunction size = 3"));
        //Проверяем, что есть правильное количество точек
        assertEquals(3, arrayResult.split("\n").length - 1); // -1 для заголовка
        assertEquals(3, linkedListResult.split("\n").length - 1);
    }
}