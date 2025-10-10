package operations;

import functions.MathFunction;

public class LeftSteppingDifferentialOperator extends SteppingDifferentialOperator {

    public LeftSteppingDifferentialOperator(double step) {
        super(step);
    }

    public MathFunction derive(final MathFunction function) {
        final double h = this.step;
        return new MathFunction() {
            public double apply(double x) {
                return (function.apply(x) - function.apply(x - h)) / h;
            }
        };
    }
}
