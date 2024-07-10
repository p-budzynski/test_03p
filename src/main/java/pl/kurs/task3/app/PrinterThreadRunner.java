package pl.kurs.task3.app;

import pl.kurs.task3.model.PrinterThread;

public class PrinterThreadRunner {
    public static void main(String[] args) {
        PrinterThread pt1 = new PrinterThread('A', 10, 100);
        PrinterThread pt2 = new PrinterThread('B', 2, 100);
        PrinterThread pt3 = new PrinterThread('C', 10, 100);
        PrinterThread pt4 = new PrinterThread('X', 5, 100);

        pt1.start();
        pt2.start();
        pt3.start();

        new Thread(() -> {
            try {
                while (true) {
                    if (!pt1.isAlive() || !pt2.isAlive() || !pt3.isAlive()) {
                        pt4.start();
                        break;
                    }
                    Thread.sleep(10);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();

    }
}
