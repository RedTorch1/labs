package concurrent;

import functions.TabulatedFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteTask implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(WriteTask.class);

    private final TabulatedFunction function;
    private final double value;
    private final Object lock;

    public WriteTask(TabulatedFunction function, double value, Object lock) {
        this.function=function;
        this.value=value;
        this.lock=lock;
    }
    public void run()
    {
        logger.info("Поток записи {} запущен (value = {})", Thread.currentThread().getName(), value);

        int count=function.getCount();
        for(int i=0; i<count;i++) {
            synchronized (lock) { //И тут тоже
                while(ReadTask.readturn) { //Ждём ход 2
                    try {
                        lock.wait();
                    } catch(InterruptedException e) {
                        logger.warn("Поток {} был прерван во время ожидания", Thread.currentThread().getName(), e);
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                function.setY(i,value);
                logger.debug("Запись: i={}, новое значение y={}", i, value);
                ReadTask.readturn=true; //Передаём ход 2
                lock.notifyAll();
            }
        }
        logger.info("Поток записи {} завершён", Thread.currentThread().getName());
    }
}
