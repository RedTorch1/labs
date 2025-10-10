package functions;

import java.util.Arrays;
import java.util.Iterator;
import java.lang.Iterable;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable, Iterable<Point>{
    private double[] xValues;
    private double[] yValues;
    // Конструктор по массивам
    public ArrayTabulatedFunction(double[] xValues, double[] yValues) {
        if (xValues.length <2) { throw new IllegalArgumentException("Length of list lower than minimum (2)"); }
        if (xValues.length != yValues.length) {
            throw new IllegalArgumentException("Некорректные массивы");
        }

        checkLengthIsTheSame(xValues, yValues);
        checkSorted(xValues);

        this.count = xValues.length;
        this.xValues = Arrays.copyOf(xValues, count);
        this.yValues = Arrays.copyOf(yValues, count);
    }

    // Конструктор по другой функции + интервал + число точек
    public ArrayTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
        if (count < 2) throw new IllegalArgumentException("Недостаточно точек");
        if (xFrom > xTo) {
            double tmp = xFrom; xFrom = xTo; xTo = tmp;
        }
        this.count = count;
        this.xValues = new double[count];
        this.yValues = new double[count];

        if (xFrom == xTo) {
            Arrays.fill(this.xValues, xFrom);
            Arrays.fill(this.yValues, source.apply(xFrom));
        } else {
            double step = (xTo - xFrom) / (count - 1);
            for (int i = 0; i < count; i++) {
                xValues[i] = xFrom + i * step;
                yValues[i] = source.apply(xValues[i]);
            }
        }
    }

    public int getCount() { return count; }

    public double getX(int index) {
        if (index<0 || index>=count) {
            throw new IllegalArgumentException("Index out of range");
        }
        return xValues[index]; }

    public double getY(int index) {
        if (index<0 || index>=count) {
            throw new IllegalArgumentException("Index out of range");
        }
        return yValues[index]; }

    public void setY(int index, double value) {
        if (index<0||index>=count) {
            throw new IllegalArgumentException("Index out of range");
        }
        yValues[index] = value; }

    public int indexOfX(double x) {
        for (int i = 0; i < count; i++) if (xValues[i] == x) return i;
        return -1;
    }
    
    public int indexOfY(double y) {
        for (int i = 0; i < count; i++) if (yValues[i] == y) return i;
        return -1;
    }

    public double leftBound() { return xValues[0]; }

    public double rightBound() { return xValues[count - 1]; }

    protected int floorIndexOfX(double x) {
        if (x < xValues[0]) throw new IllegalArgumentException("X is lower than left border");
        if (x > xValues[count - 1]) return count - 1;
        for (int i = 0; i < count - 1; i++) {
            if (xValues[i] <= x && x < xValues[i + 1]) return i;
        }
        return count - 1;
    }

    protected double extrapolateLeft(double x) {
        return interpolate(x, xValues[0], xValues[1], yValues[0], yValues[1]);
    }

    protected double extrapolateRight(double x) {
        return interpolate(x, xValues[count - 2], xValues[count - 1],
                yValues[count - 2], yValues[count - 1]);
    }

    protected double interpolate(double x, int floorIndex) {
        return interpolate(x,
                xValues[floorIndex], xValues[floorIndex + 1],
                yValues[floorIndex], yValues[floorIndex + 1]);
    }

    public void remove(int index) {
        if (count <= 1) {
            throw new IllegalStateException("Нельзя удалить последнюю точку");
        }
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Некорректный индекс");
        }

        double[] newX = new double[count - 1];
        double[] newY = new double[count - 1];

        System.arraycopy(xValues, 0, newX, 0, index);
        System.arraycopy(xValues, index + 1, newX, index, count - index - 1);

        System.arraycopy(yValues, 0, newY, 0, index);
        System.arraycopy(yValues, index + 1, newY, index, count - index - 1);

        this.xValues = newX;
        this.yValues = newY;
        this.count--;
    }
    public void insert(double x, double y)
    {
        //Проверяем есть ли x уже
        int existingIndex=indexOfX(x);
        if(existingIndex!=-1) {setY(existingIndex,y);return;} //Если есть, то меняем
        //Создаём новые массивы большего размера
        double[] newX=new double[count+1];
        double[] newY=new double[count+1];
        //Найдём позицию вставки
        int insertIndex=0;
        while(insertIndex<count&&xValues[insertIndex]<x) {insertIndex++;}
        //Копировальный центр (до позиции вставки)
        System.arraycopy(xValues,0,newX,0,insertIndex);
        System.arraycopy(yValues,0,newY,0,insertIndex);
        //Вставка нового
        newX[insertIndex]=x;
        newY[insertIndex]=y;
        //Копировальный центр(продолжение)
        System.arraycopy(xValues,insertIndex,newX,insertIndex+1,count-insertIndex);
        System.arraycopy(yValues,insertIndex,newY,insertIndex+1,count-insertIndex);
        this.xValues=newX;
        this.yValues=newY;
        count++;
    }

    public java.util.Iterator<Point> iterator() {
        return new java.util.Iterator<Point>() {
            private int i = 0;

            public boolean hasNext() {
                return i < count;
            }

            public Point next() {
                if (!hasNext()) throw new java.util.NoSuchElementException("No more elements in the tabulated function");
                Point p = new Point(xValues[i], yValues[i]);
                i++;
                return p;
            }
        };
    }
}
