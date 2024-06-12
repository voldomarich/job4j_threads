package ru.job4j.concurrent;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class SimpleBlockingQueueTest {
    SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<Integer>(2);

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
