package io;

import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TabulatedFunctionFileWriter {
    public static void main(String[] args) {

        File outDir = new File("output");
        if (!outDir.exists()) {
            boolean created = outDir.mkdirs();
            if (!created) {
                System.err.println("Не удалось создать директорию output");
            }
        }

        // Примеры данных: равномерная сетка из 5 точек на [0, 2]
        double[] xValues = new double[] {0.0, 0.5, 1.0, 1.5, 2.0};
        double[] yValuesArray = new double[] {0.0, 0.25, 1.0, 2.25, 4.0}; // y = x^2
        double[] yValuesList  = new double[] {1, 2, 3, 4, 5};    // просто пример

        // Создаём две функции: массивная и на связном списке
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValuesArray);
        TabulatedFunction linkedListFunc = new LinkedListTabulatedFunction(xValues, yValuesList);

        // Путь к файлам
        File arrayFile = new File(outDir, "array function.txt");
        File listFile  = new File(outDir, "linked list function.txt");

        // Открываем два FileWriter в одной конструкции try-with-resources (оборачиваем в BufferedWriter)
        try (BufferedWriter bwArray = new BufferedWriter(new FileWriter(arrayFile));
             BufferedWriter bwList  = new BufferedWriter(new FileWriter(listFile))) {

            // Используем ранее реализованный метод записи (в этом проекте ожидается наличие FunctionsIO)
            FunctionsIO.writeTabulatedFunction(bwArray, arrayFunc);
            FunctionsIO.writeTabulatedFunction(bwList, linkedListFunc);

            System.out.println("Функции успешно записаны в:");
            System.out.println(" - " + arrayFile.getPath());
            System.out.println(" - " + listFile.getPath());

        } catch (IOException e) {
            // Требуется: вывести stack trace в поток ошибок
            e.printStackTrace(System.err);
        }
    }
}
