package com.hargrove.utilities;

public class ErrorMessageJSON {
    public String message;
    public int status;

    public ErrorMessageJSON() {
        message = "The token has expired";
        status = 401;
    }
}
