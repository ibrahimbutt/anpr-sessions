package com.hozah;

import com.hozah.models.Camera;
import com.hozah.models.CarPark;
import com.hozah.models.TrackingRecord;
import com.hozah.models.enums.Direction;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class InputParserTest {
    @Test
    public void shouldThrowIOExceptionWhenGivenIncorrectFormat() {
        InputParser parser = new InputParser();
        List<String> inputLinesWithoutBlank = List.of("Line 1", "Line 2");

        assertThrows(IOException.class, () -> parser.parse(inputLinesWithoutBlank));
    }

    @Test
    public void shouldReturnListOfCarParks_WhenInputIsValid() throws IOException {
        InputParser parser = new InputParser();

        List<String> input = List.of("cp1,cam1,entry,exit", "", "1,cam1,TOWARDS,AAAA");
        List<CarPark> carParks = parser.parse(input);

        assertEquals(1, carParks.size());
        CarPark carPark = carParks.get(0);

        assertEquals("cp1", carPark.id());
        assertTrue(carPark.hasCameraById("cam1"));
    }

    @Test
    public void shouldReturnCarPark_WithCorrectCameraDetails_WhenInputIsValid() throws IOException {
        InputParser parser = new InputParser();

        List<String> input = List.of("cp1,cam1,entry,exit", "", "1,cam1,TOWARDS,AAAA");
        List<CarPark> carParks = parser.parse(input);

        List<Camera> cameras = carParks.stream().flatMap(cp -> cp.cameras().stream()).collect(Collectors.toList());

        assertEquals(1, cameras.size());
        Camera camera = cameras.get(0);

        assertEquals("cam1", camera.id());
        assertEquals(Direction.TOWARDS, camera.entryDirection());
        assertEquals(Direction.AWAY, camera.exitDirection());
    }

    @Test
    public void shouldReturnCarPark_WithCorrectTrackingRecordDetails_WhenInputIsValid() throws IOException {
        InputParser parser = new InputParser();

        List<String> input = List.of("cp1,cam1,entry,exit", "", "1,cam1,TOWARDS,AAAA");
        List<CarPark> carParks = parser.parse(input);

        List<TrackingRecord> trackingRecords = carParks
                .stream()
                .flatMap(cp -> cp.trackingRecords().stream())
                .collect(Collectors.toList());

        assertEquals(1, trackingRecords.size());
        TrackingRecord trackingRecord = trackingRecords.get(0);

        assertEquals(1, trackingRecord.tick());
        assertEquals("cam1", trackingRecord.cameraId());
        assertEquals(Direction.TOWARDS, trackingRecord.direction());
        assertEquals("AAAA", trackingRecord.vrm());
    }
}