package ru.job4j.pool;

import org.junit.jupiter.api.Test;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class RolColSumTest {

    @Test
    public void matrixLengthIs3() {
        int[][] matrix = {new int[]{1,  2,  3},
                          new int[]{7,  8,  9},
                          new int[]{13, 14, 15}};
        Sums[] sums = RolColSum.sum(matrix);
        assertEquals(sums[1].getRowSum(), 24);
        assertEquals(sums[1].getColSum(), 24);
        assertEquals(sums[2].getRowSum(), 42);
    }

    @Test
    public void matrixLengthIs3Async() throws ExecutionException, InterruptedException {
        int[][] matrix = {new int[]{1,  2,  3},
                          new int[]{7,  8,  9},
                          new int[]{13, 14, 15}};
        Sums[] sums = RolColSum.asyncSum(matrix);
        assertEquals(sums[1].getRowSum(), 24);
        assertEquals(sums[1].getColSum(), 24);
        assertEquals(sums[2].getRowSum(), 42);
        assertThrows(IndexOutOfBoundsException.class, () -> sums[3].getRowSum());
    }

    @Test
    public void matrixLengthIs2() {
        int[][] matrix = {new int[]{1,  2},
                          new int[]{7,  8}};
        Sums[] sums = RolColSum.sum(matrix);
        assertEquals(sums[0].getRowSum(), 3);
        assertEquals(sums[1].getColSum(), 10);
        assertEquals(sums[1].getRowSum(), 15);
    }

    @Test
    public void matrixLengthIs2Async() {
        int[][] matrix = {new int[]{1,  2},
                          new int[]{7,  8}};
        Sums[] sums = RolColSum.sum(matrix);
        assertEquals(sums[0].getRowSum(), 3);
        assertEquals(sums[1].getColSum(), 10);
        assertEquals(sums[1].getRowSum(), 15);
        assertThrows(IndexOutOfBoundsException.class, () -> sums[4].getRowSum());
    }

    @Test
    public void whenOk() {
        int[][] matrix = {{1, 1, 1, 1},
                            {1, 1, 1, 1},
                            {1, 1, 1, 1},
                            {1, 1, 1, 1}};
        Sums[] result = RolColSum.sum(matrix);
        Sums s0 = new Sums(4, 4);
        Sums s1 = new Sums(4, 4);
        Sums s2 = new Sums(4, 4);
        Sums s3 = new Sums(4, 4);
        Sums[] expected = {s0, s1, s2, s3};
        assertArrayEquals(expected, result);
    }

    @Test
    public void whenSmall() throws ExecutionException, InterruptedException {
        int[][] matrix = {{1, 1, 1, 1},
                          {1, 1, 1, 1},
                          {1, 1, 1, 1},
                          {1, 1, 1, 1}};
        Sums s0 = new Sums(4, 4);
        Sums s1 = new Sums(4, 4);
        Sums s2 = new Sums(4, 4);
        Sums s3 = new Sums(4, 4);
        Sums[] expected = {s0, s1, s2, s3};
        Sums[] result = RolColSum.asyncSum(matrix);
        assertArrayEquals(expected, result);
    }
}
