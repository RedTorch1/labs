package io;

import functions.TabulatedFunction;
import functions.factory.ArrayTabulatedFunctionFactory;
import functions.factory.LinkedListTabulatedFunctionFactory;
import functions.factory.TabulatedFunctionFactory;
import operations.TabulatedDifferentialOperator;
import java.io.*;

public class TabulatedFunctionFileInputStream {
    public static void main(String[] args) {
        //Часть 1: Чтение из бинарного файла
        File inputFile = new File("input/binary function.bin");
        if (!inputFile.exists()) {
            System.err.println("Файл не найден: " + inputFile.getPath());
            return;
        }
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile))) {
            TabulatedFunctionFactory arrayFactory = new ArrayTabulatedFunctionFactory();
            TabulatedFunction arrayFunction = FunctionsIO.readTabulatedFunction(bis, arrayFactory);
            System.out.println("=== Функция из бинарного файла ===");
            System.out.println(arrayFunction.toString());
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        //Часть 2: Чтение из консоли
        System.out.println("Введите размер и значения функции: ");
        //Не используем try-with-resources, так как нельзя закрывать System.in
        BufferedReader consoleReader = null;
        try {
            consoleReader = new BufferedReader(new InputStreamReader(System.in));
            TabulatedFunctionFactory linkedListFactory = new LinkedListTabulatedFunctionFactory();
            TabulatedFunction consoleFunction = FunctionsIO.readTabulatedFunction(consoleReader, linkedListFactory);
            System.out.println("=== Введенная функция ===");
            System.out.println(consoleFunction.toString());
            //Вычисление производной
            TabulatedDifferentialOperator diffOperator = new TabulatedDifferentialOperator();
            TabulatedFunction derivative = diffOperator.derive(consoleFunction);
            System.out.println("=== Производная функции ===");
            System.out.println(derivative.toString());
        } catch (IOException e) {
            e.printStackTrace(System.err);
        } //ура оно реально работает
    }
}