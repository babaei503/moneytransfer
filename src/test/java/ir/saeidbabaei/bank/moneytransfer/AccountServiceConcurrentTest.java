package ir.saeidbabaei.bank.moneytransfer;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
 
import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;

import ir.saeidbabaei.bank.moneytransfer.controller.AccountController;
import ir.saeidbabaei.bank.moneytransfer.exception.DuplicateAccountException;
import ir.saeidbabaei.bank.moneytransfer.exception.InvalidAccountException;
import ir.saeidbabaei.bank.moneytransfer.model.Account;
import ir.saeidbabaei.bank.moneytransfer.model.AccountTransaction;

 
@RunWith(ConcurrentTestRunner.class)
public class AccountServiceConcurrentTest extends JerseyTest {
    
    
    private static Account senderaccount = new Account();
    private static AccountTransaction accountTransaction = new AccountTransaction();
    private static Account recipientAccount = new Account();
    
    private static Date today = new Date();
    
    private final static int THREAD_COUNT = 15;
 
	@Override
	public Application configure() {
		
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return new ResourceConfig(AccountController.class);
		
	}
	
    @Before
    public void initialCount() throws DuplicateAccountException {
    	
		senderaccount.setAccountNumber(1L);
		senderaccount.setName("Saeid");
		senderaccount.setCreateDate(today);
		senderaccount.setBalance(1000D);
    		
		accountTransaction.setSenderAccountId(1L);
		accountTransaction.setRecipientAccountId(2L);
		accountTransaction.setAmount(50D);
		
		recipientAccount.setAccountNumber(2L);
		recipientAccount.setName("Babaei");
		recipientAccount.setCreateDate(today);
		recipientAccount.setBalance(500D);
   				
		target("/api/account/create").request().post(Entity.entity(senderaccount, MediaType.APPLICATION_JSON));
		target("/api/account/create").request().post(Entity.entity(recipientAccount, MediaType.APPLICATION_JSON));
			
    }
    
 
    @Test
    @ThreadCount(THREAD_COUNT)
    public void transfer() throws InvalidAccountException {
    	
    	Response output = target("/api/account/transfer").request().post(Entity.entity(accountTransaction, MediaType.APPLICATION_JSON));
    	System.out.println(output.readEntity(String.class));
    }
 
    @After
    public void testBalance() throws InvalidAccountException {
    	
    	Response senderOutput = target("/api/account/1").request().get();
    	Response recipientOutput = target("/api/account/2").request().get();
    	
        assertEquals("Balance should be 250", 250D, senderOutput.readEntity(Account.class).getBalance(),0);
        assertEquals("Balance should be 1250", 1250D, recipientOutput.readEntity(Account.class).getBalance(),0);
        
    }
}