package ru.job4j.io;

import java.io.*;
import java.util.logging.Logger;

public final class SaveFile {

    private final File file;
    private static final Logger LOGGER = Logger.getLogger(SaveFile.class.getName());

    public SaveFile(File file) {
        this.file = file;
    }

    public void saveContent(String content) {
        try (FileWriter fileWriter = new FileWriter(file.toString());
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(content);
        } catch (IOException e) {
            LOGGER.severe("Ошибка возникает во время добавления содержимого в файл: " + e.getMessage());
        }
    }
}
