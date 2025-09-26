package functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdentityFunctionTest {
    void apply()
    {
        MathFunction identity = new IdentityFunction(); //Объект тестирования

        //Тесты
        assertEquals(0, identity.apply(0), 0.0001); //apply должен вернуть 0
        assertEquals(5, identity.apply(5), 0.0001); //должен 5
        assertEquals(-3.7, identity.apply(-3.7), 0.0001); //должен -3.7
        assertEquals(2.5, identity.apply(2.5), 0.0001); //должен 2.5
    }

}