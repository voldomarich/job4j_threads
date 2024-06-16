package ru.job4j.concurrent;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

 /* Блокирующая очередь, ограниченная по размеру, основанная на шаблоне Producer Consumer */
@ThreadSafe
public class SimpleBlockingQueue<T> {
    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;

    public SimpleBlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    /* Метод добавления элемента в очередь */
    public void offer(T value) throws InterruptedException {
        synchronized (queue) {
            while (queue.size() == capacity) {
                queue.wait();                   /* Ожидание, если размер очереди равен вмещающей способности объекта */
            }
            queue.add(value);       /* Добавление элемента в очередь */
            queue.notifyAll();      /* Перевод всех нитей в активное систояние RUNNABLE */
        }
    }

    /* Метод извлечения элемента из очереди */
    public T poll() throws InterruptedException {
        T result;
        synchronized (queue) {
            while (queue.isEmpty()) {
                queue.wait();                /* Ожидание, если очередь пуста */
            }
            result = queue.poll();          /* Извлечение элемента из очереди */
            queue.notifyAll();              /* Перевод всех нитей в активное систояние RUNNABLE */
        }
        return result;
    }

     public boolean isEmpty() {
         return queue.isEmpty();
     }
}
