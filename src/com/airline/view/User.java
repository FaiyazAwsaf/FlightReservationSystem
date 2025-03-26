package com.airline.view;

import com.airline.controller.FlightReservation;
import com.airline.controller.RolesAndPermissions;
import com.airline.model.customer.Customer;
import com.airline.model.flight.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for the Airline Reservation System.
 * This class serves as the entry point and handles user interaction.
 */
public class User {

    // ************************************************************ Fields ************************************************************
    private static List<Customer> customersCollection = new ArrayList<>();
    private static Scanner mainScanner = new Scanner(System.in);

    // ************************************************************ Public Methods ************************************************************

    /**
     * Main method - entry point of the application
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        RolesAndPermissions r1 = new RolesAndPermissions();
        Flight f1 = new Flight();
        FlightReservation bookingAndReserving = new FlightReservation();
        Customer c1 = new Customer();
        f1.flightScheduler();

        displayWelcomeMessage();
        displayMainMenu();
        int desiredOption = getValidMainMenuOption();

        do {
            Scanner inputScanner = new Scanner(System.in);
            
            switch (desiredOption) {
                case 1: // Admin login
                    handleAdminLogin(r1, f1, bookingAndReserving, c1, inputScanner);
                    break;
                case 2: // Register new customer
                    c1.addNewCustomer();
                    break;
                case 3: // Passenger login
                    handlePassengerLogin(r1, f1, bookingAndReserving, inputScanner);
                    break;
                case 4: // Add new admin
                    handleAddNewAdmin(r1, inputScanner);
                    break;
                case 0: // Exit
                    System.out.println("Thanks for Using BAV Airlines Ticketing System...!!!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
            
            displayMainMenu();
            desiredOption = getValidMainMenuOption();
        } while (desiredOption != 0);
    }

    /**
     * Get the customers collection
     * 
     * @return the list of customers
     */
    public static List<Customer> getCustomersCollection() {
        return customersCollection;
    }

    // ************************************************************ Private Methods ************************************************************

    /**
     * Displays the welcome message
     */
    private static void displayWelcomeMessage() {
        System.out.println(
                "\n\t\t\t\t\t+++++++++++++ Welcome to BAV AirLines +++++++++++++\n\nTo Further Proceed, Please enter a value.");
        System.out.println(
                "\n***** Default Username && Password is root-root ***** Using Default Credentials will restrict you to just view the list of Passengers....\n");
    }

    /**
     * Displays the main menu options
     */
    private static void displayMainMenu() {
        // Clear console with a few newlines instead of hundreds
        System.out.println("\n\n\n\n\n");
        System.out.println("\t\t\t\t\t+++++++++++++ BAV Airlines Main Menu +++++++++++++");
        System.out.println("\t\t(a) Press 1 to Login as admin.");
        System.out.println("\t\t(b) Press 2 to Register as Passenger.");
        System.out.println("\t\t(c) Press 3 to Login as Passenger.");
        System.out.println("\t\t(d) Press 4 to Add new Admin.");
        System.out.println("\t\t(e) Press 0 to Exit.");
    }

    /**
     * Gets a valid option from the main menu
     * 
     * @return the selected option
     */
    private static int getValidMainMenuOption() {
        System.out.print("Enter the desired option: ");
        int option = mainScanner.nextInt();
        while (option < 0 || option > 4) {
            System.out.print("ERROR!! Please enter value between 0 - 4. Enter the value again :\t");
            option = mainScanner.nextInt();
        }
        return option;
    }

    /**
     * Handles the admin login process
     * 
     * @param r1 the roles and permissions controller
     * @param f1 the flight controller
     * @param bookingAndReserving the flight reservation controller
     * @param c1 the customer model
     * @param inputScanner the scanner for user input
     */
    private static void handleAdminLogin(RolesAndPermissions r1, Flight f1, FlightReservation bookingAndReserving, 
                                        Customer c1, Scanner inputScanner) {
        System.out.print("\nEnter the UserName to login to the Management System :     ");
        String username = inputScanner.nextLine();
        System.out.print("Enter the Password to login to the Management System :    ");
        String password = inputScanner.nextLine();
        System.out.println();

        int loginResult = r1.isPrivilegedUserOrNot(username, password);
        if (loginResult == -1) {
            System.out.printf(
                    "\n%20sERROR!!! Unable to login Cannot find user with the entered credentials.... Try Creating New Credentials or get yourself register by pressing 4....\n",
                    "");
        } else if (loginResult == 0) {
            System.out.println(
                    "You've standard/default privileges to access the data... You can just view customers data..."
                            + "Can't perform any actions on them....");
            c1.displayCustomersData(true);
        } else {
            System.out.printf(
                    "%-20sLogged in Successfully as \"%s\"...... For further Proceedings, enter a value from below....",
                    "", username);
            handleAdminMenu(username, f1, bookingAndReserving, c1, inputScanner);
        }
    }

    /**
     * Handles the admin menu after successful login
     * 
     * @param username the admin username
     * @param f1 the flight controller
     * @param bookingAndReserving the flight reservation controller
     * @param c1 the customer model
     * @param inputScanner the scanner for user input
     */
    private static void handleAdminMenu(String username, Flight f1, FlightReservation bookingAndReserving, 
                                       Customer c1, Scanner inputScanner) {
        int desiredOption;
        do {
            displayAdminMenu(username);
            desiredOption = mainScanner.nextInt();
            
            switch (desiredOption) {
                case 1: // Add new passenger
                    c1.addNewCustomer();
                    break;
                case 2: // Search passenger
                    searchPassenger(c1, inputScanner);
                    break;
                case 3: // Update passenger data
                    updatePassengerData(c1, inputScanner);
                    break;
                case 4: // Delete passenger
                    deletePassenger(c1, inputScanner);
                    break;
                case 5: // Display all passengers
                    c1.displayCustomersData(false);
                    break;
                case 6: // Display flights registered by passenger
                    displayFlightsByPassenger(c1, bookingAndReserving, inputScanner);
                    break;
                case 7: // Display passengers in a flight
                    displayPassengersInFlight(f1, bookingAndReserving, inputScanner);
                    break;
                case 8: // Delete a flight
                    deleteFlight(f1, inputScanner);
                    break;
                case 0: // Logout
                    System.out.println("Thanks for Using BAV Airlines Ticketing System...!!!");
                    break;
                default:
                    System.out.println(
                            "Invalid Choice...Looks like you're Robot...Entering values randomly...You've Have to login again...");
                    desiredOption = 0;
                    break;
            }
        } while (desiredOption != 0);
    }

    /**
     * Displays the admin menu
     * 
     * @param username the admin username
     */
    private static void displayAdminMenu(String username) {
        System.out.printf("\n\n%-60s+++++++++ Admin Menu +++++++++%50sLogged in as \"%s\"\n", "",
                "", username);
        System.out.printf("%-30s (a) Enter 1 to add new Passenger....\n", "");
        System.out.printf("%-30s (b) Enter 2 to search a Passenger....\n", "");
        System.out.printf("%-30s (c) Enter 3 to update the Data of the Passenger....\n", "");
        System.out.printf("%-30s (d) Enter 4 to delete a Passenger....\n", "");
        System.out.printf("%-30s (e) Enter 5 to Display all Passengers....\n", "");
        System.out.printf("%-30s (f) Enter 6 to Display all flights registered by a Passenger...\n", "");
        System.out.printf("%-30s (g) Enter 7 to Display all registered Passengers in a Flight....\n", "");
        System.out.printf("%-30s (h) Enter 8 to Delete a Flight....\n", "");
        System.out.printf("%-30s (i) Enter 0 to Go back to the Main Menu/Logout....\n", "");
        System.out.print("Enter the desired Choice :   ");
    }

    /**
     * Handles searching for a passenger
     * 
     * @param c1 the customer model
     * @param inputScanner the scanner for user input
     */
    private static void searchPassenger(Customer c1, Scanner inputScanner) {
        c1.displayCustomersData(false);
        System.out.print("Enter the CustomerID to Search :\t");
        String customerID = inputScanner.nextLine();
        System.out.println();
        c1.searchUser(customerID);
    }

    /**
     * Handles updating passenger data
     * 
     * @param c1 the customer model
     * @param inputScanner the scanner for user input
     */
    private static void updatePassengerData(Customer c1, Scanner inputScanner) {
        c1.displayCustomersData(false);
        System.out.print("Enter the CustomerID to Update its Data :\t");
        String customerID = inputScanner.nextLine();
        if (customersCollection.size() > 0) {
            c1.editUserInfo(customerID);
        } else {
            System.out.printf("%-50sNo Customer with the ID %s Found...!!!\n", " ", customerID);
        }
    }

    /**
     * Handles deleting a passenger
     * 
     * @param c1 the customer model
     * @param inputScanner the scanner for user input
     */
    private static void deletePassenger(Customer c1, Scanner inputScanner) {
        c1.displayCustomersData(false);
        System.out.print("Enter the CustomerID to Delete its Data :\t");
        String customerID = inputScanner.nextLine();
        if (customersCollection.size() > 0) {
            c1.deleteUser(customerID);
        } else {
            System.out.printf("%-50sNo Customer with the ID %s Found...!!!\n", " ", customerID);
        }
    }

    /**
     * Handles displaying flights registered by a passenger
     * 
     * @param c1 the customer model
     * @param bookingAndReserving the flight reservation controller
     * @param inputScanner the scanner for user input
     */
    private static void displayFlightsByPassenger(Customer c1, FlightReservation bookingAndReserving, Scanner inputScanner) {
        c1.displayCustomersData(false);
        System.out.print("\n\nEnter the ID of the user to display all flights registered by that user...");
        String id = inputScanner.nextLine();
        bookingAndReserving.displayFlightsRegisteredByOneUser(id);
    }

    /**
     * Handles displaying passengers in a flight
     * 
     * @param f1 the flight controller
     * @param bookingAndReserving the flight reservation controller
     * @param inputScanner the scanner for user input
     */
    private static void displayPassengersInFlight(Flight f1, FlightReservation bookingAndReserving, Scanner inputScanner) {
        System.out.print(
                "Do you want to display Passengers of all flights or a specific flight.... 'Y/y' for displaying all flights and 'N/n' to look for a"
                        + " specific flight.... ");
        char choice = inputScanner.nextLine().charAt(0);
        if ('y' == choice || 'Y' == choice) {
            bookingAndReserving.displayRegisteredUsersForAllFlight();
        } else if ('n' == choice || 'N' == choice) {
            f1.displayFlightSchedule();
            System.out.print("Enter the Flight Number to display the list of passengers registered in that flight... ");
            String flightNum = inputScanner.nextLine();
            bookingAndReserving.displayRegisteredUsersForASpecificFlight(flightNum);
        } else {
            System.out.println("Invalid Choice...No Response...!");
        }
    }

    /**
     * Handles deleting a flight
     * 
     * @param f1 the flight controller
     * @param inputScanner the scanner for user input
     */
    private static void deleteFlight(Flight f1, Scanner inputScanner) {
        f1.displayFlightSchedule();
        System.out.print("Enter the Flight Number to delete the flight : ");
        String flightNum = inputScanner.nextLine();
        f1.deleteFlight(flightNum);
    }

    /**
     * Handles the passenger login process
     * 
     * @param r1 the roles and permissions controller
     * @param f1 the flight controller
     * @param bookingAndReserving the flight reservation controller
     * @param inputScanner the scanner for user input
     */
    private static void handlePassengerLogin(RolesAndPermissions r1, Flight f1, FlightReservation bookingAndReserving, 
                                           Scanner inputScanner) {
        System.out.print("\nEnter your Email to login to the Passenger Portal :     ");
        String username = inputScanner.nextLine();
        System.out.print("Enter your Password to login to the Passenger Portal :    ");
        String password = inputScanner.nextLine();
        System.out.println();

        String[] result = r1.isPassengerRegistered(username, password).split("-");
        if (result[0].equals("0")) {
            System.out.printf(
                    "\n%20sERROR!!! Unable to login Cannot find user with the entered credentials.... Try Creating New Credentials or get yourself register by pressing 2....\n",
                    "");
        } else {
            System.out.printf("%-20sLogged in Successfully..... For further Proceedings, enter a value from below....",
                    "");
            handlePassengerMenu(result[1], f1, bookingAndReserving, inputScanner);
        }
    }

    /**
     * Handles the passenger menu after successful login
     * 
     * @param userId the passenger user ID
     * @param f1 the flight controller
     * @param bookingAndReserving the flight reservation controller
     * @param inputScanner the scanner for user input
     */
    private static void handlePassengerMenu(String userId, Flight f1, FlightReservation bookingAndReserving, 
                                          Scanner inputScanner) {
        int desiredOption;
        do {
            displayPassengerMenu();
            desiredOption = mainScanner.nextInt();
            
            switch (desiredOption) {
                case 1: // Book a flight
                    bookFlight(f1, bookingAndReserving, userId, inputScanner);
                    break;
                case 2: // Cancel a flight
                    bookingAndReserving.cancelFlight(userId);
                    break;
                case 3: // Display all flights
                    f1.displayFlightSchedule();
                    break;
                case 4: // Display flights registered by user
                    bookingAndReserving.displayFlightsRegisteredByOneUser(userId);
                    break;
                case 5: // Display measurement instructions
                    f1.displayMeasurementInstructions();
                    break;
                case 0: // Logout
                    System.out.println("Thanks for Using BAV Airlines Ticketing System...!!!");
                    break;
                default:
                    System.out.println(
                            "Invalid Choice...Looks like you're Robot...Entering values randomly...You've Have to login again...");
                    desiredOption = 0;
                    break;
            }
        } while (desiredOption != 0);
    }

    /**
     * Displays the passenger menu
     */
    private static void displayPassengerMenu() {
        System.out.printf("\n\n%-60s+++++++++ Passenger Menu +++++++++\n", "");
        System.out.printf("%-30s (a) Enter 1 to Book a Flight....\n", "");
        System.out.printf("%-30s (b) Enter 2 to Cancel a Flight....\n", "");
        System.out.printf("%-30s (c) Enter 3 to Display all Flights....\n", "");
        System.out.printf("%-30s (d) Enter 4 to Display all Flights registered by you....\n", "");
        System.out.printf("%-30s (e) Enter 5 to Display Measurement Instructions....\n", "");
        System.out.printf("%-30s (f) Enter 0 to Go back to the Main Menu/Logout....\n", "");
        System.out.print("Enter the desired Choice :   ");
    }

    /**
     * Handles booking a flight
     * 
     * @param f1 the flight controller
     * @param bookingAndReserving the flight reservation controller
     * @param userId the passenger user ID
     * @param inputScanner the scanner for user input
     */
    private static void bookFlight(Flight f1, FlightReservation bookingAndReserving, String userId, Scanner inputScanner) {
        f1.displayFlightSchedule();
        System.out.print("Enter the Flight Number to Book the Flight : ");
        String flightNum = inputScanner.nextLine();
        System.out.printf("Enter the Number of tickets for %s flight : ", flightNum.toUpperCase());
        int numOfTickets = mainScanner.nextInt();
        bookingAndReserving.bookFlight(flightNum, numOfTickets, userId);
    }

    /**
     * Handles adding a new admin
     * 
     * @param r1 the roles and permissions controller
     * @param inputScanner the scanner for user input
     */
    private static void handleAddNewAdmin(RolesAndPermissions r1, Scanner inputScanner) {
        System.out.print("\nEnter the UserName for the new Admin :     ");
        String username = inputScanner.nextLine();
        System.out.print("Enter the Password for the new Admin :    ");
        String password = inputScanner.nextLine();
        System.out.println();
        if (r1.addNewAdmin(username, password)) {
            System.out.printf("\n%20sAdmin with the username \"%s\" added successfully....\n", "", username);
        } else {
            System.out.printf("\n%20sERROR!!! Admin list is full. Cannot add more admins.\n", "");
        }
    }
}