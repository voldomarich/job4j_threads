package ru.job4j.cache;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        return memory.putIfAbsent(model.id(), model) == null;
    }

    public boolean update(Base model) {
        return memory.computeIfPresent(model.id(), (key, currentCacheBase) -> {
                    if (currentCacheBase.version() != model.version()) {
                        throw new OptimisticException("Версия кэша "
                                + "обновляемой модели не совпадает с версией кэша новой модели");
                        }
                    return new Base(key, model.name(), currentCacheBase.version() + 1);
                }
        ) != null;
    }

    public void delete(int id) {
        memory.remove(id);
    }

    public Optional<Base> findById(int id) {
        return Stream.of(memory.get(id))
                .filter(Objects::nonNull)
                .findFirst();
    }
}
