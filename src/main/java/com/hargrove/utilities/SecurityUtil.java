package com.hargrove.utilities;

import java.util.ArrayList;
import java.util.List;

public abstract class SecurityUtil {
    protected final String PATH_USER = "http://localhost:8080/bankingapp/api/user";
    protected final String PATH_USER_INDIVIDUAL = "http://localhost:8080/bankingapp/api/user/individual";
    protected final String PATH_ADMIN = "http://localhost:8080/bankingapp/api/admin";
    protected final String PATH_CHECKING = "http://localhost:8080/bankingapp/api/checking";
    protected final String PATH_TRANSACTION = "http://localhost:8080/bankingapp/api/transaction";
    protected final List<String> SECURED_ENDPOINTS = new ArrayList<>();
    protected final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    protected final String ROLE_ADMIN = "ROLE_ADMIN";
    protected final String ROLE_BANKER = "ROLE_BANKER";
}
