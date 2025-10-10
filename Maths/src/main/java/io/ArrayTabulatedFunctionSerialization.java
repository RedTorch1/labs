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


public class ArrayTabulatedFunctionSerialization {

    public static void main(String[] args) {
        File outDir = new File("output");
        if (!outDir.exists()) {
            boolean created = outDir.mkdirs();
            if (!created) {
                System.err.println("Не удалось создать директорию output");
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
            FunctionsIO.serialize(bos, original);
            FunctionsIO.serialize(bos, firstDeriv);
            FunctionsIO.serialize(bos, secondDeriv);

            bos.flush();

            System.out.println("Сериализация выполнена в файл: " + outFile.getPath());
        } catch (IOException e) {
            System.err.println("Ошибка при записи файла сериализации:");
            e.printStackTrace(System.err);
            return;
        }

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(outFile))) {
            TabulatedFunction deserializedOriginal = FunctionsIO.deserialize(bis);
            TabulatedFunction deserializedFirst = FunctionsIO.deserialize(bis);
            TabulatedFunction deserializedSecond = FunctionsIO.deserialize(bis);

            System.out.println("Начальная функция: ");
            System.out.println(deserializedOriginal.toString());

            System.out.println("Первая производная: ");
            System.out.println(deserializedFirst.toString());

            System.out.println("Вторая производная: ");
            System.out.println(deserializedSecond.toString());

        } catch (IOException e) {
            System.err.println("IOException при чтении сериализованного файла:");
            e.printStackTrace(System.err);
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException при десериализации:");
            e.printStackTrace(System.err);
        }
    }
}
