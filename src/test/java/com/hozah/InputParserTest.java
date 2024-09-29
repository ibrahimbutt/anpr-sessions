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
    public void parse_ShouldThrowException_WhenInputIsNull() {
        InputParser parser = new InputParser();

        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_ShouldThrowException_WhenNoLineBreakFound_InTheInput() {
        InputParser parser = new InputParser();

        assertThrows(IOException.class, () -> parser.parse(List.of("Sample Data")));
    }

    @Test
    public void parse_ShouldReturnListOfCarParks_WhenInputIsValid() throws IOException {
        InputParser parser = new InputParser();

        List<String> input = List.of("CP1,cam1,entry,exit", "", "1,cam1,TOWARDS,au8 65k");
        List<CarPark> carParks = parser.parse(input);

        assertEquals(1, carParks.size());
        CarPark carPark = carParks.get(0);

        assertEquals("CP1", carPark.id());
        assertTrue(carPark.hasCameraById("cam1"));
    }

    @Test
    public void parse_ShouldReturnCarPark_WithCorrectCameraDetails_WhenInputIsValid() throws IOException {
        InputParser parser = new InputParser();

        List<String> input = List.of("CP1,cam1,entry,exit", "", "1,cam1,TOWARDS,au8 65k");
        List<CarPark> carParks = parser.parse(input);

        List<Camera> cameras = carParks.stream().flatMap(cp -> cp.cameras().stream()).collect(Collectors.toList());

        assertEquals(1, cameras.size());
        Camera camera = cameras.get(0);

        assertEquals("cam1", camera.id());
        assertEquals(Direction.TOWARDS, camera.entryDirection());
        assertEquals(Direction.AWAY, camera.exitDirection());
    }

    @Test
    public void parse_ShouldReturnCarPark_WithCorrectTrackingRecordDetails_WhenInputIsValid() throws IOException {
        InputParser parser = new InputParser();

        List<String> input = List.of("CP1,cam1,entry,exit", "", "1,cam1,TOWARDS,au8 65k");
        List<CarPark> carParks = parser.parse(input);

        List<TrackingRecord> trackingRecords = carParks.stream().flatMap(cp -> cp.trackingRecords().stream()).collect(Collectors.toList());

        assertEquals(1, trackingRecords.size());
        TrackingRecord trackingRecord = trackingRecords.get(0);

        assertEquals(1, trackingRecord.tick());
        assertEquals("cam1", trackingRecord.cameraId());
        assertEquals(Direction.TOWARDS, trackingRecord.direction());
        assertEquals("au8 65k", trackingRecord.vrm());
    }
}