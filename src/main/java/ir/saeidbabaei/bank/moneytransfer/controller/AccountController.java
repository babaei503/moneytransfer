package ir.saeidbabaei.bank.moneytransfer.controller;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import ir.saeidbabaei.bank.moneytransfer.exception.DuplicateAccountException;
import ir.saeidbabaei.bank.moneytransfer.exception.InsufficientBalanceException;
import ir.saeidbabaei.bank.moneytransfer.exception.InvalidAccountException;
import ir.saeidbabaei.bank.moneytransfer.model.Account;
import ir.saeidbabaei.bank.moneytransfer.model.AccountTransaction;
import ir.saeidbabaei.bank.moneytransfer.service.AccountService;

/**
 * RESTful API for simple bank account operation.
 * 
 * @author Saeid Babaei
 * @version 1.0
 * @since 1.0
 */
@Path("/api/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountController {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2-eclipselink");
	EntityManager manager = factory.createEntityManager();
	
	AccountService service = new AccountService(manager);

	/** Creates an account with the specified account number
	 * and name and create date and balance.
	 * 
	 * @param 		account						Account info.
	 * 
	 * @return									Response 200 OK. Account info.
	 * @exception	BadRequestException			In case of the format of parameter is incorrect.
	 * @exception	DuplicateAccountException	If account number already exist.
	*/
	@POST @Consumes("application/json")
    @Path("/create")
    public Response create(Account account) {

        try {
        	
        	long id = Long.valueOf(account.getAccountNumber());
        	double balance = Double.valueOf(account.getBalance());
        	String name = account.getName();
        	
    		manager.getTransaction().begin();
    		Account result = service.createAccount(id, name, new Date(), balance);
    		manager.getTransaction().commit();
    		
            return Response.status(Response.Status.OK).entity(result.toString()).build();
            
        }catch (BadRequestException | DuplicateAccountException e){
        	
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
            
        }
    }

	
	/** Find an account with the specified account number
	 * 
	 * @param 		id							Account number.
	 * 
	 * @return									Response 200 OK. Account info.
	 * @exception	InvalidAccountException		if account number does not exist.
	 * @exception	BadRequestException			In case of the format of parameter is incorrect.
	*/
    @GET
    @Path("/{id}")
    public Response getAccount(@PathParam("id") String id){
    	      
        try {
        	
            long accountId = Long.valueOf(id);
            
            Account account = service.findAccount(accountId);
            
            return Response.status(Response.Status.OK).entity(account.toString()).build();
           
        }catch (BadRequestException | InvalidAccountException e){      	
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();            
        }
    }
 
    
	/** Withdraw an amount from an account and deposit the amount
	 * into another account with the specified account number
	 * 
	 * @param 		accountTransaction				Sender and recipient account number and amount of money.
	 * 
	 * @return										Response 200 OK. Accounts info.
	 * @exception	InvalidAccountException			if account numbers do not exist.
	 * @exception	InsufficientBalanceException	if the balance of account is less than amount. 
	 * @exception	BadRequestException				In case of the format of parameter is incorrect. 
	*/
	@POST @Consumes("application/json")
    @Path("/transfer")
    public Response transfer(AccountTransaction accountTransaction) {

        try {
        	
        	long senderAccountId = Long.valueOf(accountTransaction.getSenderAccountId());
        	long recipientAccountId = Long.valueOf(accountTransaction.getRecipientAccountId());
        	double amount = Double.valueOf(accountTransaction.getAmount());
        	
    		manager.getTransaction().begin();
    		Account senderAccount = service.withdraw(senderAccountId, amount);
    		Account recipientAccount = service.deposit(recipientAccountId, amount);
    		manager.getTransaction().commit();
    		
    		String output = "{\r\n\"accounts\":[\r\n"
    						+ senderAccount.toString() 
    						+ ",\r\n"
    						+ recipientAccount.toString()
    						+ "\r\n]\r\n}";
            return Response.status(Response.Status.OK).entity(output).build();
            
        }catch (BadRequestException | InvalidAccountException | InsufficientBalanceException e){
        	
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
            
        }
    }


	/** Close an account with the specified account number
	 * 
	 * @param 		id							Account number.
	 * @exception	InvalidAccountException		if account number does not exist.
	 * @exception	BadRequestException			In case of the format of parameter is incorrect. 
	*/	
    @GET
    @Path("/close/{id}")
    public Response closeAccount(@PathParam("id") String id){
    	      
        try {
        	
            long accountId = Long.valueOf(id);
            
    		manager.getTransaction().begin();
            service.closeAccount(accountId);
    		manager.getTransaction().commit();
            
            return Response.status(Response.Status.OK).build();
           
        }catch (BadRequestException | InvalidAccountException e){      	
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();            
        }
    }	
    
}