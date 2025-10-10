package operations;

import functions.MathFunction;

public class RightSteppingDifferentialOperator extends SteppingDifferentialOperator {

    public RightSteppingDifferentialOperator(double step) {
        super(step);
    }

    public MathFunction derive(final MathFunction function) {
        final double h = this.step;
        return new MathFunction() {
            public double apply(double x) {
                return (function.apply(x + h) - function.apply(x)) / h;
            }
        };
    }
}
