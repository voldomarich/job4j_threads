package ru.job4j.io;

import java.io.*;
import java.util.function.Predicate;

public final class ParseFile {

    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    private synchronized String readContent(Predicate<Character> filter) {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileInputStream in = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(in)) {
            int data;
            while ((data = bis.read()) != -1) {
                if (filter.test((char) data)) {
                    stringBuilder.append(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public synchronized String getContent() {
        return readContent(s -> true);
    }

    public synchronized String getContentWithoutUnicode() {
        return readContent(s -> s < 0x80);
    }
}
