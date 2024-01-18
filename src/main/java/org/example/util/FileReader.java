package org.example.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * класс для чтения содержимого файла
 */
public class FileReader {
    /**
     * читает содержимое файла по указанному пути
     *
     * @param filePath путь к файлу, который необходимо прочитать
     * @return строка, в которой находится содержимое файла
     * @throws IOException ошибка ввода-вывода при чтении файла
     */
    public static String readFile(String filePath) throws IOException {
        byte[] encodedBytes = Files.readAllBytes(Path.of(filePath));
        String fileContent = new String(encodedBytes, "UTF-8");

        return fileContent;
    }
}
