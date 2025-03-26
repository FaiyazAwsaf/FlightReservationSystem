package com.airline.model;

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
     * Adds numOfTickets to existing customer's tickets for the this flight.
     *
     * @param customer     customer in which tickets are to be added
     * @param numOfTickets number of tickets to add
     */
    public void addTicketsToExistingCustomer(Customer customer, int numOfTickets) {
        customer.addExistingFlightToCustomerList(customerIndex, numOfTickets);
    }

    /**
     * Checks if the specified customer is already registered in the Flight's array list
     * 
     * @param customersList of the flight
     * @param customer specified customer to be checked
     * @return true if customer is already registered, false otherwise
     */
    public boolean isCustomerAlreadyAdded(List<Customer> customersList, Customer customer) {
        boolean isAdded = false;
        for (Customer c : customersList) {
            if (c.getUserID().equals(customer.getUserID())) {
                customerIndex = customersList.indexOf(c);
                isAdded = true;
                break;
            }
        }
        return isAdded;
    }

    /**
     * Displays all the flights in the flight scheduler
     */
    public void displayFlightSchedule() {
        System.out.println();
        System.out.print("+------+-------------------------------------------+-----------+-------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
        System.out.printf("| Num  | FLIGHT SCHEDULE\t\t\t   | FLIGHT NO | SEATS AVAIL. | \tFROM ====>>       | \t====>> TO\t   | \t    ARRIVAL TIME       | FLIGHT TIME |  GATE  |  FLIGHT STATUS  |%n");
        System.out.print("+------+-------------------------------------------+-----------+-------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
        for (int i = 0; i < flightList.size(); i++) {
            System.out.println(toString(i + 1));
            System.out.print("+------+-------------------------------------------+-----------+-------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
        }
    }

    /**
     * Deletes the flight with the specified flight number
     * 
     * @param flightNum the flight number to delete
     */
    public void deleteFlight(String flightNum) {
        boolean isFound = false;
        Iterator<Flight> flightIterator = flightList.iterator();
        while (flightIterator.hasNext()) {
            Flight flight = flightIterator.next();
            if (flightNum.equalsIgnoreCase(flight.getFlightNumber())) {
                isFound = true;
                flightIterator.remove();
                break;
            }
        }
        if (isFound) {
            System.out.printf("\n%-50sPrinting all Flights after deleting Flight with the ID %s.....!!!!\n", "", flightNum.toUpperCase());
            displayFlightSchedule();
        } else {
            System.out.printf("%-50sNo Flight with the ID %s Found...!!!\n", " ", flightNum.toUpperCase());
        }
    }

    /**
     * Creates new flights and time for the flight scheduler
     * 
     * @return formatted flight schedule string
     */
    public String createNewFlightsAndTime() {
        Instant instant = Instant.now().plus(nextFlightDay, ChronoUnit.DAYS);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss");
        nextFlightDay++;
        return dateTime.format(formatter);
    }

    /**
     * Calculates the flight time based on distance
     * 
     * @param distanceInMiles distance in miles
     * @return formatted flight time string
     */
    public String calculateFlightTime(double distanceInMiles) {
        double groundSpeed = 450.0;        // in Knots
        double flightTimeInHours = distanceInMiles / groundSpeed;
        int hours = (int) flightTimeInHours;
        int minutes = (int) ((flightTimeInHours - hours) * 60);
        return String.format("%d:%02d", hours, minutes);
    }

    /**
     * Calculates the arrival time based on departure time and flight duration
     * 
     * @return formatted arrival time string
     */
    public String fetchArrivalTime() {
        String[] flightTimeHoursAndMinutes = flightTime.split(":");
        int flightTimeHours = Integer.parseInt(flightTimeHoursAndMinutes[0]);
        int flightTimeMinutes = Integer.parseInt(flightTimeHoursAndMinutes[1]);

        String[] departureTimeHoursAndMinutes = flightSchedule.split(" ");
        String[] hoursAndMinutes = departureTimeHoursAndMinutes[4].split(":");
        int departureHours = Integer.parseInt(hoursAndMinutes[0]);
        int departureMinutes = Integer.parseInt(hoursAndMinutes[1]);

        int totalMinutes = departureMinutes + flightTimeMinutes;
        int additionalHours = totalMinutes / 60;
        int arrivalMinutes = totalMinutes % 60;

        int arrivalHours = (departureHours + flightTimeHours + additionalHours) % 24;

        return String.format("%02d:%02d", arrivalHours, arrivalMinutes);
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
        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        double distanceInKm = c * r;
        double distanceInMiles = distanceInKm * 0.621371;

        String[] distanceBetweenTheCities = new String[2];
        distanceBetweenTheCities[0] = String.format("%.2f", distanceInMiles);
        distanceBetweenTheCities[1] = String.format("%.2f", distanceInKm);

        return distanceBetweenTheCities;
    }

    /**
     * Returns a string representation of the flight for display purposes
     * 
     * @param serialNum serial number for display
     * @return formatted string representation of the flight
     */
    @Override
    public String toString(int serialNum) {
        Flight flight = flightList.get(serialNum - 1);
        return String.format("| %-5d| %-41s | %-9s | \t%-9d | %-21s | %-22s | %-10s  |   %-6sHrs |  %-4s  | %-15s |", serialNum, flight.getFlightSchedule(), flight.getFlightNumber(), flight.getNoOfSeats(), flight.getFromWhichCity(), flight.getToWhichCity(), flight.fetchArrivalTime(), flight.getFlightTime(), flight.getGate(), "As Per Schedule");
    }

    // ************************************************************ Static Methods ************************************************************

    /**
     * Get the list of all flights
     * 
     * @return the list of all flights
     */
    public static List<Flight> getFlightList() {
        return flightList;
    }

    // ************************************************************ Getters and Setters ************************************************************

    /**
     * Get the flight schedule
     * 
     * @return flight schedule
     */
    public String getFlightSchedule() {
        return flightSchedule;
    }

    /**
     * Get the flight number
     * 
     * @return flight number
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * Get the origin city
     * 
     * @return origin city
     */
    public String getFromWhichCity() {
        return fromWhichCity;
    }

    /**
     * Get the destination city
     * 
     * @return destination city
     */
    public String getToWhichCity() {
        return toWhichCity;
    }

    /**
     * Get the flight time
     * 
     * @return flight time
     */
    public String getFlightTime() {
        return flightTime;
    }

    /**
     * Get the number of available seats
     * 
     * @return number of seats
     */
    public int getNoOfSeats() {
        return numOfSeatsInTheFlight;
    }

    /**
     * Get the gate number
     * 
     * @return gate number
     */
    public String getGate() {
        return gate;
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
     * Set the number of seats in the flight
     * 
     * @param numOfSeatsInTheFlight new number of seats
     */
    public void setNoOfSeatsInTheFlight(int numOfSeatsInTheFlight) {
        this.numOfSeatsInTheFlight = numOfSeatsInTheFlight;
    }
}