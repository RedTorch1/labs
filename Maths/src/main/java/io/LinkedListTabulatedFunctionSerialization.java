package io;

import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import operations.TabulatedDifferentialOperator;
import java.io.*;

public class LinkedListTabulatedFunctionSerialization {
    public static void main(String[] args) {
        //Часть 1: Сериализация функций
        File outputFile = new File("output/serialized linked list functions.bin");
        //Создаём директорию output если её нет
        File outputDir = outputFile.getParentFile();
        if (!outputDir.exists()) {
            boolean created = outputDir.mkdirs();
            if (!created) {
                System.err.println("Не удалось создать директорию output");
                return;
            }
        }
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            //Создаём исходную функцию
            double[] xValues = {0.0, 1.0, 2.0, 3.0, 4.0};
            double[] yValues = {0.0, 1.0, 4.0, 9.0, 16.0}; // f(x) = x^2
            LinkedListTabulatedFunction originalFunction = new LinkedListTabulatedFunction(xValues, yValues);
            //Создаём оператор для вычисления производных
            TabulatedDifferentialOperator diffOperator = new TabulatedDifferentialOperator();
            //Вычисляем первую и вторую производные
            TabulatedFunction firstDerivative = diffOperator.derive(originalFunction);
            TabulatedFunction secondDerivative = diffOperator.derive(firstDerivative);
            //Сериализуем все три функции в поток
            //Для сериализации используем ObjectOutputStream напрямую
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(originalFunction);
            oos.writeObject(firstDerivative);
            oos.writeObject(secondDerivative);
            oos.flush();
            System.out.println("Функции успешно сериализованы в файл: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        //Часть 2: Десериализация функций
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(outputFile))) {
            //Десериализуем все три функции
            TabulatedFunction deserializedOriginal = FunctionsIO.deserialize(bis);
            TabulatedFunction deserializedFirstDerivative = FunctionsIO.deserialize(bis);
            TabulatedFunction deserializedSecondDerivative = FunctionsIO.deserialize(bis);
            //Выводим функции в консоль
            System.out.println("\n=== Десериализованная исходная функция ===");
            System.out.println(deserializedOriginal.toString());
            System.out.println("\n=== Десериализованная первая производная ===");
            System.out.println(deserializedFirstDerivative.toString());
            System.out.println("\n=== Десериализованная вторая производная ===");
            System.out.println(deserializedSecondDerivative.toString());
            //Проверяем типы десериализованных функций
            System.out.println("\n=== Типы десериализованных функций ===");
            System.out.println("Исходная функция: " + deserializedOriginal.getClass().getSimpleName());
            System.out.println("Первая производная: " + deserializedFirstDerivative.getClass().getSimpleName());
            System.out.println("Вторая производная: " + deserializedSecondDerivative.getClass().getSimpleName());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(System.err);
        }
    } //ура победа
}