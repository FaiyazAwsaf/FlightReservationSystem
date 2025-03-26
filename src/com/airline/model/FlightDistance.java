package com.airline.model;

/**
 * Abstract class that provides distance calculation functionality for flights.
 * This class defines methods for calculating distances between airports and displaying measurement information.
 */
public abstract class FlightDistance {
    
    /**
     * Formats flight information as a string for display purposes.
     * 
     * @param i index or serial number for the flight
     * @return formatted string representation of flight information
     */
    public abstract String toString(int i);

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

    /**
     * Displays important guidelines and information about flight measurements.
     * This includes information about distance calculations, flight times, and arrival/departure times.
     */
    public void displayMeasurementInstructions() {
        String symbols = "+---------------------------+";
        System.out.printf("\n\n %100s\n %100s", symbols, "| SOME IMPORTANT GUIDELINES |");
        System.out.printf("\n %100s\n", symbols);
        System.out.println("\n\t\t1. Distance between the destinations are based upon the Airports Coordinates(Latitudes && Longitudes) based in those cities\n");
        System.out.println("\t\t2. Actual Distance of the flight may vary from this approximation as Airlines may define their on Travel Policy that may restrict the planes to fly through specific regions...\n");
        System.out.println("\t\t3. Flight Time depends upon several factors such as Ground Speed(GS), AirCraft Design, Flight Altitude and Weather. Ground Speed for these calculations is 450 Knots...\n");
        System.out.println("\t\t4. Expect reaching your destination early or late from the Arrival Time. So, please keep a margin of ±1 hour...\n");
        System.out.println("\t\t5. The departure time is the moment that your plane pushes back from the gate, not the time it takes off. The arrival time is the moment that your plane pulls into the gate, not the time\n\t\t   it touches down on the runway...\n");
    }
}