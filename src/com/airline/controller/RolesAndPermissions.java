package com.airline.controller;

import com.airline.model.customer. Customer;

/**
 * Controller class that handles authentication and authorization for the airline reservation system.
 * This class verifies user credentials and determines access permissions.
 */
public class RolesAndPermissions {
    
    // ************************************************************ Fields ************************************************************
    private static String[][] adminUserNameAndPassword = new String[10][2];
    
    // ************************************************************ Constructors ************************************************************
    
    /**
     * Default constructor
     */
    public RolesAndPermissions() {
        // Initialize default admin credentials if not already set
        if (adminUserNameAndPassword[0][0] == null) {
            adminUserNameAndPassword[0][0] = "root";
            adminUserNameAndPassword[0][1] = "root";
        }
    }
    
    // ************************************************************ Public Methods ************************************************************

    /**
     * Checks if the admin with specified credentials is registered or not.
     * 
     * @param username of the admin
     * @param password of the admin
     * @return -1 if admin not found, 0 if default admin, 1 if privileged admin
     */
    public int isPrivilegedUserOrNot(String username, String password) {
        int isFound = -1;
        for (int i = 0; i < adminUserNameAndPassword.length; i++) {
            if (username.equals(adminUserNameAndPassword[i][0])) {
                if (password.equals(adminUserNameAndPassword[i][1])) {
                    // Default admin has standard privileges (can only view data)
                    if (i == 0 && "root".equals(username) && "root".equals(password)) {
                        isFound = 0;
                    } else {
                        isFound = 1; // Privileged admin
                    }
                    break;
                }
            }
        }
        return isFound;
    }

    /**
     * Checks if the passenger with specified credentials is registered or not.
     * 
     * @param email of the specified passenger
     * @param password of the specified passenger
     * @return "1-userID" if the passenger is registered, else "0"
     */
    public String isPassengerRegistered(String email, String password) {
        String isFound = "0";
        for (Customer c : Customer.getCustomerCollection()) {
            if (email.equals(c.getEmail())) {
                if (password.equals(c.getPassword())) {
                    isFound = "1-" + c.getUserID();
                    break;
                }
            }
        }
        return isFound;
    }
    
    /**
     * Adds a new admin to the system
     * 
     * @param username the username for the new admin
     * @param password the password for the new admin
     * @return true if admin was added successfully, false if the admin list is full
     */
    public boolean addNewAdmin(String username, String password) {
        for (int i = 0; i < adminUserNameAndPassword.length; i++) {
            if (adminUserNameAndPassword[i][0] == null) {
                adminUserNameAndPassword[i][0] = username;
                adminUserNameAndPassword[i][1] = password;
                return true;
            }
        }
        return false; // Admin list is full
    }
    
    // ************************************************************ Getters and Setters ************************************************************
    
    /**
     * Get the admin credentials array
     * 
     * @return the admin username and password array
     */
    public static String[][] getAdminUserNameAndPassword() {
        return adminUserNameAndPassword;
    }
}