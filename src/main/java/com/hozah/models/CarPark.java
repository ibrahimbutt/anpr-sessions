package com.hozah.models;

import java.util.ArrayList;
import java.util.List;

/**
 * The CarPark class represents a parking facility with cameras that monitor vehicle movements
 * and track records.
 * It provides methods to access and manage its cameras and tracking records.
 */
public class CarPark {

    private final String id;
    private List<Camera> cameras;
    private List<TrackingRecord> trackingRecords = new ArrayList<>();

    public CarPark(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public List<Camera> cameras() {
        return cameras;
    }

    public void setCameras(List<Camera> cameras) {
        this.cameras = cameras;
    }

    public boolean hasCameraById(String cameraId) {
        return cameras.stream()
                .anyMatch(camera -> camera.id().equals(cameraId));
    }

    public List<TrackingRecord> trackingRecords() {
        return trackingRecords;
    }

    public void setTrackingRecords(List<TrackingRecord> trackingRecords) {
        this.trackingRecords = trackingRecords;
    }
}
