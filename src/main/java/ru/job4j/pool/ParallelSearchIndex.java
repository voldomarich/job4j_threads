package ru.job4j.pool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelSearchIndex<T> extends RecursiveTask<Integer> {

    private final T[] array;
    private final int from;
    private final int to;
    private final T element;

    public ParallelSearchIndex(T[] array, int from, int to, T element) {
        this.array = array;
        this.from = from;
        this.to = to;
        this.element = element;
    }

    @Override
    protected Integer compute() {
        if (to - from < 10) {
            return search();
        }
        int middle = (from + to) / 2;
        ParallelSearchIndex<T> left = new ParallelSearchIndex<>(array, from, middle, element);
        ParallelSearchIndex<T> right = new ParallelSearchIndex<>(array, middle + 1, to, element);
        left.fork();
        right.fork();
        int leftResult = left.join();
        int rightResult = right.join();
        return Math.max(leftResult, rightResult);
    }

    private int search() {
        int result = -1;
        for (int i = from; i <= to; i++) {
            if (array[i].equals(element)) {
                result = i;
                break;
            }
        }
        return result;
    }

    public static <T> int findIndex(T[] array, T element) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new ParallelSearchIndex<>(array, 0, array.length - 1, element));
    }
}
