package com.hargrove.resources;

import com.hargrove.beans.CheckingFilterBean;
import com.hargrove.models.CheckingAccount;
import com.hargrove.services.CheckingAccountService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.*;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("checking")
public class CheckingAccountResource {
    CheckingAccountService service;

    /*@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CheckingAccount> getChecking(@Context HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");

        service = new CheckingAccountService();
        List<CheckingAccount> accounts = service.getAllAccounts();

        return accounts;
    }*/

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CheckingAccount getChecking(@Context HttpServletResponse response, @Context ContainerRequestContext cr) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");

        // Received From AuthorizationFilter
        String userID = (String) cr.getProperty("userID");
        System.out.println("USER ID: " + userID);

        if(userID == null || userID.equals(""))
            return null;

        service = new CheckingAccountService();
        /*List<CheckingAccount> accounts = service.getAllAccounts();*/
        CheckingAccount account = service.getAccount(UUID.fromString(userID));

        return account;
    }

    // TODO: Change this endpoint to accept query string paramaters.
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createChecking(@BeanParam CheckingFilterBean checkingParam, @Context UriInfo uriInfo,
                                   @Context HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
        response.addHeader("Access-Control-Allow-Headers", "Authorization");
        response.addHeader("Access-Control-Allow-Methods", "POST");

        System.out.println("id: " + checkingParam.getUserID());
        System.out.println("Deposit: " + checkingParam.getBalance());

        service = new CheckingAccountService();
        CheckingAccount checking;
        try {
            UUID userID = UUID.fromString(checkingParam.getUserID());
            BigDecimal balance = new BigDecimal(checkingParam.getBalance());
            checking = service.create(userID, balance);
        } catch (Exception e) {
            return Response.serverError().build();
        }

        String newID = String.valueOf(checking.getId());
        URI uri = uriInfo.getAbsolutePathBuilder().path(newID).build();

        return Response.created(uri)
                .entity(newID)
                .build();
    }

    @OPTIONS
    public void preflightResponse(@Context HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        System.out.println("CheckingAccountResource: Preflight Endpoint Reached.");
    }
}
