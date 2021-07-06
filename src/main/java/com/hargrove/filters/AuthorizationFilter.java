package com.hargrove.filters;

import com.hargrove.models.Role;
import com.hargrove.utilities.AuthorizationUtil;
import com.hargrove.utilities.JJWTAuthUtil;
import com.hargrove.utilities.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.lang.Maps;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {
    JwtUtil util;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        // This is the property used for the AuthenticationFilter to communicate with this filter.
        String property = (String) requestContext.getProperty("Authenticated");
        boolean authorizationRequired = Boolean.parseBoolean(property);

        if(!authorizationRequired) {
            System.out.println("Authorization not required");
            return;
        }

        String location = requestContext.getUriInfo().getAbsolutePath().toString();
        String method = requestContext.getMethod();

        /* ---------------------------- JWT Operations ---------------------------- */
        // Authorization header should not be missing at this point, if the request
        // made it through the AuthenticationFilter.
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.replaceFirst("Bearer ", "");

        util = new JJWTAuthUtil();
        Role role = null;
        Claims jwtBody = null;
        try {
            jwtBody = Jwts.parserBuilder()
                    .deserializeJsonWith(new JacksonDeserializer(Maps.of("role", Role.class).build()))
                    .setSigningKey(util.getKey()).build().parseClaimsJws(token).getBody();
            role = jwtBody.get("role", Role.class);
            System.out.println("Role = " + role.getName());

            String userID = jwtBody.get("id", String.class);
            // System.out.println("USER ID: " + userID);
            requestContext.setProperty("userID", userID);
        } catch(Exception e) {
            System.out.println("Error with token in Authorization Filter.");
            System.out.println(e);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .header("Access-Control-Allow-Origin", "*").build());
        }

        /* ---------------------------- End JWT Operations ---------------------------- */

        AuthorizationUtil auth = (AuthorizationUtil) (new AuthorizationUtil().constructEndpoints());

        boolean isAuthorized = false;
        if(role != null) {
            isAuthorized = auth.hasPermission(location, method, role.getName());
        }
        else {
            System.out.println("No role claim in token body.");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .header("Access-Control-Allow-Origin", "*").build());
        }

        if(!isAuthorized) {
            System.out.println("AuthorizationFilter: User does not have permission to access resource.");
            requestContext.abortWith(Response.status(403,
                    "User does not have permission to access resource.")
                    .header("Access-Control-Allow-Origin", "*").build());
        }
    }
}
