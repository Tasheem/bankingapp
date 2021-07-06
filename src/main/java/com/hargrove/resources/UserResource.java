package com.hargrove.resources;

import com.hargrove.beans.UserFilterBean;
import com.hargrove.models.Login;
import com.hargrove.services.CheckingAccountService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.UriInfo;
import com.hargrove.models.User;
import com.hargrove.services.UserService;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("user")
public class UserResource {
    UserService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUser(@Context HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");

        service = new UserService();
        List<User> users = service.getAllUsers();

        return users;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user, @Context UriInfo uriInfo,
                               @Context HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST");
        response.addHeader("Access-Control-Allow-Credentials", "true");

        List<String> stringFields = new ArrayList<String>();
        stringFields.add(user.getFirstName());
        stringFields.add(user.getLastName());
        stringFields.add(user.getEmail());
        stringFields.add(user.getUsername());
        stringFields.add(user.getPassword());

        List<Enum<?>> genderInfo = new ArrayList<Enum<?>>();
        genderInfo.add(user.getGender());
        genderInfo.add(user.getPreferredPronoun());

        service = new UserService();
        service.createUser(stringFields, genderInfo, user.getBirthday());

        String newID = String.valueOf(user.getID());
        URI uri = uriInfo.getAbsolutePathBuilder().path(newID).build();

        return Response.created(uri)
                .entity(newID)
                .build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@BeanParam UserFilterBean filterBean, @Context ContainerRequestContext cr,
                           @Context HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Authorization");
        response.addHeader("Access-Control-Allow-Methods", "PUT");

        service = new UserService();
        // Received From AuthorizationFilter.
        String userID = (String) cr.getProperty("userID");
        System.out.println("USER ID: " + userID);

        System.out.println("Firstname: " + filterBean.getFirstName());
        System.out.println("Lastname: " + filterBean.getLastName());
        System.out.println("Password: " + filterBean.getPassword());
        System.out.println("Username: " + filterBean.getUsername());
        System.out.println("Email: " + filterBean.getEmail());
        System.out.println("Birthday: " + filterBean.getBirthday());
        System.out.println("Gender: " + filterBean.getGender());
        System.out.println("Preferred Pronoun: " + filterBean.getPreferredPronoun());

        if(filterBean.getFirstName() != null)
            service.updateName(userID, filterBean.getFirstName(), filterBean.getLastName());
        else if(filterBean.getPassword() != null) {
            User user = service.getUser(UUID.fromString(userID));
            if(!user.getUsername().equals(filterBean.getUsername())) {
                System.out.println("UserResource: Incorrect username.");
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            if(!user.getPassword().equals(filterBean.getPassword())) {
                System.out.println("UserResource: Incorrect password.");
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            service.updatePassword(userID, filterBean.getNewPassword());
        }
        else if(filterBean.getUsername() != null)
            service.updateUsername(userID, filterBean.getUsername());
        else if(filterBean.getEmail() != null)
            service.updateEmail(userID, filterBean.getEmail());
        else if(filterBean.getBirthday() != null)
            service.updateBirthday(userID, filterBean.getBirthday());
        else if(filterBean.getGender() != null)
            service.updateGender(userID, filterBean.getGender(), filterBean.getPreferredPronoun());

        return Response.status(204).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser(Login accountInfo, @Context ContainerRequestContext cr,
                           @Context HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.addHeader("Access-Control-Allow-Methods", "DELETE");

        service = new UserService();
        User user = service.getUser(accountInfo.getUsername(), accountInfo.getPassword());

        // Received From AuthorizationFilter.
        String loggedInUserId = (String) cr.getProperty("userID");
        System.out.println("USER ID: " + loggedInUserId);

        // This block prevents users from deleting other another user's account.
        if(!loggedInUserId.equals(user.getID().toString())) {
            System.out.println("If block reached. Status code should be 403");
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // Deleting bank account before user account since checking_accounts table is
        // dependent on users table in the database.
        CheckingAccountService checkingAccountService = new CheckingAccountService();
        boolean accountDeleted = checkingAccountService.deleteChecking(user.getID());
        if(!accountDeleted)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        boolean isSuccessful = service.deleteUser(user.getID());

        if(!isSuccessful) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } else {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }

    /*
     * When extra values are provided in the request header, the browser
     * first sends an OPTIONS request to the server.  It has to be confirmed
     * that the request origin and the request headers are allowed by the server.
     *
     * This method makes sure the response to the preflight request passes the "access
     * control check".
     */
    @OPTIONS
    public void preflightResponse(@Context HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        System.out.println("UserResource: Preflight Endpoint Reached.");
    }
}
