package com.hargrove.filters;

import com.hargrove.utilities.AuthenticationUtil;
import com.hargrove.utilities.ErrorMessageJSON;
import com.hargrove.utilities.JJWTAuthUtil;
import com.hargrove.utilities.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.util.Date;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    JwtUtil util;
    AuthenticationUtil auth;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        System.out.println("Method: " + requestContext.getMethod());

        util = new JJWTAuthUtil();
        auth = new AuthenticationUtil();

        String location = requestContext.getUriInfo().getAbsolutePath().toString();
        String method = requestContext.getMethod();

        boolean authenticationRequired = auth.constructEndpoints()
                .isEndpointSecured(location, method);

        if(!authenticationRequired) {
            System.out.println("required: " + authenticationRequired);
            // passing this information onto AuthorizationFilter to skip the authorization process.
            requestContext.setProperty("Authenticated", "False");
            return;
        }

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if(authHeader == null) {
            System.out.println("No Authorization Header Present.");
            // The header is added to satisfy CORS policy.
            requestContext.abortWith(Response.status(401, "No Authorization Header Present.")
                    .header("Access-Control-Allow-Origin", "*").build());
            return;
        }

        // Base 64 encoded string is all that is left after Bearer prefix is removed.
        String token = authHeader.replaceFirst("Bearer ", "");

        Date expiration = null;
        try {
            expiration = Jwts.parserBuilder().setSigningKey(util.getKey())
                    .build().parseClaimsJws(token).getBody().getExpiration();
            System.out.println("Valid Token: " + token);
            System.out.println("Request Key: " + util.getKeyAsString());
        } catch(ExpiredJwtException exp) {
            System.out.println("The expiration date has passed.");
            requestContext.abortWith(Response.status(401, "The Token Has Expired.")
                    .header("Access-Control-Allow-Origin", "*").entity(new ErrorMessageJSON()).build());
        } catch (Exception e) {
            System.out.println("Invalid Token: " + token);
            System.out.println("Request Key: " + util.getKeyAsString());
            System.out.println(e.getMessage());
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .header("Access-Control-Allow-Origin", "*").build());
        }

        // Passing information to next filter.
        requestContext.setProperty("Authenticated", "True");
    }
}
