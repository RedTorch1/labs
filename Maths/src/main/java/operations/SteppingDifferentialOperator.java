package operations;

import functions.MathFunction;

public abstract class SteppingDifferentialOperator implements DifferentialOperator<MathFunction> {
    protected double step;

    public SteppingDifferentialOperator(double step) {
        // Проверка: шаг должен быть положителен, конечен и не NaN
        if (step <= 0.0 || !Double.isFinite(step)) {
            throw new IllegalArgumentException("Step must be > 0 and finite, was: " + step);
        }
        this.step = step;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        if (step <= 0.0 || !Double.isFinite(step)) {
            throw new IllegalArgumentException("Step must be > 0 and finite, was: " + step);
        }
        this.step = step;
    }
}
