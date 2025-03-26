package com.airline.controller;

import com.airline.model.Customer;
import com.airline.model.DisplayClass;
import com.airline.model.Flight;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Controller class that handles flight reservation operations.
 * This class manages booking, canceling, and displaying flight information.
 */
public class FlightReservation implements DisplayClass {

    // ************************************************************ Fields ************************************************************
    private final Flight flight;
    private int flightIndexInFlightList;

    // ************************************************************ Constructors ************************************************************
    
    /**
     * Default constructor
     */
    public FlightReservation() {
        this.flight = new Flight();
    }

    // ************************************************************ Public Methods ************************************************************

    /**
     * Book the numOfTickets for said flight for the specified user. Update the available seats in main system by
     * Subtracting the numOfTickets from the main system. If a new customer registers for the flight, then it adds
     * the customer to that flight, else if the user is already added to that flight, then it just updates the
     * numOfSeats of that flight.
     *
     * @param flightNo     FlightID of the flight to be booked
     * @param numOfTickets number of tickets to be booked
     * @param userID       userID of the user which is booking the flight
     */
    public void bookFlight(String flightNo, int numOfTickets, String userID) {
        boolean isFound = false;
        for (Flight f1 : Flight.getFlightList()) {
            if (flightNo.equalsIgnoreCase(f1.getFlightNumber())) {
                for (Customer customer : Customer.getCustomerCollection()) {
                    if (userID.equals(customer.getUserID())) {
                        isFound = true;
                        f1.setNoOfSeatsInTheFlight(f1.getNoOfSeats() - numOfTickets);
                        if (!f1.isCustomerAlreadyAdded(f1.getListOfRegisteredCustomersInAFlight(), customer)) {
                            f1.addNewCustomerToFlight(customer);
                        }
                        if (isFlightAlreadyAddedToCustomerList(customer.getFlightsRegisteredByUser(), f1)) {
                            addNumberOfTicketsToAlreadyBookedFlight(customer, numOfTickets);
                            if (flightIndex(Flight.getFlightList(), flight) != -1) {
                                customer.addExistingFlightToCustomerList(flightIndex(Flight.getFlightList(), flight), numOfTickets);
                            }
                        } else {
                            customer.addNewFlightToCustomerList(f1);
                            addNumberOfTicketsForNewFlight(customer, numOfTickets);
                        }
                    break;
                    }
                }
            }
        }
        if (!isFound) {
            System.out.println("Invalid Flight Number...! No flight with the  ID \"" + flightNo + "\" was found...");
        } else {
            System.out.printf("\n %50s You've booked %d tickets for Flight \"%5s\"...", "", numOfTickets, flightNo.toUpperCase());
        }
    }

    /**
     * Cancels the flight for a particular user and return/add the numOfTickets back to
     * the main flight scheduler.
     *
     * @param userID    ID of the user for whom the flight is to be cancelled
     */
    public void cancelFlight(String userID) {
        String flightNum = "";
        Scanner read = new Scanner(System.in);
        int index = 0, ticketsToBeReturned;
        boolean isFound = false;
        for (Customer customer : Customer.getCustomerCollection()) {
            if (userID.equals(customer.getUserID())) {
                if (customer.getFlightsRegisteredByUser().size() != 0) {
                    System.out.printf("%50s %s Here is the list of all the Flights registered by you %s", " ", "++++++++++++++", "++++++++++++++");
                    displayFlightsRegisteredByOneUser(userID);
                    System.out.print("Enter the Flight Number of the Flight you want to cancel : ");
                    flightNum = read.nextLine();
                    System.out.print("Enter the number of tickets to cancel : ");
                    int numOfTickets = read.nextInt();
                    Iterator<Flight> flightIterator = customer.getFlightsRegisteredByUser().iterator();
                    while (flightIterator.hasNext()) {
                        Flight flight = flightIterator.next();
                        if (flightNum.equalsIgnoreCase(flight.getFlightNumber())) {
                            isFound = true;
                            int numOfTicketsForFlight = customer.getNumOfTicketsBookedByUser().get(index);
                            while (numOfTickets > numOfTicketsForFlight) {
                                System.out.print("ERROR!!! Number of tickets cannot be greater than " + numOfTicketsForFlight + " for this flight. Please enter the number of tickets again : ");
                                numOfTickets = read.nextInt();
                            }
                            if (numOfTicketsForFlight == numOfTickets) {
                                ticketsToBeReturned = flight.getNoOfSeats() + numOfTicketsForFlight;
                                customer.getNumOfTicketsBookedByUser().remove(index);
                                flightIterator.remove();
                            } else {
                                ticketsToBeReturned = numOfTickets + flight.getNoOfSeats();
                                customer.getNumOfTicketsBookedByUser().set(index, (numOfTicketsForFlight - numOfTickets));
                            }
                            flight.setNoOfSeatsInTheFlight(ticketsToBeReturned);
                            break;
                        }
                        index++;
                    }

                } else {
                    System.out.println("No Flight Has been Registered by you with ID \"\"" + flightNum.toUpperCase() +"\"\".....");
                }
                if (!isFound) {
                    System.out.println("ERROR!!! Couldn't find Flight with ID \"" + flightNum.toUpperCase() + "\".....");
                }
            }
        }
    }

    /**
     * Adds tickets to an already booked flight for a customer
     * 
     * @param customer the customer to add tickets for
     * @param numOfTickets the number of tickets to add
     */
    public void addNumberOfTicketsToAlreadyBookedFlight(Customer customer, int numOfTickets) {
        int newNumOfTickets = customer.getNumOfTicketsBookedByUser().get(flightIndexInFlightList) + numOfTickets;
        customer.getNumOfTicketsBookedByUser().set(flightIndexInFlightList, newNumOfTickets);
    }

    /**
     * Adds tickets for a new flight booking
     * 
     * @param customer the customer booking the flight
     * @param numOfTickets the number of tickets to book
     */
    public void addNumberOfTicketsForNewFlight(Customer customer, int numOfTickets) {
        customer.getNumOfTicketsBookedByUser().add(numOfTickets);
    }

    /**
     * Checks if a flight is already in a customer's list of registered flights
     * 
     * @param flightList the customer's list of flights
     * @param flight the flight to check
     * @return true if the flight is already in the list, false otherwise
     */
    public boolean isFlightAlreadyAddedToCustomerList(List<Flight> flightList, Flight flight) {
        boolean addedOrNot = false;
        for (Flight flight1 : flightList) {
            if (flight1.getFlightNumber().equalsIgnoreCase(flight.getFlightNumber())) {
                this.flightIndexInFlightList = flightList.indexOf(flight1);
                addedOrNot = true;
                break;
            }
        }
        return addedOrNot;
    }

    /**
     * Finds the index of a flight in a list of flights
     * 
     * @param flightList the list of flights to search
     * @param flight the flight to find
     * @return the index of the flight, or -1 if not found
     */
    public int flightIndex(List<Flight> flightList, Flight flight) {
        int index = -1;
        for (Flight flight1 : flightList) {
            if (flight1.getFlightNumber().equalsIgnoreCase(flight.getFlightNumber())) {
                index = flightList.indexOf(flight1);
                break;
            }
        }
        return index;
    }

    /**
     * Displays all registered users for all flights
     */
    @Override
    public void displayRegisteredUsersForAllFlight() {
        for (Flight flight : Flight.getFlightList()) {
            if (flight.getListOfRegisteredCustomersInAFlight().size() > 0) {
                displayHeaderForUsers(flight, flight.getListOfRegisteredCustomersInAFlight());
            }
        }
    }

    /**
     * Displays all registered users for a specific flight
     * 
     * @param flightNum the flight number to display users for
     */
    @Override
    public void displayRegisteredUsersForASpecificFlight(String flightNum) {
        boolean isFound = false;
        for (Flight flight : Flight.getFlightList()) {
            if (flightNum.equalsIgnoreCase(flight.getFlightNumber())) {
                isFound = true;
                if (flight.getListOfRegisteredCustomersInAFlight().size() > 0) {
                    displayHeaderForUsers(flight, flight.getListOfRegisteredCustomersInAFlight());
                } else {
                    System.out.printf("\n\n\n\n%50s No Passenger has registered for the Flight %s yet...\n\n\n", "", flightNum.toUpperCase());
                }
                break;
            }
        }
        if (!isFound) {
            System.out.printf("\n\n\n\n%50s No Flight with the ID %s Found...\n\n\n", "", flightNum.toUpperCase());
        }
    }

    /**
     * Displays a header and information about users registered for a specific flight
     * 
     * @param flight the flight to display users for
     * @param c the list of customers registered for the flight
     */
    @Override
    public void displayHeaderForUsers(Flight flight, List<Customer> c) {
        System.out.printf("\n\n\n\n%50s Here is the list of all the Passengers registered for the Flight %s\n\n\n", "", flight.getFlightNumber());
        System.out.printf("%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+\n", "");
        System.out.printf("%10s| SerialNum  |   UserID   | Passenger Names                  | Age     | EmailID\t\t       | Home Address\t\t\t     | Phone Number\t       |%n", "");
        System.out.printf("%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+\n", "");
        System.out.println();
        for (int i = 0; i < c.size(); i++) {
            System.out.printf("%10s| %-10d | %-10s | %-32s | %-7s | %-27s | %-35s | %-23s |\n", "", i + 1, c.get(i).randomIDDisplay(c.get(i).getUserID()), c.get(i).getName(), c.get(i).getAge(), c.get(i).getEmail(), c.get(i).getAddress(), c.get(i).getPhone());
            System.out.printf("%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+\n", "");
        }
    }

    /**
     * Displays all flights registered by a specific user
     * 
     * @param userID the ID of the user whose flights to display
     */
    @Override
    public void displayFlightsRegisteredByOneUser(String userID) {
        boolean isFound = false;
        for (Customer customer : Customer.getCustomerCollection()) {
            if (userID.equals(customer.getUserID())) {
                isFound = true;
                if (customer.getFlightsRegisteredByUser().size() > 0) {
                    System.out.printf("\n\n\n\n%50s Here is the list of all the Flights registered by %s\n\n\n", "", customer.getName());
                    System.out.print("+------+-------------------------------------------+-----------+-------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
                    System.out.printf("| Num  | FLIGHT SCHEDULE\t\t\t   | FLIGHT NO | SEATS BOOKED| \tFROM ====>>       | \t====>> TO\t   | \t    ARRIVAL TIME       | FLIGHT TIME |  GATE  |  FLIGHT STATUS  |%n");
                    System.out.print("+------+-------------------------------------------+-----------+-------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
                    for (int i = 0; i < customer.getFlightsRegisteredByUser().size(); i++) {
                        Flight flight = customer.getFlightsRegisteredByUser().get(i);
                        System.out.printf("| %-5d| %-41s | %-9s | \t%-9d | %-21s | %-22s | %-10s  |   %-6sHrs |  %-4s  | %-15s |\n", i + 1, flight.getFlightSchedule(), flight.getFlightNumber(), customer.getNumOfTicketsBookedByUser().get(i), flight.getFromWhichCity(), flight.getToWhichCity(), flight.fetchArrivalTime(), flight.getFlightTime(), flight.getGate(), "As Per Schedule");
                        System.out.print("+------+-------------------------------------------+-----------+-------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
                    }
                } else {
                    System.out.printf("\n\n\n\n%50s No Flight has been registered by %s yet...\n\n\n", "", customer.getName());
                }
                break;
            }
        }
        if (!isFound) {
            System.out.printf("\n\n\n\n%50s No Customer with the ID %s Found...\n\n\n", "", userID);
        }
    }
}