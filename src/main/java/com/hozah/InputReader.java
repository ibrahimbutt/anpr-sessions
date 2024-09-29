package com.hozah;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The InputReader class provides functionality to read and process text data from files.
 * It normalizes file paths, reads all lines from the specified file, and removes leading and trailing spaces from each line.
 */
public class InputReader {

    /**
     * Reads the contents of a file specified by the given file path.
     * The method normalizes the path, reads all lines from the file,
     * and removes leading and trailing spaces from each line.
     *
     * @param filePath the path to the file to be read
     * @return a list of strings representing the cleaned lines from the file
     * @throws IOException if an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read
     */
    public List<String> read(String filePath) throws IOException {
        Path path = resolveFilePath(filePath);
        List<String> lines = readLines(path);

        return cleanup(lines);
    }

    private Path resolveFilePath(String filePath) {
        return Paths.get(filePath).toAbsolutePath().normalize();
    }

    private List<String> readLines(Path path) throws IOException {
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new IOException("Error reading file: " + path, e);
        }
    }

    private List<String> cleanup(List<String> lines) {
        return lines.stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }
}
