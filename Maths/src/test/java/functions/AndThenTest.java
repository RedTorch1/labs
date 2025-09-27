package functions;

import static org.junit.jupiter.api.Assertions.*;

public class AndThenTest {

    
    public void testIdentityThenSquare() {
        MathFunction f = new IdentityFunction();
        MathFunction g = new SqrFunction();

        MathFunction composite = f.andThen(g);

        // Должно совпадать с g(f(x)) = x^2
        assertEquals(25.0, composite.apply(5.0), 1e-9);
        assertEquals(9.0, composite.apply(-3.0), 1e-9);
    }

    public void testSquareThenIdentity() {
        MathFunction f = new SqrFunction();
        MathFunction g = new IdentityFunction();

        MathFunction composite = f.andThen(g);

        // Должно совпадать с g(f(x)) = f(x) = x^2
        assertEquals(16.0, composite.apply(4.0), 1e-9);
    }

    public void testChainOfFunctions() {
        MathFunction f = new IdentityFunction();   // f(x) = x
        MathFunction g = new SqrFunction();       // g(x) = x^2
        MathFunction h = new UnitFunction();      // h(x) = 1

        // (f andThen g andThen h)(x) = h(g(f(x))) = 1
        MathFunction composite = f.andThen(g).andThen(h);

        assertEquals(1.0, composite.apply(100), 1e-9);
        assertEquals(1.0, composite.apply(-999), 1e-9);
    }

    public void testWithConstantFunction() {
        MathFunction f = new ConstantFunction(3.0); // f(x) = 3
        MathFunction g = new SqrFunction();         // g(x) = x^2

        MathFunction composite = f.andThen(g);

        // (f andThen g)(x) = g(f(x)) = (3)^2 = 9
        assertEquals(9.0, composite.apply(-5), 1e-9);
        assertEquals(9.0, composite.apply(100), 1e-9);
    }
}
