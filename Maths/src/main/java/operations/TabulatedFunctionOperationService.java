package operations;

import functions.*;
import functions.factory.TabulatedFunctionFactory;
import exceptions.InconsistentFunctionsException;

public class TabulatedFunctionOperationService {
    private TabulatedFunctionFactory factory;

    public TabulatedFunctionOperationService() {
        this.factory = new functions.factory.ArrayTabulatedFunctionFactory();
    }
    public TabulatedFunctionOperationService(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }
    public TabulatedFunctionFactory getFactory() { return factory; }
    public void setFactory(TabulatedFunctionFactory factory) { this.factory = factory; }

    public Point[] asPoints(TabulatedFunction tabulatedFunction) {
        Point[] pts = new Point[tabulatedFunction.getCount()];
        int i = 0;
        for (Point p : tabulatedFunction) {
            pts[i++] = p;
        }
        return pts;
    }

    private interface BiOperation { double apply(double u, double v); }

    private TabulatedFunction doOperation(TabulatedFunction a, TabulatedFunction b, BiOperation op) {
        if (a.getCount() != b.getCount()) throw new InconsistentFunctionsException("Different counts");
        Point[] pa = asPoints(a);
        Point[] pb = asPoints(b);
        int n = pa.length;
        double[] xValues = new double[n];
        double[] yValues = new double[n];
        for (int i = 0; i < n; i++) {
            if (Double.compare(pa[i].x, pb[i].x) != 0) {
                throw new InconsistentFunctionsException("x values differ at index " + i);
            }
            xValues[i] = pa[i].x;
            yValues[i] = op.apply(pa[i].y, pb[i].y);
        }
        return factory.create(xValues, yValues);
    }

    public TabulatedFunction add(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (u, v) -> u + v);
    }
    public TabulatedFunction subtract(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (u, v) -> u - v);
    }
}
