package ru.job4j.concurrent;

/* Главная нить записывает новое значение переменной в кеш процессора,
а дополнительная нить будет продолжать читать переменную instance из регистра.
Возникает ошибка видимости (share visibility problem).
Чтобы ее решить, можно использовать облегчённый механизм синхронизации - volatile.
Его можно использовать только в том случае, когда общий ресурс
не обновляется в зависимости от своего состояния.
У нас метод getInstance() защищен от атомарности
явным указанием монитора, поэтому общий ресурс независим, смело используем ключевое слово volatile
и, тем самым, решаем ошибку видимости */
public class DCLSingleton {
    private static volatile DCLSingleton instance;

    public static DCLSingleton getInstance() {
        if (instance == null) {
            synchronized (DCLSingleton.class) {
                if (instance == null) {
                    instance = new DCLSingleton();
                }
            }
        }
        return instance;
    }

    private DCLSingleton() {
    }
}
