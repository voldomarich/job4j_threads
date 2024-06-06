package ru.job4j.concurrent;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Wget implements Runnable {

    private static final int LENGTH_BUFFER = 1024;
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream out = new FileOutputStream(fileName)) {
            byte[] dataBuffer = new byte[LENGTH_BUFFER];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                var startTime = System.currentTimeMillis();
                out.write(dataBuffer, 0, bytesRead);
                bytesRead += bytesRead;
                if (bytesRead >= speed) {
                    double downloadTime = System.currentTimeMillis() - startTime;
                    if (downloadTime < 1000) {
                        Thread.sleep((int) (1000 - downloadTime));
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            throw new IllegalArgumentException("Нет аргументов. Нужно установить аргументы:"
                    + " <url> <speed>. где speed - byte/s.");
        }
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        if (speed < 1000) {
            throw new IllegalArgumentException("Невалидное значение <speed>. Необходимо установить от 1000 byte/ms.");
        }
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}
