package concurrent;
import functions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadWriteTaskExecutor {
    private static final Logger logger = LoggerFactory.getLogger(ReadWriteTaskExecutor.class);
    public static void main(String[] args) {
        logger.info("Запуск ReadWriteTaskExecutor");
        ConstantFunction constantFunction = new ConstantFunction(-1);
        TabulatedFunction tabulatedFunction = new LinkedListTabulatedFunction(constantFunction,1,1000,1000);

        Object lock=new Object();

        ReadTask readTask=new ReadTask(tabulatedFunction, lock);
        WriteTask writeTask=new WriteTask(tabulatedFunction, 0.5, lock);

        Thread readThread=new Thread(readTask); //Многопоточность
        Thread writeThread=new Thread(writeTask);

        logger.debug("Потоки созданы, запускаем их...");
        readThread.start();
        writeThread.start();

        try { //Для того чтобы оно выводилось нормально (сначала чтение потом запись), нужно было создать доп замок
            readThread.join();
            writeThread.join();
        } catch(InterruptedException e) {
            logger.error("Потоки были прерваны", e); //Сообщения были заменены на логирование
        }
        logger.info("Оба потока завершены.");
    }

} //На коммиты кстати особо не смотрите что они разом все сделаны, я просто забыл пушить
//4(5) пункта подряд делают своё дело
//Файл покрыт логами (это уже новый коммит)