package ir.saeidbabaei.bank.moneytransfer;
  
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ir.saeidbabaei.bank.moneytransfer.exception.DuplicateAccountException;
import ir.saeidbabaei.bank.moneytransfer.exception.InsufficientBalanceException;
import ir.saeidbabaei.bank.moneytransfer.exception.InvalidAccountException;
import ir.saeidbabaei.bank.moneytransfer.model.Account;
import ir.saeidbabaei.bank.moneytransfer.service.AccountService;
import ir.saeidbabaei.bank.moneytransfer.service.AccountServiceIntf;

public class AccountServiceTest {

    private static Account account = new Account();
    
    static EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2-eclipselink");
	static EntityManager manager = factory.createEntityManager();
	
    private static AccountServiceIntf accountServiceIntf = new AccountService(manager);
    
    private static Date today = new Date();

    @BeforeClass
    public static void setUp(){

    	account.setAccountNumber(1L);
    	account.setName("Saeid");
    	account.setCreateDate(today);
    	account.setBalance(1000D);
    	
    }

    @AfterClass
    public static void tearDown(){

    }
    
    @Before
    public void beforeTest() throws InvalidAccountException{
    	
		manager.getTransaction().begin();
		
		List<Account> list = accountServiceIntf.listAllAccount();

        for(Account account:list) {
        	accountServiceIntf.closeAccount(account.getAccountNumber());
        }
        
		manager.getTransaction().commit();
		
    }
    
    @After
    public void afterTest() throws InvalidAccountException{
    	
        
    }

    @Test
    public void testCreateAccount_ShouldCreateAndReturnAccount() throws DuplicateAccountException, InvalidAccountException {

        Assert.assertEquals(account.toString(), accountServiceIntf.createAccount(1L, "Saeid", today, 1000D).toString());
        
    }
    
    @Test
    public void testCreateAccount_ShouldThrowDuplicateAccountException() throws DuplicateAccountException{

        accountServiceIntf.createAccount(1L, "Saeid", today, 1000D);
        Assert.assertThrows(DuplicateAccountException.class, () -> accountServiceIntf.createAccount(1L, "Saeid", today, 1000D));
        
    }
    
    @Test
    public void testFindAccount_ShouldReturnAccount() throws DuplicateAccountException, InvalidAccountException{
    	
        accountServiceIntf.createAccount(1L, "Saeid", today, 1000D);
        Assert.assertEquals(account.toString(), accountServiceIntf.findAccount(1L).toString());
        
    }
    
    @Test
    public void testFindAccount_ShouldThrowInvalidAccountException(){
    	
        Assert.assertThrows(InvalidAccountException.class, () -> accountServiceIntf.findAccount(2L));
        
    }
    
    @Test
    public void testCloseAccount_ShouldCloseAccount() throws DuplicateAccountException, InvalidAccountException{
    	
    	accountServiceIntf.createAccount(1L, "Saeid", today, 1000D);
    	
    	accountServiceIntf.closeAccount(1L);
    	
        Assert.assertThrows(InvalidAccountException.class, () -> accountServiceIntf.findAccount(1L));
        
    }
    
    @Test
    public void testCloseAccount_ShouldThrowInvalidAccountException() throws InvalidAccountException{
    	   	
        Assert.assertThrows(InvalidAccountException.class, () -> accountServiceIntf.closeAccount(1L));
        
    }
    
    
    @Test
    public void testDeposit_ShouldDepositAmount() throws DuplicateAccountException, InvalidAccountException {

		manager.getTransaction().begin();
		
    	accountServiceIntf.createAccount(1L, "Saeid", today, 1000D);
    	
    	accountServiceIntf.deposit(1L, 50D);

        Assert.assertEquals(1050D, accountServiceIntf.findAccount(1L).getBalance(),0);
        
		manager.getTransaction().commit();
    }
    
    @Test
    public void testDeposit_ShouldThrowInvalidAccountException() throws InvalidAccountException {
   	
        Assert.assertThrows(InvalidAccountException.class, () -> accountServiceIntf.deposit(1L, 50D));

    }
    
    @Test
    public void testWithdraw_ShouldWithdrawAmount() throws DuplicateAccountException, InvalidAccountException, InsufficientBalanceException {

		manager.getTransaction().begin();
		
    	accountServiceIntf.createAccount(1L, "Saeid", today, 1000D);
    	
    	accountServiceIntf.withdraw(1L, 50D);

        Assert.assertEquals(950D, accountServiceIntf.findAccount(1L).getBalance(),0);
        
		manager.getTransaction().commit();
    }
    
    @Test
    public void testWithdraw_ShouldThrowInvalidAccountException() throws InvalidAccountException {
   	
        Assert.assertThrows(InvalidAccountException.class, () -> accountServiceIntf.withdraw(1L, 50D));

    }
    
    @Test
    public void testWithdraw_ShouldThrowInsufficientBalanceException() throws InsufficientBalanceException, DuplicateAccountException {
   		
    	accountServiceIntf.createAccount(1L, "Saeid", today, 1000D);
    	
        Assert.assertThrows(InsufficientBalanceException.class, () -> accountServiceIntf.withdraw(1L, 1050D));

    }
    
}

