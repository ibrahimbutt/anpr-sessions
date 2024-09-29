package com.hozah;

import com.hozah.models.Camera;
import com.hozah.models.Session;
import com.hozah.models.TrackingRecord;
import com.hozah.models.enums.Direction;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrackingRecordMatcherTest {

    @Test
    void shouldMatchTrackingRecords() {
        List<TrackingRecord> records = Arrays.asList(
                new TrackingRecord(1, "cam1", Direction.TOWARDS, "vrm1"),
                new TrackingRecord(2, "cam1", Direction.AWAY, "vrm1"),
                new TrackingRecord(3, "cam2", Direction.TOWARDS, "vrm2"),
                new TrackingRecord(4, "cam2", Direction.AWAY, "vrm2")
        );

        Map<String, Camera> cameras = new HashMap<String, Camera>() {
            {
                put("cam1", new Camera("cam1", "cp1"));
                put("cam2", new Camera("cam2", "cp2"));
            }
        };

        TrackingRecordMatcher matcher = new TrackingRecordMatcher();

        List<Session> sessions = matcher.match(records, cameras);

        assertEquals(2, sessions.size());

        assertEquals(new Session("vrm1", 1, 2), sessions.get(1));
        assertEquals(new Session("vrm2", 3, 4), sessions.get(0));
    }

    @Test
    void shouldIgnoreFirstEntryRecord_whenFollowedByAnotherEntryRecord() {
        List<TrackingRecord> records = Arrays.asList(
                new TrackingRecord(1, "cam1", Direction.TOWARDS, "vrm1"),
                new TrackingRecord(2, "cam1", Direction.TOWARDS, "vrm1"),
                new TrackingRecord(3, "cam1", Direction.AWAY, "vrm1"),
                new TrackingRecord(4, "cam2", Direction.TOWARDS, "vrm2"),
                new TrackingRecord(5, "cam2", Direction.AWAY, "vrm2")
        );

        Map<String, Camera> cameras = new HashMap<String, Camera>() {
            {
                put("cam1", new Camera("cam1", "cp1"));
                put("cam2", new Camera("cam2", "cp2"));
            }
        };

        TrackingRecordMatcher matcher = new TrackingRecordMatcher();
        List<Session> sessions = matcher.match(records, cameras);
        assertEquals(2, sessions.size());
        assertEquals(new Session("vrm1", 2, 3), sessions.get(1));
        assertEquals(new Session("vrm2", 4, 5), sessions.get(0));
    }

}