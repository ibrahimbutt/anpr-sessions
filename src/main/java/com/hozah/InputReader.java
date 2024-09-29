package com.hozah;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class InputProcessor {

    public List<String> read(String relativeFilePath) throws IOException {
        Path path = resolveFilePath(relativeFilePath);
        List<String> lines = readLines(path);
        return sanitize(lines);
    }

    private Path resolveFilePath(String relativeFilePath) {
        return Paths.get(relativeFilePath).toAbsolutePath().normalize();
    }

    private List<String> readLines(Path path) throws IOException {
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new IOException("Error reading file: " + path, e);
        }
    }

    private List<String> sanitize(List<String> lines) {
        // TODO: Validate VRM's
        return lines.stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }
}
