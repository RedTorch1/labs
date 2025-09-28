package functions;

import java.util.Arrays;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction implements Removable{
    private double[] xValues;
    private double[] yValues;

    // Конструктор по массивам
    public ArrayTabulatedFunction(double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length || xValues.length < 2) {
            throw new IllegalArgumentException("Некорректные массивы");
        }
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

    public double getX(int index) { return xValues[index]; }

    public double getY(int index) { return yValues[index]; }

    public void setY(int index, double value) { yValues[index] = value; }

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
        if (x < xValues[0]) return 0;
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
}
