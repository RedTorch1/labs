package concurrent;
import functions.TabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.ConstantFunction;
public class ReadWriteTaskExecutor {
    public static void main(String[] args) {
        ConstantFunction constantFunction = new ConstantFunction(-1);
        TabulatedFunction tabulatedFunction = new LinkedListTabulatedFunction(constantFunction,1,1000,1000);

        Object lock=new Object();

        ReadTask readTask=new ReadTask(tabulatedFunction, lock);
        WriteTask writeTask=new WriteTask(tabulatedFunction, 0.5, lock);

        Thread readThread=new Thread(readTask); //Многопоточность
        Thread writeThread=new Thread(writeTask);

        readThread.start();
        writeThread.start();
        try { //Для того чтобы оно выводилось нормально (сначала чтение потом запись), нужно было создать доп замок
            readThread.join();
            writeThread.join();
        } catch(InterruptedException e) {
            e.printStackTrace(); //Вывод стека и ошибки
        }
        System.out.println("Program finished."); //ладно оказалось проще чем казалось
    }
} //На коммиты кстати особо не смотрите что они разом все сделаны, я просто забыл пушить
//4(5) пункта подряд делают своё дело
