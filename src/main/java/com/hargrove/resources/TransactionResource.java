package com.hargrove.resources;

import com.hargrove.models.CheckingAccount;
import com.hargrove.models.Transaction;
import com.hargrove.services.CheckingAccountService;
import com.hargrove.services.TransactionService;
import com.hargrove.utilities.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.List;
import java.util.UUID;

@Path("transaction")
public class TransactionResource {
    TransactionService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transaction> getTransactions(@Context HttpServletResponse response,
                                             @Context ContainerRequestContext cr) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");

        // Received From AuthorizationFilter
        String userID = (String) cr.getProperty("userID");
        System.out.println("USER ID: " + userID);

        if(userID == null || userID.equals(""))
            return null;

        service = new TransactionService();
        List<Transaction> transactions = service.get(userID);

        return transactions;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTransaction(Transaction transaction, @Context UriInfo uriInfo,
                                      @Context HttpServletResponse response, @Context ContainerRequestContext cr) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.addHeader("Access-Control-Allow-Methods", "POST, OPTIONS");

        // Received From AuthorizationFilter
        String userID = (String) cr.getProperty("userID");
        System.out.println("USER ID: " + userID);

        // The user id taken from the JWT is used here to find the account associated with the user.
        UUID id = UUID.fromString(userID);
        CheckingAccountService checkingAccountService = new CheckingAccountService();
        CheckingAccount account = checkingAccountService.getAccount(id);
        transaction.setOriginAccountNumber(account.getAccountNumber());

        System.out.println("Type: " + transaction.getCategory());
        System.out.println("Origin: " + transaction.getOriginAccountNumber());
        System.out.println("Destination: " + transaction.getDestinationAccountNumber());
        System.out.println("Amount: " + transaction.getAmount());

        service = new TransactionService();
        service.create(transaction);

        return Response.ok().build();
    }

    @OPTIONS
    public void preflightResponse(@Context HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        System.out.println("CheckingAccountResource: Preflight Endpoint Reached.");
    }
}
