package ru.job4j.cash;

import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class CASCountTest {
    private final CASCount casCount = new CASCount();

    @Test
    public void whenIncrement() throws InterruptedException {
        Thread first = new Thread(() -> {
            for (int i = 0; i < 28; i++) {
                casCount.increment();
            }
        }
        );
        Thread second = new Thread(() -> {
            for (int i = 0; i < 59; i++) {
                casCount.increment();
            }
        }
        );
        first.start();
        second.start();
        first.join();
        second.join();
        assertEquals(87, casCount.get());
    }

    @Test
    public void threeThreadIncrement() throws InterruptedException {
        CASCount casCount = new CASCount();
        Thread tread1 = new Thread(
                () -> IntStream.range(83, 87)
                        .forEach(el -> casCount.increment())
        );
        Thread tread2 = new Thread(
                () -> IntStream.range(0, 7)
                        .forEach(el -> casCount.increment())
        );
        Thread tread3 = new Thread(
                () -> IntStream.range(11, 7)
                        .forEach(el -> casCount.increment())
        );
        tread1.start();
        tread1.join();
        tread2.start();
        tread2.join();
        tread3.start();
        tread3.join();
        assertEquals(11, casCount.get());
    }
}
