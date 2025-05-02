package lii.autotexttprocessor.service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileService {

    /**
     * Processes a list of files by reading their content, replacing all occurrences
     * of a regex pattern with a replacement string, and writing the updated content
        * back to the files.
        * @param filePaths The list of file paths to be processed.
     * @param regex The regex pattern to search for.
     * @param replacement The string to replace the regex matches with.
     * @throws IOException If an I/O error occurs.
     */
    public void processFiles(List<String> filePaths, String regex, String replacement) throws IOException {
        for (String filePath : filePaths) {
            String content = readFile(filePath);
            String updatedContent = content.replaceAll(regex, replacement);
            writeFile(filePath, updatedContent);
        }
    }

    /**
     * Reads the content of a file and returns it as a String.
     *  @param filePath The path to the file to be read.
     * @return The content of the file as a String.
     * @throws IOException If an I/O error occurs.
     */
    public String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    /**
     * Writes the given content to a file.
     * @param filePath The path to the file to be written.
     * @param content The content to be written to the file.
     * @throws IOException If an I/O error occurs.
     */
    public void writeFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(filePath), StandardCharsets.UTF_8)) {
            writer.write(content);
        }
    }
}