package ru.job4j.cash;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountStorageTest {

    @Test
    void whenAdd() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Не найден аккаунт по id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(100);
    }

    @Test
    void whenUpdate() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.update(new Account(1, 200));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Не найден аккаунт по id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(200);
    }

    @Test
    void whenDelete() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.delete(1);
        assertThat(storage.getById(1)).isEmpty();
    }

    @Test
    void whenTransfer() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        storage.transfer(1, 2, 100);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Не найден аккаунт по id = 1"));
        var secondAccount = storage.getById(2)
                .orElseThrow(() -> new IllegalStateException("Не найден аккаунт по id = 2"));
        assertThat(firstAccount.amount()).isEqualTo(0);
        assertThat(secondAccount.amount()).isEqualTo(200);
    }

    @Test
    void whenNotEnoughMoneyToTransfer() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> storage.transfer(1, 2, 110));
        assertEquals("Баланс получателя меньше суммы перевода amount", exception.getMessage());
    }

    @Test
    void whenAccountDoesNotExistInTransfer1() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> storage.transfer(3, 5, 100));
        assertEquals("Получателя либо отправителя нет в базе данных", exception.getMessage());
    }

    @Test
    void whenAccountIsDuplicatedInTransfer() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> storage.transfer(1, 1, 100));
        assertEquals("Отправитель отправляет себе же", exception.getMessage());
    }
}
