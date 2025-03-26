package com.airline.model;

import java.util.List;
import com.airline.model.customer.Customer;
import com.airline.model.flight.*;


/**
 * Interface defining display operations for flight and customer data.
 * Classes implementing this interface are responsible for formatting and presenting
 * information about flights and customers to users.
 */
public interface DisplayClass {

    /**
     * Displays all registered users for all flights in the system.
     * This provides a comprehensive view of all passengers across all flights.
     */
    void displayRegisteredUsersForAllFlight();

    /**
     * Displays all registered users for a specific flight.
     * 
     * @param flightNum the flight number to display passengers for
     */
    void displayRegisteredUsersForASpecificFlight(String flightNum);

    /**
     * Displays a header and formatted information about users registered for a specific flight.
     * 
     * @param flight the flight to display users for
     * @param c the list of customers registered for the flight
     */
    void displayHeaderForUsers(Flight flight, List<Customer> c);

    /**
     * Displays all flights registered by a specific user.
     * 
     * @param userID the ID of the user whose flights to display
     */
    void displayFlightsRegisteredByOneUser(String userID);
}