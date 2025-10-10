package io;

import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import java.io.*;

public class TabulatedFunctionFileOutputStream {
    public static void main(String[] args) {
        File outDir = new File("output");
        if (!outDir.exists()) {
            boolean created = outDir.mkdirs();
            if (!created) {
                System.err.println("Не удалось создать директорию output");
            }
        }
        //Примеры данных: равномерная сетка из 5 точек на [0, 2]
        double[] xValues = new double[] {0.0, 0.5, 1.0, 1.5, 2.0};
        double[] yValuesArray = new double[] {0.0, 0.25, 1.0, 2.25, 4.0}; //y = x^2
        double[] yValuesList  = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};   //линейная функция
        //Создаём две функции: массивная и на связном списке
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValuesArray);
        TabulatedFunction linkedListFunc = new LinkedListTabulatedFunction(xValues, yValuesList);
        //Путь к бинарным файлам
        File arrayFile = new File(outDir, "array function.bin");
        File listFile  = new File(outDir, "linked list function.bin");
        //Открываем два FileOutputStream в одной конструкции try-with-resources (оборачиваем в BufferedOutputStream)
        try (BufferedOutputStream bosArray = new BufferedOutputStream(new FileOutputStream(arrayFile));
             BufferedOutputStream bosList  = new BufferedOutputStream(new FileOutputStream(listFile))) {
            //Используем новый метод записи в байтовый поток
            FunctionsIO.writeTabulatedFunction(bosArray, arrayFunc);
            FunctionsIO.writeTabulatedFunction(bosList, linkedListFunc);
            System.out.println("Функции успешно записаны в бинарные файлы:");
            System.out.println(" - " + arrayFile.getAbsolutePath());
            System.out.println(" - " + listFile.getAbsolutePath());
            System.out.println("Размер файла array function.bin: " + arrayFile.length() + " байт");
            System.out.println("Размер файла linked list function.bin: " + listFile.length() + " байт");
        } catch (IOException e) {
            //Выводим stack trace в поток ошибок
            e.printStackTrace(System.err);
        }
    }
}