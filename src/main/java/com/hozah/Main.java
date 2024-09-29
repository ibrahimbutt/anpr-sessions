package com.hozah;

import com.hozah.models.Camera;
import com.hozah.models.CarPark;
import com.hozah.models.Session;
import com.hozah.models.TrackingRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static final String DEFAULT_FILE_PATH = "src/main/resources/input.txt";
    private static final InputReader inputReader = new InputReader();
    private static final InputParser inputParser = new InputParser();
    private static final SessionPrinter sessionPrinter = new SessionPrinter();
    private static final TrackingRecordMatcher trackingRecordMatcher = new TrackingRecordMatcher();

    public static void main(String[] args) {
        String relativeFilePath;
        if (args.length != 1) {
            relativeFilePath = DEFAULT_FILE_PATH;
        } else {
            relativeFilePath = args[0];
        }

        List<String> inputLines = readInput(relativeFilePath);
        List<CarPark> carParks = parseInput(inputLines);

        List<TrackingRecord> allTrackingRecords = buildAllTrackingRecordsList(carParks);
        Map<String, Camera> allCamerasIdMap = buildAllCamerasMap(carParks);

        List<Session> allSessions = trackingRecordMatcher.match(allTrackingRecords, allCamerasIdMap);
        sessionPrinter.print(allSessions);
    }


    private static List<TrackingRecord> buildAllTrackingRecordsList(List<CarPark> carParks) {
        List<TrackingRecord> trackingRecords = new ArrayList<>();

        for (CarPark carPark : carParks) {
            trackingRecords.addAll(carPark.trackingRecords());
        }

        return trackingRecords;
    }

    private static Map<String, Camera> buildAllCamerasMap(List<CarPark> carParks) {
        List<Camera> cameras = new ArrayList<>();

        for (CarPark carPark : carParks) {
            cameras.addAll(carPark.cameras());
        }

        return buildCameraMap(cameras);
    }

    private static List<String> readInput(String relativeFilePath) {
        List<String> inputLines;
        try {
            inputLines = inputReader.read(relativeFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return inputLines;
    }

    private static List<CarPark> parseInput(List<String> inputLines) {
        List<CarPark> carParks;
        try {
            carParks = inputParser.parse(inputLines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return carParks;
    }

    private static Map<String, Camera> buildCameraMap(List<Camera> cameras) {
        return cameras
                .stream()
                .collect(Collectors.toMap(
                        Camera::id,
                        camera -> camera
                ));
    }
}
