package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompositeFunctionTest {
    void testApplySimpleComposition()
    {
        //f(x)=x
        MathFunction f=new IdentityFunction();
        //g(x)=x^2
        MathFunction g=new SqrFunction();
        //h(x)=g(f(x))
        CompositeFunction h =new CompositeFunction(f,g);
        assertEquals(0,h.apply(0),0.0001);
        assertEquals(25,h.apply(5),0.0001);
        assertEquals(9,h.apply(3),0.0001);
    }
    void testApplyComplexComposition()
    {
        //f(x)=x^2
        MathFunction f=new SqrFunction();
        //g(x)=x+1
        MathFunction g=new MathFunction() {
            public double apply(double x) {
                return x+1;
            }
        };
        //h(x)=g(f(x))=g(x^2)=x^2+1
        CompositeFunction h=new CompositeFunction(f,g);
        assertEquals(1,h.apply(0),0.0001);
        assertEquals(2,h.apply(1),0.0001);
        assertEquals(5,h.apply(2),0.0001);
        assertEquals(10,h.apply(-3),0.0001);
    }
    void testApplyCompositeOfComposites()
    {
        //Композит в композите f(x)=x g(x)=x^2 h(x)=g(f(x)) p(x)=f(h(x))=f(g(f(x)))
        MathFunction f=new IdentityFunction();
        MathFunction g=new SqrFunction();
        CompositeFunction h= new CompositeFunction(f,g);
        CompositeFunction p= new CompositeFunction(h,f);
        assertEquals(1,p.apply(1),0.0001);
        assertEquals(4,p.apply(2),0.0001);
    }
}