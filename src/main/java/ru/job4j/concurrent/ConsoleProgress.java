package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {

    @Override
    public void run() {
        char[] process = new char[] {'|', '/', '|', '\\', '-'};
        try {
            int index = 0;
            while (!Thread.currentThread().isInterrupted()) {
                System.out.print("\r Загрузка: " + process[index++]); /* выводим символ загрузки */
                if (index == process.length) {
                    index = 0;
                }
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(20000); /* симулируем выполнение параллельной задачи в течение 20 секунд. */
        progress.interrupt();
    }
}
