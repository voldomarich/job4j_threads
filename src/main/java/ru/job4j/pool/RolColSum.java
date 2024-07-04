package ru.job4j.pool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RolColSum {

    public static Sums[] sum(int[][] matrix) {
        int n = matrix.length;
        Sums[] result = new Sums[n];
        for (int row = 0; row < n; row++) {
            result[row] = sumColumn(matrix, row);
        }
        return result;
    }

    public static Sums sumColumn(int[][] matrix, int row) {
        int n = matrix.length;
        int rowSum = 0;
        int colSum = 0;
        for (int column = 0; column < n; column++) {
            rowSum += matrix[row][column];
            colSum += matrix[column][row];
        }
        return new Sums(rowSum, colSum);
    }

    private static CompletableFuture<Sums> asyncTask(int[][] matrix, int row) {
        return CompletableFuture.supplyAsync(() -> sumColumn(matrix, row));
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        int n = matrix.length;
        Sums[] sums = new Sums[n];
        Map<Integer, CompletableFuture<Sums>> map = new HashMap<>();
        for (int index = 0; index <= n / 2; index++) {
            map.put(index, asyncTask(matrix, index));
            int k = n - index - 1;
            map.put(k, asyncTask(matrix, k));
        }
        for (Integer key : map.keySet()) {
            sums[key] = map.get(key).get();
        }
        return sums;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        int[][] matrix = new int[10000][10000];
        int count = 1;
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 10000; j++) {
                matrix[i][j] = count++;
            }
        }

        long start = System.currentTimeMillis();
        Sums[] sum = RolColSum.sum(matrix);
        System.out.println(sum[11].getColSum());
        System.out.println(sum[87].getRowSum());
        System.out.println(System.currentTimeMillis() - start);

        long startAsync = System.currentTimeMillis();
        Sums[] sumAsync = RolColSum.asyncSum(matrix);
        System.out.println(sumAsync[11].getColSum());
        System.out.println(sumAsync[87].getRowSum());
        System.out.println(System.currentTimeMillis() - startAsync);

        long start2 = System.currentTimeMillis();
        Sums[] sum2 = RolColSum.sum(matrix);
        long end2 = System.currentTimeMillis();
        long startAsync2 = System.currentTimeMillis();
        Sums[] sumAsync2 = RolColSum.asyncSum(matrix);
        long endAsync2 = System.currentTimeMillis();
        System.out.println((end2 - start2) + " VS " + (endAsync2 - startAsync2));
    }
}
