package com.example.service;

import com.example.model.Operation;
import com.example.model.impl.Transfer;
import com.example.model.impl.User;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Объект класса проводит операции над пользователями и сохраняет результаты в новую директорию.
 */
public class Parser {
    private static Parser parser;

    private final Path dir;
    private final Path loadPath;
    private final List<User> users;
    private final List<Operation> operations;

    private Parser(Path dir, Path path, List<User> users, List<Operation> operations) {
        this.dir = dir;
        this.loadPath = path;
        this.users = users;
        this.operations = operations;
    }

    public static Parser get() {
        return Objects.requireNonNull(parser);
    }

    public static Parser get(Path dir, Path load, List<User> users, List<Operation> operations){
        if (parser!=null) return parser;
        parser = new Parser(dir, load, users, operations);
        return parser;
    }

    /**
     * Проводит операции, сортирую их по дате, над пользователями и сохраняет результаты в новую директорию.
     */
    public void parse(){
        this.operations.sort(Comparator.comparing(Operation::getDate));
        this.operations.forEach(x->{
            x.execute();
            x.getExecutor().addOperation(x);
            if (x.getClass() == Transfer.class) ((Transfer) x).getTarget().addOperation(x);
        });
        Path out = Paths.get(this.dir.toString(),this.loadPath.toString());
        Util util = Util.get();
        util.createOutDir(out);
        users.forEach(x->{
            x.getOperations().sort(Comparator.comparing(Operation::getDate));
            File file = new File(String.valueOf(Path.of(String.valueOf(out),""+x.getName()+".log")));
            util.createLogFileByName(file);
            PrintWriter pw = util.getPrintWriter(file);
            x.getOperations().forEach(op->{
                if (op.getClass()!=Transfer.class) {
                    pw.write(op + "\n");
                }
                else{
                    if (op.getExecutor()==x) pw.write(op.toString()+"\n");
                    else pw.write(((Transfer) op).toStringSwapUsers()+"\n");
                }
            });
            pw.write(x.getFinalBalance()+"\n");
            pw.close();
        });
    }
}
