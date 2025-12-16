package ru.ssau.tk.maths.functions;
//h(x)=g(f(x))
public class CompositeFunction implements MathFunction{
    //приватные поля функций
    private final MathFunction firstFunction;
    private final MathFunction secondFunction;
    //Конструктор композитной(ну то есть сложной) функции
    public CompositeFunction(MathFunction firstFunction, MathFunction secondFunction)
    {
        this.firstFunction=firstFunction;
        this.secondFunction=secondFunction;
    }
    public double apply(double x)
    {
        return secondFunction.apply(firstFunction.apply(x)); //Сама сложная функция
    }
}
