package concurrent;

import functions.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiplyingTaskExecutor {
    private static final Logger logger = LoggerFactory.getLogger(MultiplyingTaskExecutor.class);

    public static void main(String[] args) {
        logger.info("Запуск MultiplyingTaskExecutor");

        MathFunction unit = new ConstantFunction(1); //Работает, как unitfunction
        TabulatedFunction function = new LinkedListTabulatedFunction(unit, 1, 1000, 1000);
        logger.debug("Создана функция на основе UnitFunction");

        int threadsCount = 10;
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < threadsCount; i++) {
            Thread t = new Thread(new MultiplyingTask(function), "mul-" + i);
            threads.add(t);
        }

        for (Thread t : threads) t.start();
        logger.info("Все потоки умножения запущены");

        try {
            for (Thread t : threads) t.join();
            logger.info("Все потоки успешно завершены");
        }
        catch (InterruptedException e) {
            logger.error("Ошибка при ожидании потоков", e);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            logger.error("Ошибка при ожидании потоков", e);
        }

        logger.info("MultiplyingTaskExecutor завершил работу");

        for (int i = 0; i < 5; i++) {
            System.out.printf("[%d] x=%.3f y=%.3f%n", i, function.getX(i), function.getY(i));
        }
    }
}
