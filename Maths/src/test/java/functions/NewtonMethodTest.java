package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NewtonMethodTest {
    void solve()
    {
        //f(x)=x^2-9 корни x=3 и x=-3
        MathFunction func = new SqrFunction() {
            public double apply(double x) { return x*x-9;} //Конкретно класс под текущую функцию
        };
        NewtonMethod solver=new NewtonMethod(func);
        assertEquals(3,solver.solve(5),0.01); //Тестировка нахождения положительного корня
        assertEquals(3,solver.solve(-5),0.01); //Тестировка нахождения отрицательного корня
    }
    void testDefaultConstructor()
    {
        MathFunction func = new SqrFunction() {
            public double apply(double x)
            {
                return x*x-4; //f(x)=x^2-4
            }
        };
        NewtonMethod solver = new NewtonMethod(func); //Используем конструктор по умолчанию
        assertEquals(2,solver.solve(3),0.01); //Должен найти корень 2
    }
    void testImpossibleEquation()
    {
        //f(x)=1 (всегда положительна, корней нет)
        MathFunction func = new MathFunction() {
            public double apply(double x) {
                return 1;
            }
        };
        NewtonMethod solver=new NewtonMethod(func);
        assertThrows(ArithmeticException.class, () -> {solver.solve(0);});
    }
}