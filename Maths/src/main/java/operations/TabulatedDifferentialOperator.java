package operations;

import functions.Point;
import functions.TabulatedFunction;
import functions.factory.TabulatedFunctionFactory;
import functions.factory.ArrayTabulatedFunctionFactory;
import concurrent.SynchronizedTabulatedFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TabulatedDifferentialOperator implements DifferentialOperator<TabulatedFunction>{
    private static final Logger logger = LoggerFactory.getLogger(TabulatedDifferentialOperator.class);

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
        logger.debug("Вычисление производной для функции типа {}", function.getClass().getSimpleName());
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
        logger.info("Производная успешно вычислена");
        return factory.create(xValues,yValues); //Создаём табу функцию через фабричку
    }
    public TabulatedFunction deriveSynchronously(TabulatedFunction function) {
        SynchronizedTabulatedFunction syncFunction;

        if (function instanceof SynchronizedTabulatedFunction) {
            syncFunction = (SynchronizedTabulatedFunction) function;
        }
        else {
            syncFunction = new SynchronizedTabulatedFunction(function);
        }

        return syncFunction.doSynchronously(func -> this.derive(func));
    }
}
//Ещё одна жертва логирования (пока требуется не так много классов с логами)