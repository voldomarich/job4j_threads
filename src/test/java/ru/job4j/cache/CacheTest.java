package ru.job4j.cache;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CacheTest {

    @Test
    public void whenAddFind() throws OptimisticException {
        var base = new Base(1,  "Base", 1);
        var cache = new Cache();
        cache.add(base);
        var find = cache.findById(base.id());
        assertEquals("Base", find.get().name());
    }

    @Test
    public void whenAddUpdateFind() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.update(new Base(1, "Base updated", 1));
        var find = cache.findById(base.id());
        assertEquals("Base updated", find.get().name());
    }

    @Test
    public void whenAddDeleteFind() throws OptimisticException {
        var base = new Base(1,   "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.delete(1);
        var find = cache.findById(base.id());
        assertTrue(find.isEmpty());
    }

    @Test
    public void whenMultiUpdateThrowException() throws OptimisticException {
        var base = new Base(1,  "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.update(base);
        assertThrows(OptimisticException.class, () -> cache.update(base));
    }

    @Test
    public void whenMultiUpdateThrowException2() throws OptimisticException {
        var base = new Base(1,  "Base", 1);
        var base2 = new Base(1,  "Base2", 3);
        var cache = new Cache();
        cache.add(base);
        assertThrows(OptimisticException.class, () -> cache.update(base2));
        var find = cache.findById(base.id());
        assertEquals("Base", find.get().name());
        assertEquals(1, find.get().version());
    }
}
