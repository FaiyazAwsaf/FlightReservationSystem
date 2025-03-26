package com.airline.model.flight;

import com.airline.model.customer.Customer;
import com.airline.util.RandomGenerator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Represents a flight in the airline reservation system.
 * This class encapsulates all flight-related data and operations.
 */
public class Flight extends FlightDistance {

    // ************************************************************ Fields ************************************************************
    private final String flightSchedule;
    private final String flightNumber;
    private final String fromWhichCity;
    private final String gate;
    private final String toWhichCity;
    private double distanceInMiles;
    private double distanceInKm;
    private String flightTime;
    private int numOfSeatsInTheFlight;
    private List<Customer> listOfRegisteredCustomersInAFlight;
    private int customerIndex;
    private static int nextFlightDay = 0;
    private static final List<Flight> flightList = new ArrayList<>();

    // ************************************************************ Constructors ************************************************************

    /**
     * Default constructor
     */
    public Flight() {
        this.flightSchedule = null;
        this.flightNumber = null;
        this.numOfSeatsInTheFlight = 0;
        toWhichCity = null;
        fromWhichCity = null;
        this.gate = null;
    }

    /**
     * Creates new random flight from the specified arguments.
     *
     * @param flightSchedule           includes departure date and time of flight
     * @param flightNumber             unique identifier of each flight
     * @param numOfSeatsInTheFlight    available seats in the flight
     * @param chosenDestinations       consists of origin and destination airports(cities)
     * @param distanceBetweenTheCities gives the distance between the airports both in miles and kilometers
     * @param gate                     from where passengers will board to the aircraft
     */
    public Flight(String flightSchedule, String flightNumber, int numOfSeatsInTheFlight, String[][] chosenDestinations, String[] distanceBetweenTheCities, String gate) {
        this.flightSchedule = flightSchedule;
        this.flightNumber = flightNumber;
        this.numOfSeatsInTheFlight = numOfSeatsInTheFlight;
        this.fromWhichCity = chosenDestinations[0][0];
        this.toWhichCity = chosenDestinations[1][0];
        this.distanceInMiles = Double.parseDouble(distanceBetweenTheCities[0]);
        this.distanceInKm = Double.parseDouble(distanceBetweenTheCities[1]);
        this.flightTime = calculateFlightTime(distanceInMiles);
        this.listOfRegisteredCustomersInAFlight = new ArrayList<>();
        this.gate = gate;
    }

    // ************************************************************ Public Methods ************************************************************

    /**
     * Creates Flight Schedule. All methods of this class are collaborating with each other
     * to create flight schedule of the said length in this method.
     */
    public void flightScheduler() {
        int numOfFlights = 15;              // decides how many unique flights to be included/display in scheduler
        RandomGenerator r1 = new RandomGenerator();
        for (int i = 0; i < numOfFlights; i++) {
            String[][] chosenDestinations = r1.randomDestinations();
            String[] distanceBetweenTheCities = calculateDistance(Double.parseDouble(chosenDestinations[0][1]), Double.parseDouble(chosenDestinations[0][2]), Double.parseDouble(chosenDestinations[1][1]), Double.parseDouble(chosenDestinations[1][2]));
            String flightSchedule = createNewFlightsAndTime();
            String flightNumber = r1.randomFlightNumbGen(2, 1).toUpperCase();
            int numOfSeatsInTheFlight = r1.randomNumOfSeats();
            String gate = r1.randomFlightNumbGen(1, 30);
            flightList.add(new Flight(flightSchedule, flightNumber, numOfSeatsInTheFlight, chosenDestinations, distanceBetweenTheCities, gate.toUpperCase()));
        }
    }

    /**
     * Registers new Customer in this Flight.
     *
     * @param customer customer to be registered
     */
    public void addNewCustomerToFlight(Customer customer) {
        this.listOfRegisteredCustomersInAFlight.add(customer);
    }

    /**
     * Checks if a customer is already registered for this flight
     *
     * @param customerList list of customers to check
     * @param customer customer to find
     * @return true if customer is already in the list, false otherwise
     */
    public boolean isCustomerAlreadyAdded(List<Customer> customerList, Customer customer) {
        boolean isAdded = false;
        for (Customer customer1 : customerList) {
            if (customer1.getUserID().equals(customer.getUserID())) {
                this.customerIndex = customerList.indexOf(customer1);
                isAdded = true;
                break;
            }
        }
        return isAdded;
    }

    /**
     * Creates a new flight schedule with departure and arrival times
     *
     * @return formatted flight schedule string
     */
    private String createNewFlightsAndTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime departureTime = currentTime.plus(nextFlightDay, ChronoUnit.DAYS);
        nextFlightDay++;
        if (nextFlightDay > 30) {
            nextFlightDay = 0;
        }
        Random random = new Random();
        int randomHour = random.nextInt(24);
        int randomMinute = random.nextInt(60);
        departureTime = departureTime.withHour(randomHour).withMinute(randomMinute);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return departureTime.format(formatter);
    }

    /**
     * Calculates the flight time based on distance
     *
     * @param distanceInMiles distance in miles
     * @return formatted flight time string
     */
    @Override
    protected String calculateFlightTime(double distanceInMiles) {
        double groundSpeed = 450.0;  // in knots (nautical miles per hour)
        double flightTimeInHours = distanceInMiles / groundSpeed;
        int hours = (int) flightTimeInHours;
        int minutes = (int) ((flightTimeInHours - hours) * 60);
        return String.format("%d hours %d minutes", hours, minutes);
    }

    /**
     * Calculates the distance between two points on Earth using their coordinates
     *
     * @param lat1 latitude of the first point
     * @param lon1 longitude of the first point
     * @param lat2 latitude of the second point
     * @param lon2 longitude of the second point
     * @return array containing the distance in miles and kilometers
     */
    @Override
    public String[] calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distanceInKm = R * c;
        double distanceInMiles = distanceInKm * 0.621371;

        String[] distanceBetweenTheCities = new String[2];
        distanceBetweenTheCities[0] = String.format("%.2f", distanceInMiles);
        distanceBetweenTheCities[1] = String.format("%.2f", distanceInKm);

        return distanceBetweenTheCities;
    }

    /**
     * Formats flight information as a string for display purposes
     *
     * @param index index or serial number for the flight
     * @return formatted string representation of flight information
     */
    @Override
    public String toString(int index) {
        return String.format("%10s| %-10d | %-10s | %-20s | %-20s | %-15s | %-20s | %-10d | %-15s | %-10s |", "", index + 1,
                flightNumber, fromWhichCity, toWhichCity, flightSchedule, flightTime,
                numOfSeatsInTheFlight, String.format("%.2f", distanceInMiles), gate);
    }

    // ************************************************************ Getters and Setters ************************************************************

    /**
     * Get the flight number
     *
     * @return the flight number
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * Get the number of available seats
     *
     * @return the number of seats
     */
    public int getNoOfSeats() {
        return numOfSeatsInTheFlight;
    }

    /**
     * Set the number of seats for the flight
     *
     * @param numOfSeatsInTheFlight the new number of seats
     */
    public void setNoOfSeatsInTheFlight(int numOfSeatsInTheFlight) {
        this.numOfSeatsInTheFlight = numOfSeatsInTheFlight;
    }

    /**
     * Get the list of registered customers for this flight
     *
     * @return list of customers
     */
    public List<Customer> getListOfRegisteredCustomersInAFlight() {
        return listOfRegisteredCustomersInAFlight;
    }

    /**
     * Get the list of all flights
     *
     * @return the list of flights
     */
    public static List<Flight> getFlightList() {
        return flightList;
    }

    /**
     * Get the flight schedule
     *
     * @return the flight schedule
     */
    public String getFlightSchedule() {
        return flightSchedule;
    }

    /**
     * Get the origin city of the flight
     *
     * @return the origin city
     */
    public String getFromWhichCity() {
        return fromWhichCity;
    }

    /**
     * Get the destination city of the flight
     *
     * @return the destination city
     */
    public String getToWhichCity() {
        return toWhichCity;
    }

    /**
     * Get the gate number
     *
     * @return the gate number
     */
    public String getGate() {
        return gate;
    }

    /**
     * Get the flight time
     *
     * @return the flight time
     */
    public String getFlightTime() {
        return flightTime;
    }
    
    /**
     * Calculate and return the arrival time based on departure time and flight duration
     *
     * @return formatted arrival time string
     */
    public String fetchArrivalTime() {
        try {
            // Parse the departure time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime departureDateTime = LocalDateTime.parse(flightSchedule, formatter);
            
            // Parse the flight time to get hours and minutes
            String[] parts = flightTime.split(" ");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[2]);
            
            // Calculate arrival time by adding flight duration to departure time
            LocalDateTime arrivalDateTime = departureDateTime.plusHours(hours).plusMinutes(minutes);
            
            // Format and return the arrival time
            return arrivalDateTime.format(formatter);
        } catch (Exception e) {
            // Return a placeholder if there's an error in calculation
            return "Time not available";
        }
    }

    /**
     * Displays the flight schedule with all available flights
     * This method prints a formatted table of all flights with their details
     */
    public void displayFlightSchedule() {
        System.out.println("\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tFLIGHT SCHEDULE");
        System.out.println("\t\t+----------+------------+------------+----------------------+----------------------+-----------------+----------------------+------------+-----------------+------------+");
        System.out.println("\t\t|   S.No   |  Flight No |   Origin   |     Destination      |    Departure Time    |  Arrival Time   |     Flight Time      |    Seats   |     Distance    |    Gate    |");
        System.out.println("\t\t+----------+------------+------------+----------------------+----------------------+-----------------+----------------------+------------+-----------------+------------+");
        
        if (flightList.isEmpty()) {
            System.out.println("\t\t|                                                       No flights available                                                                  |");
        } else {
            for (int i = 0; i < flightList.size(); i++) {
                Flight flight = flightList.get(i);
                System.out.println("\t\t" + flight.toString(i));
            }
        }
        
        System.out.println("\t\t+----------+------------+------------+----------------------+----------------------+-----------------+----------------------+------------+-----------------+------------+");
    }
    
    /**
     * Deletes a flight with the specified flight number
     *
     * @param flightNumber the flight number to delete
     */
    public void deleteFlight(String flightNumber) {
        boolean found = false;
        for (int i = 0; i < flightList.size(); i++) {
            if (flightList.get(i).getFlightNumber().equals(flightNumber)) {
                flightList.remove(i);
                found = true;
                System.out.println("Flight " + flightNumber + " has been deleted successfully.");
                break;
            }
        }
        
        if (!found) {
            System.out.println("Flight " + flightNumber + " not found.");
        }
    }
}