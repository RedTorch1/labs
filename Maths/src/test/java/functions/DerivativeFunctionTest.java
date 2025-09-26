package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DerivativeFunctionTest {
    void testBasicDeprivative()
    {
        //f(x)=x^2 => f'(x)=2x, x=2
        MathFunction square=new SqrFunction();
        double result=DerivativeFunction.derivative(square,2);
        assertEquals(4,result,0.01); //Должно выдать 4
    }
    void testDeprivativeAtZero()
    {
        //f(x)=x^2 f'(0)=0
        MathFunction square=new SqrFunction();
        double result=DerivativeFunction.derivative(square,0);
        assertEquals(0,result,0.01); //Должен выдать 0
    }
    void testDerivativeWithDifferentStep() {
        MathFunction square=new SqrFunction();
        double result1=DerivativeFunction.derivative(square,3,0.001);
        double result2=DerivativeFunction.derivative(square,3,0.0001);
        //f'(3)=6, оба результата должны быть около 6
        assertEquals(6,result1,0.1);
        assertEquals(6,result2,0.1);
    }
}