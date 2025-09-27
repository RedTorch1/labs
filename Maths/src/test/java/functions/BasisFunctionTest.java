package functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasisFunctionTest {

    void testDegree1() {
        double[] knots = {0, 1, 2, 3};
        //Проверяем N_{0,1}
        assertEquals(1, BasisFunction.N(0, 1, 0.5, knots), 0.0001);
        assertEquals(0, BasisFunction.N(0, 1, 1.5, knots), 0.0001);
    }
    
    void testPartitionOfUnity() {
        double[] knots = {0, 0, 0, 1, 2, 3, 3, 3}; // Кубический сплайн
        int degree = 3;
        double t = 1.5;

        double sum = 0;
        for (int i = 0; i < 4; i++) { // 4 контрольные точки
            sum += BasisFunction.N(i, degree + 1, t, knots);
        }

        //Сумма базисных функций должна быть 1
        assertEquals(1, sum, 0.0001);
    }
}