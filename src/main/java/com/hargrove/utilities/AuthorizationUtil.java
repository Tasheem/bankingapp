package com.hargrove.utilities;

import java.util.ArrayList;
import java.util.List;

public class AuthorizationUtil extends AuthenticationUtil {

    private List<String> adminResources = new ArrayList<>();
    private List<String> userResources = new ArrayList<>();

    public boolean hasPermission(String path, String httpMethod, String userRole) {
        String resource = path + ":" + httpMethod;
        String resourceAndPermissions = null;
        for(String endpoint : SECURED_ENDPOINTS) {
            if(endpoint.contains(resource)) {
                resourceAndPermissions = endpoint;
                break;
            }
        }

        if(resourceAndPermissions == null) {
            System.out.println("Resource does not exist in SECURED_ENDPOINTS collection.");
            return false;
        }

        if(resourceAndPermissions.contains(userRole))
            return true;
        else
            return false;
    }
}
