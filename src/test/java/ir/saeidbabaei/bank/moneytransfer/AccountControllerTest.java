package ir.saeidbabaei.bank.moneytransfer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.Test;

import ir.saeidbabaei.bank.moneytransfer.controller.AccountController;
import ir.saeidbabaei.bank.moneytransfer.model.Account;
import ir.saeidbabaei.bank.moneytransfer.model.AccountTransaction;
import ir.saeidbabaei.bank.moneytransfer.service.AccountService;
import ir.saeidbabaei.bank.moneytransfer.service.AccountServiceIntf;

public class AccountControllerTest extends JerseyTest {

	private static Date today = new Date();
	
    private static Account account = new Account();
    private static AccountTransaction accountTransaction = new AccountTransaction();
    private static Account recipientAccount = new Account();
	  
	@Override
	public Application configure() {
		
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return new ResourceConfig(AccountController.class);
		
	}
	
	@Before
	public void setup() throws Exception {
		
		account.setAccountNumber(1L);
		account.setName("Saeid");
		account.setCreateDate(today);
		account.setBalance(1000D);
		
		accountTransaction.setSenderAccountId(1);
		accountTransaction.setRecipientAccountId(2);
		accountTransaction.setAmount(50D);
		
		recipientAccount.setAccountNumber(2L);
		recipientAccount.setName("Babaei");
		recipientAccount.setCreateDate(today);
		recipientAccount.setBalance(500D);
		
	    EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2-eclipselink");
		EntityManager manager = factory.createEntityManager();
		
	    AccountServiceIntf accountServiceIntf = new AccountService(manager);
	    
		manager.getTransaction().begin();
		
		List<Account> list = accountServiceIntf.listAllAccount();

        for(Account account:list) {
        	accountServiceIntf.closeAccount(account.getAccountNumber());
        }
        
		manager.getTransaction().commit();
		
	}

	@Test
	public void testCreateAccount_ShouldCreateAccount() {
				
		Response output = target("/api/account/create").request().post(Entity.entity(account, MediaType.APPLICATION_JSON));
		assertEquals("Should return status 200", 200, output.getStatus());
	  
	}
	
	@Test
	public void testCreateAccount_ShouldReturnBadRequest_AccountAlreadyExists(){
				
		target("/api/account/create").request().post(Entity.entity(account, MediaType.APPLICATION_JSON));
		Response output = target("/api/account/create").request().post(Entity.entity(account, MediaType.APPLICATION_JSON));

		assertEquals("Should return status 400", 400, output.getStatus());
		assertEquals("Should return Account already exists: 1", "Account already exists: 1", output.readEntity(String.class));		
	  
	}
	
	@Test
	public void testGetAccount_ShouldReturnAccount() {
				
		target("/api/account/create").request().post(Entity.entity(account, MediaType.APPLICATION_JSON));
		Response output = target("/api/account/1").request().get();
		assertEquals("Should return status 200", 200, output.getStatus());
		assertNotNull("Should return account object as json", output.getEntity());
	  
	}
	
	@Test
	public void testGetAccount_ShouldReturnBadRequest_InvalidAccount(){
				
		Response output = target("/api/account/1").request().get();
		assertEquals("Should return status 400", 400, output.getStatus());
		assertEquals("Should return Invalid account with the account id: 1", "Invalid account with the account id: 1", output.readEntity(String.class));
  
	}
	
	@Test
	public void testTransfer_ShouldTransferAmount() {
				
		target("/api/account/create").request().post(Entity.entity(account, MediaType.APPLICATION_JSON));
		target("/api/account/create").request().post(Entity.entity(recipientAccount, MediaType.APPLICATION_JSON));
		
		Response output = target("/api/account/transfer").request().post(Entity.entity(accountTransaction, MediaType.APPLICATION_JSON));
				
		Response senderOutput = target("/api/account/1").request().get();
		Response recipientOutput = target("/api/account/2").request().get();
		
		account.setBalance(950D);
		recipientAccount.setBalance(550D);
				
		assertEquals("Should return status 200", 200, output.getStatus());
		assertNotNull("Should return account object as json", output.getEntity());
		assertEquals("Should deposit sender account", account.getBalance(), senderOutput.readEntity(Account.class).getBalance(),0);
		assertEquals("Should withdraw recipient account", recipientAccount.getBalance(), recipientOutput.readEntity(Account.class).getBalance(),0);
	  
	}
	
	@Test
	public void testTransfer_ShouldReturnBadRequest_InsufficientBalance() {
				
		target("/api/account/create").request().post(Entity.entity(account, MediaType.APPLICATION_JSON));
		target("/api/account/create").request().post(Entity.entity(recipientAccount, MediaType.APPLICATION_JSON));
		
		accountTransaction.setAmount(1500D);
		
		Response output = target("/api/account/transfer").request().post(Entity.entity(accountTransaction, MediaType.APPLICATION_JSON));
							
		assertEquals("Should return status 400", 400, output.getStatus());
		assertEquals("Should return Insufficient balance in account id: 1", "Insufficient balance in account id: 1", output.readEntity(String.class));
	  
	}
	
	@Test
	public void testTransfer_ShouldReturnBadRequest_InvalidAccount() {
				
		target("/api/account/create").request().post(Entity.entity(recipientAccount, MediaType.APPLICATION_JSON));
				
		Response output = target("/api/account/transfer").request().post(Entity.entity(accountTransaction, MediaType.APPLICATION_JSON));
							
		assertEquals("Should return status 400", 400, output.getStatus());
		assertEquals("Should return Invalid account with the account id: 1", "Invalid account with the account id: 1", output.readEntity(String.class));
	  
	}
	
	@Test
	public void testCloseAccount_ShouldCloseAccount() {
			
		target("/api/account/create").request().post(Entity.entity(account, MediaType.APPLICATION_JSON));
		
		Response output = target("/api/account/close/1").request().get();
		Response outputAccount = target("/api/account/1").request().get();
		assertEquals("Should return status 200", 200, output.getStatus());
		assertEquals("Should return Invalid account with the account id: 1", "Invalid account with the account id: 1", outputAccount.readEntity(String.class));
	  
	}
	
	@Test
	public void testCloseAccount_ShouldReturnBadRequest_InvalidAccount() {
				
		Response output = target("/api/account/close/1").request().get();

		assertEquals("Should return status 400", 400, output.getStatus());
		assertEquals("Should return Invalid account with the account id: 1", "Invalid account with the account id: 1", output.readEntity(String.class));
	  
	}

}