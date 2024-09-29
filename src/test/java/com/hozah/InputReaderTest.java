package com.hozah;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InputReaderTest {

    private final InputReader inputReader = new InputReader();

    @Test
    void testRead_validFilePath() throws IOException {
        Path path = Paths.get("src/main/resources/testFile.txt").toAbsolutePath().normalize();
        Files.write(path, List.of(" test line 1 ", " test line 2 "));

        List<String> result = inputReader.read(path.toString());

        assertEquals(List.of("test line 1", "test line 2"), result);

        Files.delete(path);
    }

    @Test
    void testRead_emptyFileName() {
        String emptyFileName = "";

        assertThrows(IOException.class, () -> inputReader.read(emptyFileName));
    }

    @Test
    void testRead_nonExistingFile() {
        String nonExistingFilePath = "/path/to/nonExistingFile.txt";

        assertThrows(IOException.class, () -> inputReader.read(nonExistingFilePath));
    }
}