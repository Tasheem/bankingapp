package com.hargrove.resources;

import com.hargrove.exceptions.FailedAuthenticationException;
import com.hargrove.models.Login;
import com.hargrove.models.User;
import com.hargrove.utilities.JJWTAuthUtil;
import com.hargrove.utilities.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.*;

@Path("login")
public class LoginResource {

    JwtUtil util;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Login login, @Context UriInfo uriInfo,
                                     @Context HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.addHeader("Access-Control-Expose-Headers", "Authorization, Name");

        util = new JJWTAuthUtil();

        try {
            User user = util.checkBasicAuth(login.getUsername(), login.getPassword());
            String token = util.issueToken(user, uriInfo.getAbsolutePath().toString());
            // System.out.println("Response Key: " + util.getKeyAsString());
            // System.out.println("FirstName: " + user.getFirstName());

            return Response.ok().header("Authorization", "Bearer " + token)
                    .header("Name", user.getFirstName()).build();
        } catch (FailedAuthenticationException e) {
            System.out.println(e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @OPTIONS
    public void preflightResponse(@Context HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.addHeader("Access-Control-Allow-Methods", "POST");
        response.addHeader("Access-Control-Expose-Headers", "Authorization, Name");
    }
}
