package ru.job4j.email;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@ThreadSafe
public class EmailNotification {
    @GuardedBy("this")
    private  final ExecutorService pool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());

    public synchronized void emailTo(User user) {
        String subject = String.format("Notification %s to %s", user.username(), user.email());
        String body = String.format("Add a new event to %s", user.email());
        pool.submit(() -> send(subject, body, user.email()));
    }

    private synchronized void send(String subject, String body, String email) {
    }

    private synchronized void close() {
        pool.shutdown();
        while (!pool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
