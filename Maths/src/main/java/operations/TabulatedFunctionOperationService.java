package operations;

import functions.*;

public class TabulatedFunctionOperationService {

    public Point[] asPoints(TabulatedFunction tabulatedFunction) {
        Point[] pts = new Point[tabulatedFunction.getCount()];
        int i = 0;
        for (Point p : tabulatedFunction) {
            pts[i++] = p;
        }
        return pts;
    }
}
