package ru.ssau.tk.maths.functions;

import ru.ssau.tk.maths.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.maths.exceptions.DifferentLengthOfArraysException;

abstract public class AbstractTabulatedFunction {
    protected int count; // число табулированных точек
    // Методы, которые нужно будет реализовать в наследниках
    abstract int getCount();
    abstract double getX(int index);
    abstract double getY(int index);
    abstract void setY(int index, double value);
    abstract int indexOfX(double x);
    abstract int indexOfY(double y);
    abstract double leftBound();
    abstract double rightBound();
    protected abstract int floorIndexOfX(double x);
    protected abstract double extrapolateLeft(double x);
    protected abstract double extrapolateRight(double x);
    protected abstract double interpolate(double x, int floorIndex);

    // Универсальный метод интерполяции (общая формула)
    protected double interpolate(double x, double leftX, double rightX, double leftY, double rightY) {
        return leftY + (rightY - leftY) * (x - leftX) / (rightX - leftX);
    }
    
    public double apply(double x) {
        if (x < leftBound()) {
            return extrapolateLeft(x);
        }
        if (x > rightBound()) {
            return extrapolateRight(x);
        }
        int index = indexOfX(x);
        if (index != -1) {
            return getY(index);
        }
        int floorIndex = floorIndexOfX(x);
        return interpolate(x, floorIndex);
    }

    static void checkLengthIsTheSame(double[] xValues, double[] yValues)
    {
        if (xValues.length != yValues.length)
            throw new DifferentLengthOfArraysException();
    }

    static void checkSorted(double[] xValues)
    {
        for (int i=1;i<xValues.length;i++)
            if (xValues[i] <= xValues[i-1])
                throw new ArrayIsNotSortedException();
    }
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(getClass().getSimpleName()).append(" size = ").append(getCount()).append("\n");
        for (int i = 0; i<getCount(); i++)
        {
            sb.append("[").append(getX(i)).append("; ").append(getY(i)).append("]\n");
        }
        return sb.toString();
    }
}
