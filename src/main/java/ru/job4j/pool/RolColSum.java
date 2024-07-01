package ru.job4j.pool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RolColSum {

    public static Sums[] sum(int[][] matrix) {
        int n = matrix.length;
        Sums[] sums = new Sums[n];
        int rowSum = 0;
        int colSum = 0;
        for (int row = 0; row < n; row++) {
            for (int column = 0; column < n; column++) {
                rowSum += matrix[row][column];
                colSum += matrix[column][row];
            }
            sums[row] = new Sums(rowSum, colSum);
            rowSum = 0;
            colSum = 0;
        }
        return sums;
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        int n = matrix.length;
        Sums[] sums = new Sums[n];
        Map<Integer, CompletableFuture<Sums[]>> map = new HashMap<>();
        for (int index = 0; index <= n / 2; index++) {
            map.put(index, asyncTask(matrix));
            int k = n - index - 1;
            map.put(k, asyncTask(matrix));
        }
        for (Integer key : map.keySet()) {
            sums[key] = map.get(key).get()[key];
        }
        return sums;
    }

    private static CompletableFuture<Sums[]> asyncTask(int[][] matrix) {
        return CompletableFuture.supplyAsync(() -> sum(matrix));
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int[][] matrix = new int[1000][1000];
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                matrix[i][j] = (i + 1) * (j + 1);
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
    }
}
