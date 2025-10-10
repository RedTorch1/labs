package io;

import functions.TabulatedFunction;
import functions.factory.ArrayTabulatedFunctionFactory;
import functions.factory.LinkedListTabulatedFunctionFactory;
import functions.factory.TabulatedFunctionFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class TabulatedFunctionFileReader {

    public static void main(String[] args) {
        File inputFile = new File("input/function.txt");

        if (!inputFile.exists()) {
            System.err.println("Файл не найден: " + inputFile.getPath());
            return;
        }

        // Открываем два FileReader в одной конструкции try-with-resources и оборачиваем в BufferedReader
        try (BufferedReader readerForArray = new BufferedReader(new FileReader(inputFile));
             BufferedReader readerForList  = new BufferedReader(new FileReader(inputFile))) {

            // Создаём фабрики для соответствующих реализаций
            TabulatedFunctionFactory arrayFactory = new ArrayTabulatedFunctionFactory();
            TabulatedFunctionFactory listFactory  = new LinkedListTabulatedFunctionFactory();

            // Читаем функции используя FunctionsIO.readTabulatedFunction
            TabulatedFunction arrayFunction = FunctionsIO.readTabulatedFunction(readerForArray, arrayFactory);
            TabulatedFunction listFunction  = FunctionsIO.readTabulatedFunction(readerForList, listFactory);

            // Выводим в консоль
            System.out.println("=== ArrayTabulatedFunction ===");
            System.out.println(arrayFunction.toString());

            System.out.println("=== LinkedListTabulatedFunction ===");
            System.out.println(listFunction.toString());

        } catch (IOException e) {
            // Обработка: вывести stacktrace в поток ошибок
            e.printStackTrace(System.err);
        }
    }
}
