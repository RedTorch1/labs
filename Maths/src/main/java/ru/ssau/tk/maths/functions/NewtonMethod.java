package ru.ssau.tk.maths.functions;

public class NewtonMethod {
    private final MathFunction function; // Функция
    private final double tolerance; //Погрешность
    private final int maxIterations; //Максимальное число итераций
    private final double h; //шаг для производной

    public NewtonMethod(MathFunction function, double tolerance, int maxIterations, double h)
    {
        this.function=function; //Инициализация, конструктор при всех заданных значениях
        this.tolerance=tolerance;
        this.maxIterations=maxIterations;
        this.h=h;
    }

    public NewtonMethod(MathFunction function)
    {
        this(function,1e-8,100,1e-6); //Если задана только функция
    }

    public double solve(double initialGuess) //Само решение
    {
        double currentX = initialGuess; //Текущий x
        for (int iteration=0;iteration<maxIterations;iteration++) //Прогоняем итерации
        {
            double fValue=function.apply(currentX); //Значение функции в x
            double dValue=DerivativeFunction.derivative(function,currentX,h); //Значение производной в x
            if(Math.abs(dValue)<1e-12)
            {
                throw new ArithmeticException("The derivative is close to zero in x="+currentX);
            } //^^^Исключение, если производная близка к нулю(на ноль делить нельзя)
            double nextX=currentX-fValue/dValue; //Сама формула метода Ньютона
            if (Math.abs(nextX-currentX)<tolerance) { //Условие сходимости
                System.out.println("Converged in "+(iteration+1)+" iterations");
                return nextX;
            }
            currentX=nextX;
        }
        throw new RuntimeException("Didn't converge in "+maxIterations+" iterations"); //Не сошлось :(
    }
}
