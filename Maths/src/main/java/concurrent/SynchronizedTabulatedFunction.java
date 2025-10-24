package concurrent;

import functions.Point;
import functions.TabulatedFunction;
import java.util.Iterator;

public class SynchronizedTabulatedFunction implements TabulatedFunction{
    private final TabulatedFunction function;
    private final Object lock;

    public interface Operation<T> {
        T apply(SynchronizedTabulatedFunction function);
    }
    public <T> T doSynchronously(Operation<? extends T> operation) {
        synchronized (lock) {
            return operation.apply(this);
        }
    }

    public SynchronizedTabulatedFunction(TabulatedFunction function)
    {
        this.function=function;
        this.lock=this; //использую объект как монитор
    }
    //МЕТОДЫ
    public synchronized int getCount() {
        return function.getCount();
    }
    public synchronized double getX(int index) {
        return function.getX(index);
    }
    public synchronized double getY(int index) {
        return function.getY(index);
    }
    public synchronized void setY(int index, double val) {
        function.setY(index,val);
    }
    public synchronized int indexOfX(double x) {
        return function.indexOfX(x);
    }
    public synchronized int indexOfY(double y) {
        return function.indexOfY(y);
    }
    public synchronized double leftBound() {
        return function.leftBound();
    }
    public synchronized double rightBound() {
        return function.rightBound();
    }
    public synchronized double apply(double x) {
        return function.apply(x);
    }

    //Синхронизируем и итератор
    //Дэм поторопился получается ладно ща перепишем
    public synchronized Iterator<Point> iterator() {
        //на всякий случай делаю копии точек
        Point[] points = new Point[function.getCount()];
        for (int i=0;i<function.getCount();i++) {
            points[i]=new Point(function.getX(i),function.getY(i));
        }
        return new Iterator<Point>() {
            private int currentIndex=0;
            private final Point[] pointArray=points;
            public boolean hasNext() {
                return currentIndex<pointArray.length;
            }
            public Point next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                return pointArray[currentIndex++];
            }
        };
    }
    public synchronized String toString() {
        return function.toString();
    }
}
