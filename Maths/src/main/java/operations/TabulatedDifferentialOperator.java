package operations;
import functions.Point;
import functions.TabulatedFunction;
import functions.factory.TabulatedFunctionFactory;
import functions.factory.ArrayTabulatedFunctionFactory;
public class TabulatedDifferentialOperator implements DifferentialOperator<TabulatedFunction>{
    private TabulatedFunctionFactory factory;
    public TabulatedDifferentialOperator(TabulatedFunctionFactory factory) {
        this.factory=factory; //Конструктор по фабрике
    }
    public TabulatedDifferentialOperator() {
        this.factory=new ArrayTabulatedFunctionFactory(); //По умолчанию конструктор
    }
    public TabulatedFunctionFactory getFactory() {
        return factory; //геттер
    }
    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory=factory; //сеттер
    }
    public TabulatedFunction derive(TabulatedFunction function) {
        //Получим все точки
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        Point[] points=service.asPoints(function);
        int count =points.length;
        //Создаём массивы для x и y
        double[] xValues=new double[count];
        double[] yValues=new double[count];
        //Заполняем иксы
        for (int i=0;i<count;i++) {xValues[i]=points[i].x;}
        //Вычисление производных численным методом(тангенс угла)
        for(int i=0;i<count;i++) {
            if(i==0) {yValues[i]=(points[i+1].y-points[i].y)/(points[i+1].x-points[i].x);} //левая граница
            else if(i==count-1) {yValues[i]=(points[i].y-points[i-1].y)/(points[i].x-points[i-1].x);} //правая граница
            else {yValues[i]=(points[i+1].y-points[i-1].y)/(points[i+1].x-points[i-1].x);} //центр центр
        }
        return factory.create(xValues,yValues); //Создаём табу функцию через фабричку
    }
}
