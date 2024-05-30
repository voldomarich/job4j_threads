package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {

    @Override
    public void run() {
        char[] process = new char[] {'|', '/', '|', '\\', '-'};
        try {
            int i = 0;
            while (!Thread.currentThread().isInterrupted()) {
                System.out.print("\r Загрузка: " + process[i]); /* выводим символ загрузки */
                Thread.sleep(500);
                i++;
                if (i == process.length) {
                    i = 0;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(20000); /* симулируем выполнение параллельной задачи в течение 20 секунд. */
        progress.interrupt();
    }
}
