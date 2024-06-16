package ru.job4j.concurrent;

import org.junit.Test;

import java.util.concurrent.CopyOnWriteArrayList;

import static org.assertj.core.api.Assertions.*;

public class SimpleBlockingQueueTest {

    final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
    final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(14);

    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {

        Thread producer = new Thread(
                () -> {
                    for (int i = 10; i < 14; i++) {
                        try {
                            queue.offer(i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                        try {
                            buffer.add(queue.poll());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertThat(buffer).isNotEmpty();
        assertThat(buffer).containsExactly(10, 11, 12, 13);
    }

    @Test
    public void whenOffer() throws InterruptedException {

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 50; i++) {
                    queue.offer(i);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 48; i++) {
                    queue.poll();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(queue.poll()).isEqualTo(48);
    }
}
