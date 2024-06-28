package ru.job4j.pool;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class ParallelSearchIndexTest {
    @Test
    public void differentDataTypes() {
        String[] stringArray = {"catalonia", "scotland", "hawaii", "bavaria", "umbria"};
        int index = ParallelSearchIndex.findIndex(stringArray, "scotland");
        assertEquals(1, index);
    }

    @Test
    public void differentDataTypes1() {
        String[] stringArray = {"catalonia", "scotland", "hawaii", "bavaria", "umbria"};
        int index = ParallelSearchIndex.findIndex(stringArray, "lombardia");
        assertEquals(-1, index);
    }

    @Test
    public void differentDataTypes2() {
        Double[] doubleArray = {1.5, 2.0, 3.3, 4.2, 5.0};
        int index = ParallelSearchIndex.findIndex(doubleArray, 3.3);
        assertEquals(2, index);
    }

    @Test
    public void smallArrayLinearSearch() {
        Integer[] smallArray = {1, 2, 3, 4, 5};
        int index = ParallelSearchIndex.findIndex(smallArray, 3);
        assertEquals(2, index);
    }

    @Test
    public void largeArrayParallelSearch() {
        Integer[] largeArray = new Integer[100];
        largeArray[0] = 0;
        for (int i = 1; i < 100; i++) {
            largeArray[i] = i * 4;
        }
        int index = ParallelSearchIndex.findIndex(largeArray, 120);
        assertEquals(30, index);
    }

    @Test
    public void elementNotFound() {
        Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int index = ParallelSearchIndex.findIndex(array, 11);
        assertEquals(-1, index);
    }

    @Test
    public void whenSearchLast() {
        Integer[] array = new Integer[15];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 10;
        }
        int searchElement = 24;
        assertThat(ParallelSearchIndex.findIndex(array, searchElement)).isEqualTo(14);
    }
}
