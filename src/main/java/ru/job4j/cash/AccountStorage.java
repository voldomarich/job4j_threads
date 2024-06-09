package ru.job4j.cash;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {
    @GuardedBy("this")
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        accounts.put(account.id(), account);
        return accounts.containsKey(account.id());
    }

    public synchronized boolean update(Account account) {
        return accounts.replace(account.id(), account) != null;
    }

    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        validationForTransfert(fromId, toId, amount);
        boolean t1 = update(new Account(toId, getById(toId).get().amount() + amount));
        boolean t2 = update(new Account(fromId, getById(fromId).get().amount() - amount));
        return t1 && t2;
    }

    private synchronized void validationForTransfert(int fromId, int toId, int amount) {
        if (getById(fromId).isEmpty() && getById(toId).isEmpty()) {
            throw new IllegalArgumentException("Получателя либо отправителя нет в базе данных");
        }
        if (getById(fromId).get().amount() < amount) {
            throw new IllegalArgumentException("Баланс получателя меньше суммы перевода amount");
        }
        if (getById(fromId).get().id() == getById(toId).get().id()) {
            throw new IllegalArgumentException("Отправитель отправляет себе же");
        }
    }
}
