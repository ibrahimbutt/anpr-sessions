package com.hozah;

import com.hozah.models.Camera;
import com.hozah.models.Session;
import com.hozah.models.TrackingRecord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The TrackingRecordMatcher class is responsible for matching tracking records of vehicles
 * with their corresponding sessions. A session is defined as the period between the entry and
 * exit ticks of a vehicle identified by its Vehicle Registration Mark (VRM).
 */
public class TrackingRecordMatcher {
    public List<Session> match(List<TrackingRecord> records, Map<String, Camera> cameras) {
        List<Session> sessions = new ArrayList<>();
        Map<String, List<TrackingRecord>> vrmRecordMap = groupAndSortRecordsByVrm(records);


        // TODO: Consider refactor – too much nesting
        for (Map.Entry<String, List<TrackingRecord>> entry : vrmRecordMap.entrySet()) {
            String vrm = entry.getKey();
            List<TrackingRecord> recordsForVrm = entry.getValue();
            Map<String, List<TrackingRecord>> recordsByCamera = groupAndSortRecordsByCamera(recordsForVrm);

            for (List<TrackingRecord> sortedRecordsForCamera : recordsByCamera.values()) {
                Integer lastEntryTick = null;

                for (TrackingRecord record : sortedRecordsForCamera) {
                    Camera camera = cameras.get(record.cameraId());

                    if (record.direction() == camera.entryDirection()) {
                        lastEntryTick = record.tick();
                    } else if (record.direction() == camera.exitDirection() && lastEntryTick != null) {
                        sessions.add(new Session(vrm, lastEntryTick, record.tick()));
                        lastEntryTick = null;
                    }
                }
            }
        }
        return sessions;
    }

    private Map<String, List<TrackingRecord>> groupAndSortRecordsByVrm(List<TrackingRecord> records) {
        return records.stream()
                .collect(Collectors.groupingBy(TrackingRecord::vrm,
                        Collectors.collectingAndThen(Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparingInt(TrackingRecord::tick))
                                        .collect(Collectors.toList()))));
    }

    private Map<String, List<TrackingRecord>> groupAndSortRecordsByCamera(List<TrackingRecord> records) {
        return records.stream()
                .collect(Collectors.groupingBy(
                        TrackingRecord::cameraId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparingInt(TrackingRecord::tick))
                                        .toList()
                        )
                ));
    }
}
