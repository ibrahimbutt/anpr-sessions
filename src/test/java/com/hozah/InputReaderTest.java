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
    void shouldReadInputFile() throws IOException {
        Path path = Paths.get("src/test/resources/001_test_input.txt").toAbsolutePath().normalize();
        List<String> result = inputReader.read(path.toString());

        assertEquals(List.of(
                "cp1,cam1,entry,exit,cam2,exit,entry",
                "cp2,cam3,exit,entry",
                "",
                "0,cam2,away,TEST2",
                "1,cam1,towards,TEST1,cam3,away,TEST3",
                "2,cam1,away,TEST1,cam2,towards,TEST2",
                "3,cam3,towards,TEST3"
        ), result);
    }

    @Test
    void shouldThrowIOException_whenGivenInvalidFileName() {
        String emptyFileName = "";

        assertThrows(IOException.class, () -> inputReader.read(emptyFileName));
    }

    @Test
    void shouldThrowIOException_whenFileDoesNotExist() {
        String nonExistingFilePath = "/path/to/nonExistingFile.txt";

        assertThrows(IOException.class, () -> inputReader.read(nonExistingFilePath));
    }
}