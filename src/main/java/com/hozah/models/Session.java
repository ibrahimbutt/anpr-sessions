package com.hozah.models;

/**
 * Represents a session with its corresponding Vehicle Registration Mark (VRM),
 * entry tick, and exit tick.
 * Immune to concurrent modification risks and ensures data immutability and thread-safety.
 * Suitable for storing session details in applications that process vehicle tracking data over time.
 *
 * @param vrm The vehicle registration mark associated with the session.
 * @param entryTick The tick (time unit) when the session starts.
 * @param exitTick The tick (time unit) when the session ends.
 */
public record Session(String vrm, int entryTick, int exitTick) {
}
