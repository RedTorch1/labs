package functions;

public class ConstantFunction implements MathFunction {
    private final double value;

    public ConstantFunction(double value)
    {
        this.value = value;
    }

    public double apply(double x)
    {
        return value;
    }

    public double getValue()
    {
        return value;
    }
}

class ZeroFunction extends ConstantFunction {
    public ZeroFunction() {
        super(0);
    }
}

class UnitFunction extends ConstantFunction {
    public UnitFunction() {
        super(1);
    }
}