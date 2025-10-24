package concurrent;

import functions.*;
import java.util.ArrayList;
import java.util.List;

public class MultiplyingTaskExecutor {
    public static void main(String[] args) {
        MathFunction unit = new ConstantFunction(1); //Работает, как unitfunction
        TabulatedFunction function = new LinkedListTabulatedFunction(unit, 1, 1000, 1000);

        int threadsCount = 10;
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < threadsCount; i++) {
            Thread t = new Thread(new MultiplyingTask(function), "mul-" + i);
            threads.add(t);
        }

        for (Thread t : threads) t.start();

        try {
            for (Thread t : threads) t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All multiplying threads finished.");

        for (int i = 0; i < 5; i++) {
            System.out.printf("[%d] x=%.3f y=%.3f%n", i, function.getX(i), function.getY(i));
        }
    }
}
