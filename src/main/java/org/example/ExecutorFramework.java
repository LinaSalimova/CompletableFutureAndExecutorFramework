package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class ExecutorFramework {
    static class Transaction {
        final int fromId;
        final int toId;
        final int amount;

        Transaction(int fromId, int toId, int amount) {
            this.fromId = fromId;
            this.toId = toId;
            this.amount = amount;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите количество пользователей:");
        int n = scanner.nextInt();

        System.out.println("Введите начальные балансы для " + n + " пользователей через пробел:");
        AtomicIntegerArray balances = new AtomicIntegerArray(n);

        for (int i = 0; i < n; i++) {
            balances.set(i, scanner.nextInt());
        }

        System.out.println("Введите количество транзакций:");
        int m = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Введите " + m + " транзакций в формате 'fromId - amount - toId':");
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 0; i < m; i++) {
            System.out.println("Транзакция " + (i + 1) + ":");
            String[] parts = scanner.nextLine().split(" - ");
            int fromId = Integer.parseInt(parts[0]);
            int amount = Integer.parseInt(parts[1]);
            int toId = Integer.parseInt(parts[2]);

            transactions.add(new Transaction(fromId, toId, amount));
        }

        scanner.close();

        System.out.println("Обработка транзакций...");
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (Transaction transaction : transactions) {
            executor.submit(() -> processTransaction(balances, transaction));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Результаты обработки транзакций:");
        for (int i = 0; i < n; i++) {
            System.out.println("User " + i + " final balance: " + balances.get(i));
        }
    }

    private static void processTransaction(AtomicIntegerArray balances, Transaction transaction) {
        balances.addAndGet(transaction.fromId, -transaction.amount);
        balances.addAndGet(transaction.toId, transaction.amount);
    }
}
