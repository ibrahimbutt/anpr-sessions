package com.hozah;

import com.hozah.models.Session;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SessionPrinterTest {

    @Test
    void shouldPrintExpectedOut() {
        var session1 = new Session("TEST2", 0, 2);
        var session2 = new Session("TEST3", 1, 3);
        List<Session> sessions = Arrays.asList(session1, session2);

        var sessionPrinter = new SessionPrinter();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        sessionPrinter.print(sessions);

        String expectedOutput = "0\n" +
                "1\n" +
                "2,TEST2,0,2\n" +
                "3,TEST3,1,3\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void shouldPrintExpectedOutput_whenGivenIllegalVrmCharacter() {
        var session1 = new Session("TEST1", 0, 2);
        var session2 = new Session("TESTB", 1, 3);
        List<Session> sessions = Arrays.asList(session1, session2);

        var sessionPrinter = new SessionPrinter();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        sessionPrinter.print(sessions);

        String expectedOutput = "0\n" +
                "1\n" +
                "2,TESTL,0,2\n" +
                "3,TESTO,1,3\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void shouldPrintExpectedOutput_whenGivenMultipleIllegalVrmCharacters() {
        var session1 = new Session("TEST10", 0, 2);
        var session2 = new Session("TESTBI", 1, 3);
        List<Session> sessions = Arrays.asList(session1, session2);

        var sessionPrinter = new SessionPrinter();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        sessionPrinter.print(sessions);

        String expectedOutput = "0\n" +
                "1\n" +
                "2,TESTLO,0,2\n" +
                "3,TESTOL,1,3\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}