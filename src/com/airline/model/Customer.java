package com.airline.model;

import com.airline.util.RandomGenerator;

import java.util.*;

/**
 * Represents a customer in the airline reservation system.
 * This class encapsulates all customer-related data and operations.
 */
public class Customer {

    // ************************************************************ Fields ************************************************************
    private final String userID;
    private String email;
    private String name;
    private String phone;
    private final String password;
    private String address;
    private int age;
    private List<Flight> flightsRegisteredByUser;
    private List<Integer> numOfTicketsBookedByUser;
    private static final List<Customer> customerCollection = new ArrayList<>();

    // ************************************************************ Constructors ************************************************************

    /**
     * Default constructor
     */
    public Customer() {
        this.userID = null;
        this.name = null;
        this.email = null;
        this.password = null;
        this.phone = null;
        this.address = null;
        this.age = 0;
    }

    /**
     * Registers new customer to the program. Obj of RandomGenerator(Composition) is
     * being used to assign unique userID to the newly created customer.
     *
     * @param name     name of the customer
     * @param email    customer's email
     * @param password customer's account password
     * @param phone    customer's phone-number
     * @param address  customer's address
     * @param age      customer's age
     */
    public Customer(String name, String email, String password, String phone, String address, int age) {
        RandomGenerator random = new RandomGenerator();
        random.randomIDGen();
        this.name = name;
        this.userID = random.getRandomNumber();
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.age = age;
        this.flightsRegisteredByUser = new ArrayList<>();
        this.numOfTicketsBookedByUser = new ArrayList<>();
    }

    // ************************************************************ Public Methods ************************************************************

    /**
     * Takes input for the new customer and adds them to programs memory.
     * isUniqueData() validates the entered email
     * and returns true if the entered email is already registered. If email is
     * already registered, program will ask the user
     * to enter new email address to get himself register.
     */
    public void addNewCustomer() {
        System.out.printf("\n\n\n%60s ++++++++++++++ Welcome to the Customer Registration Portal ++++++++++++++", "");
        Scanner read = new Scanner(System.in);
        System.out.print("\nEnter your name :\t");
        String name = read.nextLine();
        System.out.print("Enter your email address :\t");
        String email = read.nextLine();
        while (isUniqueData(email)) {
            System.out.println(
                    "ERROR!!! User with the same email already exists... Use new email or login using the previous credentials....");
            System.out.print("Enter your email address :\t");
            email = read.nextLine();
        }
        System.out.print("Enter your Password :\t");
        String password = read.nextLine();
        System.out.print("Enter your Phone number :\t");
        String phone = read.nextLine();
        System.out.print("Enter your address :\t");
        String address = read.nextLine();
        System.out.print("Enter your age :\t");
        int age = read.nextInt();
        customerCollection.add(new Customer(name, email, password, phone, address, age));
    }

    /**
     * Searches for customer with the given ID and displays the customers' data if
     * found.
     *
     * @param ID of the searching/required customer
     */
    public void searchUser(String ID) {
        boolean isFound = false;
        Customer customerWithTheID = customerCollection.get(0);
        for (Customer c : customerCollection) {
            if (ID.equals(c.getUserID())) {
                System.out.printf("%-50sCustomer Found...!!!Here is the Full Record...!!!\n\n\n", " ");
                displayHeader();
                isFound = true;
                customerWithTheID = c;
                break;
            }
        }
        if (isFound) {
            System.out.println(customerWithTheID.toString(1));
            System.out.printf(
                    "%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+\n",
                    "");
        } else {
            System.out.printf("%-50sNo Customer with the ID %s Found...!!!\n", " ", ID);
        }
    }

    /**
     * Returns true if the given emailID is already registered, false otherwise
     *
     * @param emailID to be checked in the list
     * @return true if email is already registered, false otherwise
     */
    public boolean isUniqueData(String emailID) {
        boolean isUnique = false;
        for (Customer c : customerCollection) {
            if (emailID.equals(c.getEmail())) {
                isUnique = true;
                break;
            }
        }
        return isUnique;
    }

    /**
     * Edits user information based on the provided ID
     * 
     * @param ID the ID of the user to edit
     */
    public void editUserInfo(String ID) {
        boolean isFound = false;
        Scanner read = new Scanner(System.in);
        for (Customer c : customerCollection) {
            if (ID.equals(c.getUserID())) {
                isFound = true;
                System.out.print("\nEnter the new name of the Passenger:\t");
                String name = read.nextLine();
                c.setName(name);
                System.out.print("Enter the new email address of Passenger " + name + ":\t");
                c.setEmail(read.nextLine());
                System.out.print("Enter the new Phone number of Passenger " + name + ":\t");
                c.setPhone(read.nextLine());
                System.out.print("Enter the new address of Passenger " + name + ":\t");
                c.setAddress(read.nextLine());
                System.out.print("Enter the new age of Passenger " + name + ":\t");
                c.setAge(read.nextInt());
                displayCustomersData(false);
                break;
            }
        }
        if (!isFound) {
            System.out.printf("%-50sNo Customer with the ID %s Found...!!!\n", " ", ID);
        }
    }

    /**
     * Deletes a user with the specified ID
     * 
     * @param ID the ID of the user to delete
     */
    public void deleteUser(String ID) {
        boolean isFound = false;
        Iterator<Customer> iterator = customerCollection.iterator();
        while (iterator.hasNext()) {
            Customer customer = iterator.next();
            if (ID.equals(customer.getUserID())) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            iterator.remove();
            System.out.printf("\n%-50sPrinting all  Customer's Data after deleting Customer with the ID %s.....!!!!\n",
                    "", ID);
            displayCustomersData(false);
        } else {
            System.out.printf("%-50sNo Customer with the ID %s Found...!!!\n", " ", ID);
        }
    }

    /**
     * Shows the customers' data in formatted way.
     * 
     * @param showHeader to check if we want to print ascii art for the customers'
     *                   data.
     */
    public void displayCustomersData(boolean showHeader) {
        displayHeader();
        Iterator<Customer> iterator = customerCollection.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            i++;
            Customer c = iterator.next();
            System.out.println(c.toString(i));
            System.out.printf(
                    "%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+\n",
                    "");
        }
    }

    /**
     * Shows the header for printing customers data
     */
    public void displayHeader() {
        System.out.println();
        System.out.printf(
                "%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+\n",
                "");
        System.out.printf(
                "%10s| SerialNum  |   UserID   | Passenger Names                  | Age     | EmailID\t\t       | Home Address\t\t\t     | Phone Number\t       |%n",
                "");
        System.out.printf(
                "%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+\n",
                "");
        System.out.println();
    }

    /**
     * Adds space between userID to increase its readability
     * <p>
     * Example:
     * </p>
     * <b>"920 191" is much more readable than "920191"</b>
     *
     * @param randomID id to add space
     * @return randomID with added space
     */
    public String randomIDDisplay(String randomID) {
        StringBuilder newString = new StringBuilder();
        for (int i = 0; i <= randomID.length(); i++) {
            if (i == 3) {
                newString.append(" ").append(randomID.charAt(i));
            } else if (i < randomID.length()) {
                newString.append(randomID.charAt(i));
            }
        }
        return newString.toString();
    }

    /**
     * Associates a new flight with the specified customer
     *
     * @param f flight to associate
     */
    public void addNewFlightToCustomerList(Flight f) {
        this.flightsRegisteredByUser.add(f);
    }

    /**
     * Adds numOfTickets to already booked flights
     * 
     * @param index        at which flight is registered in the arraylist
     * @param numOfTickets how many tickets to add
     */
    public void addExistingFlightToCustomerList(int index, int numOfTickets) {
        int newNumOfTickets = numOfTicketsBookedByUser.get(index) + numOfTickets;
        this.numOfTicketsBookedByUser.set(index, newNumOfTickets);
    }

    /**
     * Returns String consisting of customers data(name, age, email etc...) for
     * displaying.
     * randomIDDisplay() adds space between the userID for easy readability.
     *
     * @param i for serial numbers.
     * @return customers data in String
     */
    private String toString(int i) {
        return String.format("%10s| %-10d | %-10s | %-32s | %-7s | %-27s | %-35s | %-23s |", "", i,
                randomIDDisplay(userID), name, age, email, address, phone);
    }

    // ************************************************************ Static Methods ************************************************************

    /**
     * Get the collection of all customers
     * 
     * @return the list of all customers
     */
    public static List<Customer> getCustomerCollection() {
        return customerCollection;
    }

    // ************************************************************ Getters and Setters ************************************************************

    /**
     * Get the list of flights registered by this user
     * 
     * @return list of flights
     */
    public List<Flight> getFlightsRegisteredByUser() {
        return flightsRegisteredByUser;
    }

    /**
     * Get the user's password
     * 
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the user's phone number
     * 
     * @return phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Get the user's address
     * 
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Get the user's email
     * 
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get the user's age
     * 
     * @return age
     */
    public int getAge() {
        return age;
    }

    /**
     * Get the user's ID
     * 
     * @return user ID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Get the user's name
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the list of number of tickets booked by user
     * 
     * @return list of ticket counts
     */
    public List<Integer> getNumOfTicketsBookedByUser() {
        return numOfTicketsBookedByUser;
    }

    /**
     * Set the user's name
     * 
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the user's email
     * 
     * @param email new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set the user's phone number
     * 
     * @param phone new phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Set the user's address
     * 
     * @param address new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Set the user's age
     * 
     * @param age new age
     */
    public void setAge(int age) {
        this.age = age;
    }
}