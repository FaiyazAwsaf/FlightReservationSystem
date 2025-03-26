package com.airline.model.flight;

import java.util.List;

/**
 * Abstract base class for flight operations.
 * This class provides common functionality for all flight-related classes.
 */
public abstract class FlightBase {
    
    /**
     * Calculates the flight time based on distance.
     * 
     * @param distanceInMiles the distance of the flight in miles
     * @return the calculated flight time as a string
     */
    protected abstract String calculateFlightTime(double distanceInMiles);
    
    /**
     * Formats flight information as a string for display purposes.
     * 
     * @param index index or serial number for the flight
     * @return formatted string representation of flight information
     */
    public abstract String toString(int index);
    
    /**
     * Calculates the distance between two points on Earth using their coordinates.
     * 
     * @param lat1 latitude of the first point
     * @param lon1 longitude of the first point
     * @param lat2 latitude of the second point
     * @param lon2 longitude of the second point
     * @return array containing the distance in miles and kilometers
     */
    public abstract String[] calculateDistance(double lat1, double lon1, double lat2, double lon2);
}