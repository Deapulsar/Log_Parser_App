package com.example.service;

import com.example.conf.Configuration;
import com.example.model.constants.OperationType;
import com.example.model.impl.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

/**
 * Объект класса предоставляет разнообразный полезный функционал.
 */
public class Util {
    private static Util util;

    private Util() {
    }

    public static Util get() {
        if (util == null) util = new Util();
        return util;
    }

    public long getIdByNameUser(String name) {
        try {
            return Long.parseLong(name.replaceAll("user", ""));
        } catch (Exception ex) {
            throw new RuntimeException("Некорректное имя пользователя");
        }
    }

    public Stream<Path> getPathsToLogFiles(Path path) {

        try {
            return Files.walk(path, 1).
                    filter(x -> x.toString().endsWith(".log"));
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
    }

    public Stream<String> getWordsByFile(Path p) {
        Stream<String> words;
        try {
            words = Stream.of(Files.readString(p).split("\\W"));
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
        return words;
    }

    public Stream<String> getLinesByFile(Path p) {
        Stream<String> lines;
        try {
            lines = Stream.of(Files.readString(p).split("\n")).filter(x->!x.isEmpty());
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
        return lines;
    }

    public Path getPath(String arg) {
        Path path = Paths.get(arg);
        if (!path.toFile().isDirectory()) throw new InvalidPathException(arg, "Некорректный путь к файлам!");
        return path;
    }

    public Date datePart(String line) {
        DateFormat df = this.getDateFormat();
        String part = line.replaceAll("\\[", "").replaceAll("].*$", "");
        try {
            return df.parse(part);
        } catch (ParseException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
    }

    public Double amountPart(String line) {
        String[] strs = line.
                replaceAll("user\\d+", "").
                replaceAll("[^0-9 .]", "").
                trim().
                split(" ");
        String amount = strs[strs.length - 1];
        return Double.parseDouble(amount);
    }

    public OperationType operationTypePart(String line) {
        String type = line.replaceAll("^.*] user\\d+", "").trim().split(" ")[0];
        return OperationType.valueOf(type.toUpperCase());
    }

    public User executorPart(String line) {
        String userName = line.replaceAll("^.*] ", "").trim().split(" ")[0].trim();
        return this.getUserByName(userName);
    }

    public User targetPart(String line){
        String userName = line.replaceAll("^.*(user)","$1").trim().split(" ")[0].trim();
        return this.getUserByName(userName);
    }

    public User getUserByName(String userName) {
        return  Configuration.get().
                getUsers().
                stream().
                filter(x -> x.getId().equals(Util.get().getIdByNameUser(userName))).
                findFirst().get();
    }

    public DateFormat getDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    }

    public String[] getArgs(String[] args){
        if (args.length==0) throw new RuntimeException("Не указа путь!");
        return args;
    }

    public void createOutDir(Path p){
        File f = new File(String.valueOf(p));
        if (f.exists()) {
            try {
                Files.walk(p).forEach(x -> new File(String.valueOf(x)).delete());
            }
            catch (IOException ex) {
                throw new RuntimeException(ex.getMessage(),ex.getCause());
            }
            f.delete();
        }
        f.mkdir();
    }

    public void createLogFileByName(File file){
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrintWriter getPrintWriter(File file){
        try {
            return new PrintWriter(file);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex.getMessage(),ex.getCause());
        }
    }
}
