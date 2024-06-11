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
        var accountFromId = getById(fromId);
        var accountToId = getById(toId);
        boolean validation = fromId != toId && accountFromId.isPresent() && accountToId.isPresent()
                && accountFromId.get().amount() >= amount;
        if (validation) {
            update(new Account(fromId, accountFromId.get().amount() - amount));
            update(new Account(toId, accountToId.get().amount() + amount));
            return true;
        }
        return false;
    }
}
