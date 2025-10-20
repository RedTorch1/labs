package concurrent;

import functions.TabulatedFunction;

public class MultiplyingTask implements Runnable {
    private final TabulatedFunction function;

    public MultiplyingTask(TabulatedFunction function) {
        this.function = function;
    }

    public void run() {
        for (int i = 0; i < function.getCount(); i++) {
            synchronized (function) {
                double oldY = function.getY(i);
                function.setY(i, oldY * 2);
            }
        }
        System.out.printf("Thread %s finished multiplying%n", Thread.currentThread().getName());
    }
}
