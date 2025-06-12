package com.example;

import com.example.conf.Configuration;
import com.example.service.Parser;

/**
 * Приложение для обработки и анализа логов переводов между пользователями.
 *
 * Приложенние сканирует директорию на наличие log-файлов, извлекает из них операции, присваивает эти операции
 * пользователям и выполняет их. Результаты проведения операции по каждому пользователю записывает в виде log-файлов
 * в новую директорию в том месте, где находятся исходные log-файлы.
 *
 * В качестве аргумента запуска приложение указывается местонахождение log-файлов.
 */
public class LogParsingApp {

    public static void main(String[] args){
        Configuration.get().load(args);
        Parser.get().parse();
    }
}
