package com.hozah;

import com.hozah.models.Session;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The SessionPrinter class provides functionality to print session information from a List of Session objects.
 * It formats the session details, validates and fixes VRM values, groups sessions by their exit tick,
 * and determines the maximum tick to print a detailed report of each session per tick.
 */
public class SessionPrinter {

    private static final String SEPARATOR = ",";
    private static final String ILLEGAL_CHARACTERS = "l10b";

    /**
     * Prints session information for each tick from the given list of sessions.
     * For each tick from 0 up to the maximum exit tick found in the sessions list,
     * it prints the tick number followed by the session details if any session exits at that tick.
     * Otherwise, it prints just the tick number.
     *
     * @param sessions A list of Session objects, each representing a session with its VRM, entry tick, and exit tick.
     */
    public void print(List<Session> sessions) {
        int maxTick = findMaxTick(sessions);
        var groupedSessions = groupSessions(sessions);

        for (int tick = 0; tick <= maxTick; tick++) {
            if (groupedSessions.containsKey(tick)) {
                StringBuilder line = buildOutputRow(tick, groupedSessions);
                System.out.println(line.toString());
            } else {
                System.out.println(tick);
            }
        }
    }

    private StringBuilder buildOutputRow(int tick, Map<Integer, List<Session>> groupedSessions) {
        StringBuilder line = new StringBuilder();
        line.append(tick);

        for (Session session : groupedSessions.get(tick)) {
            buildSessionColumns(session, line);
        }
        return line;
    }

    private void buildSessionColumns(Session session, StringBuilder line) {

        String testVrm = session.vrm();
        String vrm = isValidVrm(testVrm) ? testVrm : fixVrm(testVrm);

        line.append(SEPARATOR)
                .append(vrm)
                .append(SEPARATOR)
                .append(session.entryTick())
                .append(SEPARATOR)
                .append(session.exitTick());
    }

    private Map<Integer, List<Session>> groupSessions(List<Session> sessions) {
        return sessions.stream()
                .collect(Collectors.groupingBy(Session::exitTick));
    }

    private boolean isValidVrm(String vrm) {
        String lowerCaseVrm = vrm.toLowerCase();

        for (char ch : ILLEGAL_CHARACTERS.toCharArray()) {
            if (lowerCaseVrm.indexOf(ch) >= 0) {
                return false;
            }
        }
        return true;
    }

    private String fixVrm(String input) {

        // TODO: Remove hardcoded characters
        String result = input.toUpperCase();
        result = result.replaceAll("[I1]", "L");
        result = result.replaceAll("[0B]", "O");

        return result;
    }

    private int findMaxTick(List<Session> sessions) {
        return sessions.stream()
                .map(Session::exitTick)
                .max(Integer::compare)
                .orElse(0);
    }
}
