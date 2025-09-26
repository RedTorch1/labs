package functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqrFunctionTest {
    void apply()
    {
            MathFunction sqr = new SqrFunction(); //Объект тестирования

            //Тесты
            assertEquals(0, sqr.apply(0), 0.0001);
            assertEquals(25, sqr.apply(5), 0.0001);
            assertEquals(64, sqr.apply(8), 0.0001);
            assertEquals(100, sqr.apply(10), 0.0001);

    }
}