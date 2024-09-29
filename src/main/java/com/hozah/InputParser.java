package com.hozah;

import com.hozah.models.Camera;
import com.hozah.models.CarPark;
import com.hozah.models.TrackingRecord;
import com.hozah.models.enums.Direction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The InputParser class is responsible for parsing input data related to car parks,
 * cameras, and tracking records, and organizing this data into respective objects.
 * The class provides methods to parse and construct lists of car parks, cameras,
 * and tracking records from the given input.
 */
public class InputParser {

    private static final int CAR_PARK_ID_INDEX = 0;
    private static final int HEADER_START_INDEX = 0;
    private static final int BODY_START_OFFSET = 1;
    private static final int SECTION_COLUMNS = 3;
    private static final int CAM_ENTRY_COLUMN_OFFSET = 1;
    private static final int CAM_EXIT_COLUMN_OFFSET = 2;
    private static final int RECORD_DIRECTION_COLUMN_OFFSET = 1;
    private static final int RECORD_VRM_COLUMN_OFFSET = 2;
    private static final String SEPARATOR = ",";
    private static final String BODY_HEAD_SEPARATOR = "";
    private static final String CAMERA_RAW_ENTRY_VALUE = "entry";
    private static final String CAMERA_RAW_EXIT_VALUE = "exit";

    /**
     * Parses a list of strings representing car park data, builds car park, camera, and tracking record
     * objects, and associates them appropriately.
     *
     * @param input the list of strings representing car park data to parse
     * @return a list of CarPark objects with associated cameras and tracking records
     * @throws IOException if the input list contains no line breaks or encounters issues during parsing
     */
    public List<CarPark> parse(List<String> input) throws IOException {
        validateLineBreak(input);

        int separatorIndex = input.indexOf(BODY_HEAD_SEPARATOR);
        int bodyEndIndex = input.size();

        List<String> header = input.subList(HEADER_START_INDEX, separatorIndex);
        validateHeader(header);

        List<String> body = input.subList(separatorIndex + BODY_START_OFFSET, bodyEndIndex);
        validateBody(body);

        List<CarPark> carParks = buildCarParks(header);
        List<Camera> cameras = buildCameras(header);
        List<TrackingRecord> trackingRecords = buildTrackingRecords(body);

        addCamerasToCarParks(cameras, carParks);
        addTrackingRecordsToCarParks(trackingRecords, carParks);

        return carParks;
    }

    private void validateLineBreak(List<String> input) throws IOException {
        if (input.stream().noneMatch(String::isBlank)) {
            throw new IOException("Error parsing input: Incorrect formatting – no line break found");
        }
    }

    private void validateHeader(List<String> header) throws IOException {
        for (String row : header) {
            String[] sections = row.split(",");
            int lengthExcludingCarParkId = sections.length - 1;

            if (lengthExcludingCarParkId % 3 != 0) {
                throw new IOException("Error parsing input: Incorrect header formatting");
            }
        }
    }

    private void validateBody(List<String> body) throws IOException {
        for (String row : body) {
            String[] sections = row.split(",");
            int lengthExcludingTick = sections.length - 1;

            if (lengthExcludingTick % 3 != 0) {
                throw new IOException("Error parsing input: Incorrect body formatting");
            }
        }
    }

    private List<CarPark> buildCarParks(List<String> lines) {
        List<CarPark> carParks = new ArrayList<>();

        for (var line : lines) {
            var lineSections = line.split(SEPARATOR);
            var carParkId = lineSections[CAR_PARK_ID_INDEX];

            carParks.add(new CarPark(carParkId));
        }

        return carParks;
    }

    private List<Camera> buildCameras(List<String> lines) {
        List<Camera> cameras = new ArrayList<>();

        for (var line : lines) {
            var sections = line.split(SEPARATOR);
            var carParkId = sections[CAR_PARK_ID_INDEX];

            for (int i = 1; i < sections.length; i += SECTION_COLUMNS) {
                String cameraId = sections[i];

                Direction entryDirection = getEntryDirection(sections[i + CAM_ENTRY_COLUMN_OFFSET]);
                Direction exitDirection = getExitDirection(sections[i + CAM_EXIT_COLUMN_OFFSET]);

                cameras.add(new Camera(cameraId, carParkId, entryDirection, exitDirection));
            }
        }

        return cameras;
    }

    private void addCamerasToCarParks(List<Camera> cameras, List<CarPark> carParks) {
        for (CarPark carPark : carParks) {
            carPark.setCameras(
                    cameras.stream().filter(camera -> camera.carParkId().equals(carPark.id())).toList()
            );
        }
    }

    private void addTrackingRecordsToCarParks(List<TrackingRecord> trackingRecords, List<CarPark> carParks) {
        for (CarPark carPark : carParks) {
            carPark.setTrackingRecords(
                    trackingRecords.stream().filter(record -> carPark.hasCameraById(record.cameraId())).toList()
            );
        }
    }

    private List<TrackingRecord> buildTrackingRecords(List<String> lines) {
        List<TrackingRecord> trackingRecords = new ArrayList<>();

        for (String line : lines) {
            String[] sections = line.split(SEPARATOR);
            int tick = Integer.parseInt(sections[0]);

            for (int i = 1; i < sections.length; i += SECTION_COLUMNS) {
                String cameraId = sections[i];
                Direction direction = Direction.valueOf(sections[i + RECORD_DIRECTION_COLUMN_OFFSET].toUpperCase());
                String vrm = sections[i + RECORD_VRM_COLUMN_OFFSET];
                trackingRecords.add(new TrackingRecord(tick, cameraId, direction, vrm));
            }
        }

        return trackingRecords;
    }

    private Direction getEntryDirection(String rawDirection) {
        return rawDirection.equals(CAMERA_RAW_ENTRY_VALUE) ? Direction.TOWARDS : Direction.AWAY;
    }

    private Direction getExitDirection(String rawDirection) {
        return rawDirection.equals(CAMERA_RAW_EXIT_VALUE) ? Direction.AWAY : Direction.TOWARDS;
    }
}
