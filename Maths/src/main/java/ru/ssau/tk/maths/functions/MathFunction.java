package ru.ssau.tk.maths.functions;

public interface MathFunction {
    double apply(double x);

    default CompositeFunction andThen(MathFunction afterFunction) { //Добавляем новый метод в интерфейс
        return new CompositeFunction(this, afterFunction);
    }

}

