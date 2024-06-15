package ru.job4j.concurrent;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class CountBarrier {

    @GuardedBy("monitor")
    private final Object monitor = this;
    private final int total;
    private int count = 0;

    public CountBarrier(final int total) {
        this.total = total;
    }

    public void count() {
        synchronized (monitor) {
            count++;
            monitor.notifyAll();
        }
    }

    public void await() {
        synchronized (monitor) {
            while (count < total && !Thread.currentThread().isInterrupted()) {
                try {
                    monitor.wait();
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {
        CountBarrier countBarrier = new CountBarrier(6);
        Thread first = new Thread(() -> {
            countBarrier.await();
            System.out.printf("Total=%s. Count=%s. %s. %s  %n",
                    countBarrier.total,
                    countBarrier.count,
                    Thread.currentThread().getName(),
                    Thread.currentThread().getState());
        }, "first");

        Thread second = new Thread(() -> {
            for (int i = 0; i < 12; i++) {
                countBarrier.count();
                System.out.printf("Total=%s. Count=%s. i=%s. %s. %s. %s. %s %n",
                        countBarrier.total,
                        countBarrier.count,
                        i,
                        first.getName(),
                        first.getState(),
                        Thread.currentThread().getName(),
                        Thread.currentThread().getState());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "second");

        first.start();
        second.start();
    }
}
