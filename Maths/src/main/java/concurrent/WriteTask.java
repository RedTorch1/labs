package concurrent;
import functions.TabulatedFunction;
public class WriteTask implements Runnable{
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
        int count=function.getCount();
        for(int i=0; i<count;i++) {
            synchronized (lock) { //И тут тоже
                while(ReadTask.readturn) { //Ждём ход 2
                    try {lock.wait();} catch(InterruptedException e) {Thread.currentThread().interrupt(); return; }
                }
                function.setY(i,value);
                System.out.printf("Writing for index %d complete\n", i);
                ReadTask.readturn=true; //Передаём ход 2
                lock.notifyAll();
            }
        }
    }
}
