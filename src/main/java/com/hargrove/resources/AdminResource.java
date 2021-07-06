package com.hargrove.resources;

import com.hargrove.models.User;
import com.hargrove.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Path("admin")
public class AdminResource {
    UserService service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adminCreatesUser(User user, @Context UriInfo uriInfo,
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
        service.createUser(stringFields, genderInfo, user.getBirthday(), user.getRole().getName());

        String newID = String.valueOf(user.getID());
        URI uri = uriInfo.getAbsolutePathBuilder().path(newID).build();

        return Response.created(uri)
                .entity(newID)
                .build();
    }

    @OPTIONS
    public void preflightResponse(@Context HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        System.out.println("AdminResource: Preflight Endpoint Reached.");
    }
}
