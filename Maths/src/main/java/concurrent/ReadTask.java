package concurrent;
import functions.TabulatedFunction;
public class ReadTask implements Runnable{
    private final TabulatedFunction function;
    private final Object lock;
    public static boolean readturn=true; //Переменная для синхронизации

    public ReadTask(TabulatedFunction function, Object lock) {
        this.function=function;
        this.lock=lock;
    }
    public void run() {
        int count=function.getCount();
        for (int i=0;i<count;i++) {
            synchronized (lock) { //Блок синхоринзации для работы с потоками
                while(!readturn) { //Ждём свой ход
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                double x=function.getX(i);
                double y=function.getY(i);
                System.out.printf("After read: i=%d, x=%f, y=%f\n",i,x,y);
                readturn=false; //Передаём ход другому
                lock.notifyAll();
                }
            }
        }
    }