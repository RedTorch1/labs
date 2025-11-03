package io;

import functions.ArrayTabulatedFunction;
import functions.TabulatedFunction;
import functions.factory.ArrayTabulatedFunctionFactory;
import operations.TabulatedDifferentialOperator;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArrayTabulatedFunctionSerialization {
    private static final Logger logger = LoggerFactory.getLogger(ArrayTabulatedFunctionSerialization.class);

    public static void main(String[] args) {
        logger.info("Запуск сериализации функций");

        File outDir = new File("output");
        if (!outDir.exists()) {
            boolean created = outDir.mkdirs();
            if (!created) {
                logger.error("Не удалось обнаружить файл");
            }
        }

        File outFile = new File(outDir, "serialized array functions.bin");

        // Пример функции: f(x) = x^2 на сетке 0,0..2,0 с шагом 0.5
        double[] xValues = new double[] {0.0, 0.5, 1.0, 1.5, 2.0};
        double[] yValues = new double[] {0.0, 0.25, 1.0, 2.25, 4.0};

        TabulatedFunction original = new ArrayTabulatedFunction(xValues, yValues);

        TabulatedDifferentialOperator diffOperator = new TabulatedDifferentialOperator(new ArrayTabulatedFunctionFactory());

        TabulatedFunction firstDeriv = diffOperator.derive(original);
        TabulatedFunction secondDeriv = diffOperator.derive(firstDeriv);

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile))) {
            logger.debug("Открыт поток для записи");

            FunctionsIO.serialize(bos, original);
            FunctionsIO.serialize(bos, firstDeriv);
            FunctionsIO.serialize(bos, secondDeriv);

            bos.flush();

            logger.info("Функции успешно сериализованы");
        } catch (IOException e) {
            logger.error("Ошибка при сериализации функций", e);
        }

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(outFile))) {
            logger.debug("Открыт поток для чтения");

            TabulatedFunction deserializedOriginal = FunctionsIO.deserialize(bis);
            TabulatedFunction deserializedFirst = FunctionsIO.deserialize(bis);
            TabulatedFunction deserializedSecond = FunctionsIO.deserialize(bis);

            System.out.println("Начальная функция: ");
            System.out.println(deserializedOriginal.toString());

            System.out.println("Первая производная: ");
            System.out.println(deserializedFirst.toString());

            System.out.println("Вторая производная: ");
            System.out.println(deserializedSecond.toString());

            logger.info("Десериализация завершена успешно");
        } catch (IOException e) {
            logger.error("Ошибка при десериализации функций", e);
        } catch (ClassNotFoundException e) {
            logger.error("Ошибка при десериализации функций", e);
        }
        logger.info("Работа программы завершена");
    } //Включено логирование
}
