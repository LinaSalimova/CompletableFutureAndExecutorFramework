package org.example;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CompletableFuture {
    public static void main(String[] args) {
        // Пример входных данных
        double a = 3.0;
        double b = 4.0;
        double c = 10.0;
        double d = 16.0;

        try {
            // Создаем асинхронные задачи для каждой части формулы
            java.util.concurrent.CompletableFuture<Double> sumOfSquaresFuture = java.util.concurrent.CompletableFuture.supplyAsync(() -> calculateSumOfSquares(a, b));
            java.util.concurrent.CompletableFuture<Double> sqrtFuture = java.util.concurrent.CompletableFuture.supplyAsync(() -> calculateSqrt(d));
            java.util.concurrent.CompletableFuture<Double> logFuture = java.util.concurrent.CompletableFuture.supplyAsync(() -> calculateLog(c));

            // Комбинируем результаты для получения итогового значения
            java.util.concurrent.CompletableFuture<Double> resultFuture = sumOfSquaresFuture
                    .thenCombine(logFuture, (sum, log) -> sum * log)
                    .thenCombine(sqrtFuture, (product, sqrt) -> product / sqrt);

            // Получаем результат
            double result = resultFuture.get();
            System.out.println("Final result of the formula: " + result);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static double calculateSumOfSquares(double a, double b) {
        try {
            // Симуляция задержки в 5 секунд
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        double result = Math.pow(a, 2) + Math.pow(b, 2);
        System.out.println("Calculating sum of squares: " + result);
        return result;
    }

    private static double calculateSqrt(double d) {
        try {
            // Симуляция задержки в 10 секунд
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        double result = Math.sqrt(d);
        System.out.println("Calculating sqrt(d): " + result);
        return result;
    }

    private static double calculateLog(double c) {
        try {
            // Симуляция задержки в 15 секунд
            TimeUnit.SECONDS.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        double result = Math.log(c);
        System.out.println("Calculating log(c): " + result);
        return result;
    }
}
