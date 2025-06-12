package com.example.conf;

import com.example.model.Operation;
import com.example.model.impl.User;
import com.example.service.Parser;
import com.example.service.Util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Объект класс выполняет загрузку основных компонентов приложения.
 */
public class Configuration {
    private static Configuration configuration;

    private final Util util;
    private final List<User> users;
    private final List<Operation> operations;

    private Configuration() {
        this.util = Util.get();
        this.users = new ArrayList<>();
        this.operations = new ArrayList<>();
    }

    public List<User> getUsers() {
        return this.users;
    }

    public static Configuration get() {
        if (configuration == null) configuration = new Configuration();
        return configuration;
    }

    /**
     * Загрузка основных компонентов приложения.
     * @param args - аргументы, указанные при запуске приложения.
     */
    public void load(String[] args) {
        Path path = this.util.getPath(this.util.getArgs(args)[0]);
        Parser.get(path, Path.of("/transactions_by_users"), this.users, this.operations);
        this.loadUsers(path);
        this.loadOperations(path);
    }

    /**
     * Сканирование из логов имен всех возможножных пользователей и добавление их в список.
     * @param path - путь к логам.
     */
    private void loadUsers(Path path) {
        this.util.getPathsToLogFiles(path).
                flatMap(this.util::getWordsByFile).
                filter(x -> x.toLowerCase().startsWith("user")).
                forEach(x -> {
                    User user = new User(x);
                    if (!this.users.contains(user)) this.users.add(user);
                });
    }
    /**
     * Сканирование из логов всех операций и добавление их в список.
     * @param path - путь к логам.
     */
    private void loadOperations(Path path) {
        this.util.getPathsToLogFiles(path).
        flatMap(this.util::getLinesByFile).
        forEach(x->this.operations.add(Operation.get(x)));
    }
}
