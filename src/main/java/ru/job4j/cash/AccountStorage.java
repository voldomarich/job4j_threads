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
        return accounts.putIfAbsent(account.id(), account) == null;
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
        boolean validation =  getById(fromId).isPresent() && getById(toId).isPresent();
        int amountToId = getById(toId).get().amount();
        int amountFromId = getById(fromId).get().amount();
        boolean t1 = update(new Account(toId, amountToId + amount));
        boolean t2 = update(new Account(fromId, amountFromId - amount));
        return validation && t1 && t2;
    }
}
