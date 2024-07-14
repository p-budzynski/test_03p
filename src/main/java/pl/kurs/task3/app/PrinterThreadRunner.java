package pl.kurs.task3.app;

import pl.kurs.task3.model.PrinterThread;

import java.util.concurrent.*;

public class PrinterThreadRunner {
    public static void main(String[] args) throws InterruptedException {
        PrinterThread pt1 = new PrinterThread('A', 10, 100);
        PrinterThread pt2 = new PrinterThread('B', 5, 100);
        PrinterThread pt3 = new PrinterThread('C', 10, 100);
        PrinterThread pt4 = new PrinterThread('X', 5, 100);

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CompletionService<Void> completionService = new ExecutorCompletionService<>(executorService);

        completionService.submit(pt1, null);
        completionService.submit(pt2, null);
        completionService.submit(pt3, null);

        completionService.take();

        executorService.submit(pt4);

        executorService.shutdown();

    }
}
