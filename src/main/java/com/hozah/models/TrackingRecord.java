package com.hozah.models;

import com.hozah.models.enums.Direction;

/**
 * Represents a tracking record of a vehicle in a car park monitored by cameras.
 *
 * @param tick The timestamp of the tracking record.
 * @param cameraId The identifier of the camera that captured the record.
 * @param direction The direction of the vehicle movement relative to the camera (TOWARDS or AWAY).
 * @param vrm The vehicle registration mark (license plate) of the vehicle.
 */
public record TrackingRecord(int tick, String cameraId, Direction direction, String vrm) {
}
