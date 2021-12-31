package com.hargrove.utilities;

import jakarta.ws.rs.HttpMethod;

public class AuthenticationUtil extends SecurityUtil{

    public AuthenticationUtil constructEndpoints() {
        /*
         * Populating list from parent class.
         * Format ---> Path:Method|role1,role2,role3,...,roleN
         * Example ---> http://localhost:8080/bankingapp/api/user:PUT|ROLE_ADMIN,ROLE_CUSTOMER
         */
        SECURED_ENDPOINTS.add(PATH_USER + ":" + HttpMethod.GET + "|" + super.ROLE_ADMIN);
        SECURED_ENDPOINTS.add(PATH_USER + ":" + HttpMethod.PUT + "|" + super.ROLE_ADMIN + "," + super.ROLE_CUSTOMER);
        SECURED_ENDPOINTS.add(PATH_USER + ":" + HttpMethod.DELETE + "|" + super.ROLE_ADMIN + "," + super.ROLE_BANKER +
                "," + super.ROLE_CUSTOMER);
        SECURED_ENDPOINTS.add(PATH_USER_INDIVIDUAL + ":" + HttpMethod.GET + "|" + super.ROLE_ADMIN + "," +
                super.ROLE_BANKER + "," + super.ROLE_CUSTOMER);
        SECURED_ENDPOINTS.add(PATH_ADMIN + ":" + HttpMethod.POST + "|" + super.ROLE_ADMIN);
        SECURED_ENDPOINTS.add(PATH_CHECKING + ":" + HttpMethod.POST + "|" + super.ROLE_BANKER + "," + super.ROLE_ADMIN);
        SECURED_ENDPOINTS.add(PATH_CHECKING + ":" + HttpMethod.GET + "|" +  super.ROLE_CUSTOMER + ","
                + super.ROLE_BANKER + "," + super.ROLE_ADMIN);
        SECURED_ENDPOINTS.add(PATH_TRANSACTION + ":" + HttpMethod.POST + "|" + super.ROLE_CUSTOMER + ","
                + super.ROLE_BANKER + "," + super.ROLE_ADMIN);
        SECURED_ENDPOINTS.add(PATH_TRANSACTION + ":" + HttpMethod.GET + "|" + super.ROLE_CUSTOMER + ","
                + super.ROLE_BANKER + "," + super.ROLE_ADMIN);

        return this;
    }

    public boolean isEndpointSecured(String path, String method) {
        String requestDestination = path + ":" + method;

        for(String endpoint : SECURED_ENDPOINTS) {
            if(endpoint.contains(requestDestination))
                return true;
        }

        return false;
    }
}
