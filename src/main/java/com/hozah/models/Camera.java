package com.hozah.models;

import com.hozah.models.enums.Direction;

/**
 * Represents a camera installed in a car park to monitor vehicle movements.
 *
 * @param id The unique identifier of the camera.
 * @param carParkId The identifier of the car park where the camera is located.
 * @param entryDirection The direction in which vehicles are considered to be entering the camera's view.
 * @param exitDirection The direction in which vehicles are considered to be exiting the camera's view.
 */
public record Camera(String id, String carParkId, Direction entryDirection, Direction exitDirection) {

    public Camera(String id, String carParkId) {
        this(id, carParkId, Direction.TOWARDS, Direction.AWAY);
    }
}
