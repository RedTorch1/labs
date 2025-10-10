package operations;

import functions.MathFunction;

public class MiddleSteppingDifferentialOperator extends SteppingDifferentialOperator {

    public MiddleSteppingDifferentialOperator(double step) {
        super(step);
    }

    public MathFunction derive(final MathFunction function) {
        final double h = this.step;
        return new MathFunction() {
            public double apply(double x) {
                return (function.apply(x + h) - function.apply(x - h)) / (2 * h);
            }
        };
    }
}
