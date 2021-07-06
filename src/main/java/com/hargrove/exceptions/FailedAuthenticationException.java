package com.hargrove.exceptions;


/**
 * The FailedAuthenticationException is thrown when a query of the database
 * finds no matching pair of the provided username and password.
 *
 * @author Tasheem Hargrove
 */
public class FailedAuthenticationException extends Exception {

    static String message = "The user and/or password does not exist in the database";
    public FailedAuthenticationException() {
        super(message);
    }
}
