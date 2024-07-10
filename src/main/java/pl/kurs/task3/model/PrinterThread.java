package pl.kurs.task3.model;

public class PrinterThread extends Thread {
    private char letter;
    private int amount;
    private int interval;

    public PrinterThread(char letter, int amount, int interval) {
        this.letter = letter;
        this.amount = amount;
        this.interval = interval;
    }

    @Override
    public void run() {
        for (int i = 0; i < amount; i++) {
            System.out.println(letter);
            try {
                Thread.sleep(interval);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
