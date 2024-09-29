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
    void testPrint_SessionsWithValidVrm() {
        var session1 = new Session("AB12CDF", 0, 2);
        var session2 = new Session("GH34IJK", 1, 3);
        List<Session> sessions = Arrays.asList(session1, session2);

        var sessionPrinter = new SessionPrinter();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        sessionPrinter.print(sessions);

        String expectedOutput = "0\n" +
                "1\n" +
                "2,AB12CDF,0,2\n" +
                "3,GH34IJK,1,3\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void testPrint_SessionsWithIllegalCharactersInVrm() {
        var session1 = new Session("AB12i1B0", 0, 2);
        var session2 = new Session("GH34LKB0", 1, 3);
        List<Session> sessions = Arrays.asList(session1, session2);

        var sessionPrinter = new SessionPrinter();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        sessionPrinter.print(sessions);

        String expectedOutput = "0\n" +
                "1\n" +
                "2,AB12LLBO,0,2\n" +
                "3,GH34LKB0,1,3\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void testPrint_SessionsWithNullVrm() {
        var session1 = new Session(null, 0, 2);
        var session2 = new Session("GH34LJB0", 1, 3);
        List<Session> sessions = Arrays.asList(session1, session2);

        var sessionPrinter = new SessionPrinter();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        sessionPrinter.print(sessions);

        String expectedOutput = "0\n" +
                "1\n" +
                "2,,0,2\n" +
                "3,GH34LJB0,1,3\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}