package operations;
import functions.*;
import functions.factory.*;
import exceptions.InconsistentFunctionsException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TabulatedFunctionOperationServiceTest {
    @Test
    void testMultiply() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {2.0, 3.0, 4.0};
        double[] yValues2 = {5.0, 6.0, 7.0};
        ArrayTabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);
        ArrayTabulatedFunction func2 = new ArrayTabulatedFunction(xValues1, yValues2);
        TabulatedFunction result = service.multiply(func1, func2);
        assertNotNull(result);
        assertEquals(3, result.getCount());
        assertEquals(10.0, result.getY(0)); //2.0*5.0 = 10.0
        assertEquals(18.0, result.getY(1)); //3.0*6.0 = 18.0
        assertEquals(28.0, result.getY(2)); //4.0*7.0 = 28.0
    }
    @Test
    void testMultiplyWithLinkedListFactory() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(new LinkedListTabulatedFunctionFactory());
        double[] xValues = {0.0, 1.0, 2.0};
        double[] yValues1 = {1.0, 2.0, 3.0};
        double[] yValues2 = {4.0, 5.0, 6.0};
        LinkedListTabulatedFunction func1 = new LinkedListTabulatedFunction(xValues, yValues1);
        LinkedListTabulatedFunction func2 = new LinkedListTabulatedFunction(xValues, yValues2);
        TabulatedFunction result = service.multiply(func1, func2);
        assertNotNull(result);
        assertTrue(result instanceof LinkedListTabulatedFunction);
        assertEquals(3, result.getCount());
        assertEquals(4.0, result.getY(0));  //1.0*4.0 = 4.0
        assertEquals(10.0, result.getY(1)); //2.0*5.0 = 10.0
        assertEquals(18.0, result.getY(2)); //3.0*6.0 = 18.0
    }
    @Test
    void testDivide() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues1 = {6.0, 12.0, 18.0};
        double[] yValues2 = {2.0, 3.0, 6.0};
        ArrayTabulatedFunction func1 = new ArrayTabulatedFunction(xValues, yValues1);
        ArrayTabulatedFunction func2 = new ArrayTabulatedFunction(xValues, yValues2);
        TabulatedFunction result = service.divide(func1, func2);
        assertNotNull(result);
        assertEquals(3, result.getCount());
        assertEquals(3.0, result.getY(0)); //6.0/2.0 = 3.0
        assertEquals(4.0, result.getY(1)); //12.0/3.0 = 4.0
        assertEquals(3.0, result.getY(2)); //18.0/6.0 = 3.0
    }

    @Test
    void testDivideByZeroThrowsException() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues1 = {6.0, 12.0, 18.0};
        double[] yValues2 = {2.0, 0.0, 6.0}; //Деление на ноль во второй точке
        ArrayTabulatedFunction func1 = new ArrayTabulatedFunction(xValues, yValues1);
        ArrayTabulatedFunction func2 = new ArrayTabulatedFunction(xValues, yValues2);
        assertThrows(ArithmeticException.class, () -> {service.divide(func1, func2);}, "Должно быть выброшено исключение при делении на ноль");
    }
    @Test
    void testMultiplyWithDifferentFunctionsTypes() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValues = {0.0, 1.0, 2.0};
        double[] yValues1 = {2.0, 3.0, 4.0};
        double[] yValues2 = {1.0, 2.0, 3.0};
        ArrayTabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues1);
        LinkedListTabulatedFunction linkedListFunc = new LinkedListTabulatedFunction(xValues, yValues2);
        TabulatedFunction result = service.multiply(arrayFunc, linkedListFunc);
        assertNotNull(result);
        assertEquals(3, result.getCount());
        assertEquals(2.0, result.getY(0)); //2.0*1.0 = 2.0
        assertEquals(6.0, result.getY(1)); //3.0*2.0 = 6.0
        assertEquals(12.0, result.getY(2)); //4.0*3.0 = 12.0
    }
    @Test
    void testDivideWithDifferentFactories() {
        //Тестируем деление с Array фабрикой
        TabulatedFunctionOperationService arrayService = new TabulatedFunctionOperationService(new ArrayTabulatedFunctionFactory());
        //Тестируем деление с LinkedList фабрикой
        TabulatedFunctionOperationService linkedListService = new TabulatedFunctionOperationService(new LinkedListTabulatedFunctionFactory());
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues1 = {10.0, 20.0, 30.0};
        double[] yValues2 = {2.0, 4.0, 5.0};
        ArrayTabulatedFunction func1 = new ArrayTabulatedFunction(xValues, yValues1);
        ArrayTabulatedFunction func2 = new ArrayTabulatedFunction(xValues, yValues2);
        TabulatedFunction arrayResult = arrayService.divide(func1, func2);
        TabulatedFunction linkedListResult = linkedListService.divide(func1, func2);
        //Проверяем, что результаты одинаковы независимо от фабрики
        assertEquals(3, arrayResult.getCount());
        assertEquals(3, linkedListResult.getCount());
        for (int i = 0; i < arrayResult.getCount(); i++) {
            assertEquals(arrayResult.getY(i), linkedListResult.getY(i), 1e-10, "Результаты деления должны совпадать для разных фабрик");
        }
        //Проверяем правильность вычислений
        assertEquals(5.0, arrayResult.getY(0)); //10.0/2.0 = 5.0
        assertEquals(5.0, arrayResult.getY(1)); //20.0/4.0 = 5.0
        assertEquals(6.0, arrayResult.getY(2)); //30.0/5.0 = 6.0
    }
    @Test
    void testOperationsWithInconsistentFunctions() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] xValues2 = {1.0, 2.5, 3.0}; //Разные x значения
        double[] yValues = {1.0, 2.0, 3.0};
        ArrayTabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues);
        ArrayTabulatedFunction func2 = new ArrayTabulatedFunction(xValues2, yValues);
        assertThrows(InconsistentFunctionsException.class, () -> {service.multiply(func1, func2);}, "Должно быть выброшено исключение при разных x значениях");
        assertThrows(InconsistentFunctionsException.class, () -> {service.divide(func1, func2);}, "Должно быть выброшено исключение при разных x значениях");
    }
    @Test
    void testOperationsWithDifferentCounts() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] xValues2 = {1.0, 2.0}; //Разное количество точек
        double[] yValues1 = {1.0, 2.0, 3.0};
        double[] yValues2 = {1.0, 2.0};
        ArrayTabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);
        ArrayTabulatedFunction func2 = new ArrayTabulatedFunction(xValues2, yValues2);
        assertThrows(InconsistentFunctionsException.class, () -> {
            service.multiply(func1, func2);
        }, "Должно быть выброшено исключение при разном количестве точек");
    }
    @Test
    void testAllOperationsTogether() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues1 = {4.0, 5.0, 6.0};
        double[] yValues2 = {2.0, 3.0, 4.0};
        ArrayTabulatedFunction func1 = new ArrayTabulatedFunction(xValues, yValues1);
        ArrayTabulatedFunction func2 = new ArrayTabulatedFunction(xValues, yValues2);
        //Тестируем все операции
        TabulatedFunction addResult = service.add(func1, func2);
        TabulatedFunction subtractResult = service.subtract(func1, func2);
        TabulatedFunction multiplyResult = service.multiply(func1, func2);
        TabulatedFunction divideResult = service.divide(func1, func2);
        // Проверяем сложение
        assertEquals(6.0, addResult.getY(0)); // 4.0+2.0 = 6.0
        assertEquals(8.0, addResult.getY(1)); // 5.0+3.0 = 8.0
        assertEquals(10.0, addResult.getY(2)); // 6.0+4.0 = 10.0
        // Проверяем вычитание
        assertEquals(2.0, subtractResult.getY(0)); //4.0-2.0 = 2.0
        assertEquals(2.0, subtractResult.getY(1)); //5.0-3.0 = 2.0
        assertEquals(2.0, subtractResult.getY(2)); //6.0-4.0 = 2.0
        //Проверяем умножение
        assertEquals(8.0, multiplyResult.getY(0)); //4.0*2.0 = 8.0
        assertEquals(15.0, multiplyResult.getY(1)); //5.0*3.0 = 15.0
        assertEquals(24.0, multiplyResult.getY(2)); //6.0*4.0 = 24.0
        //Проверяем деление
        assertEquals(2.0, divideResult.getY(0)); //4.0/2.0 = 2.0
        assertEquals(5.0/3.0, divideResult.getY(1), 1e-10); //5.0/3.0 примерно 1.666...
        assertEquals(1.5, divideResult.getY(2)); //6.0/4.0 = 1.5
    }
}