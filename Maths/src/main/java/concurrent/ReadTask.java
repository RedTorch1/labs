package concurrent;

import functions.TabulatedFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadTask implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(ReadTask.class);


    private final TabulatedFunction function;
    private final Object lock;
    public static boolean readturn=true; //Переменная для синхронизации

    public ReadTask(TabulatedFunction function, Object lock) {
        this.function=function;
        this.lock=lock;
    }
    public void run() {
        logger.info("Поток чтения {} запущен", Thread.currentThread().getName());
        int count=function.getCount();
        for (int i=0;i<count;i++) {
            synchronized (lock) { //Блок синхоринзации для работы с потоками
                while(!readturn) { //Ждём свой ход
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        logger.warn("Поток {} был прерван во время ожидания", Thread.currentThread().getName(), e);
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                double x=function.getX(i);
                double y=function.getY(i);
                logger.debug("Чтение: i={}, x={}, y={}", i, x, y);
                readturn=false; //Передаём ход другому
                lock.notifyAll();
                }
            }
        logger.info("Поток чтения {} завершён", Thread.currentThread().getName());
        }
    }