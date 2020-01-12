package ir.saeidbabaei.bank.moneytransfer.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;

import ir.saeidbabaei.bank.moneytransfer.exception.DuplicateAccountException;
import ir.saeidbabaei.bank.moneytransfer.exception.InsufficientBalanceException;
import ir.saeidbabaei.bank.moneytransfer.exception.InvalidAccountException;
import ir.saeidbabaei.bank.moneytransfer.model.Account;


public class AccountService implements AccountServiceIntf {
	 
		protected EntityManager entityManager;
		 
		public AccountService(EntityManager entityManager) {
		                this.entityManager = entityManager;
		}
 
		public Account createAccount(long accno, String name, Date createDate, double balance) throws DuplicateAccountException {
			
			Account account = entityManager.find(Account.class, accno);
            if (account != null) {
            	throw new DuplicateAccountException(accno);
            }
            else
            {
                Account newAccount = new Account();
                newAccount.setAccountNumber(accno);
                newAccount.setName(name);
                newAccount.setCreateDate(createDate);
                newAccount.setBalance(balance);
                entityManager.persist(newAccount);
                return newAccount;
            }
        }
 
        public void closeAccount(long accno) throws InvalidAccountException {
        	
            Account account = entityManager.find(Account.class, accno);
            
            if (account != null) {
                entityManager.remove(account);
            }
            else
            {
            	throw new InvalidAccountException(accno);
            }
        }
 
        public Account deposit(long accno, double amount) throws InvalidAccountException {
        	
            Account account = entityManager.find(Account.class, accno);
            
            if (account != null) {
            	entityManager.lock(account, LockModeType.OPTIMISTIC);
            	account.setBalance(account.getBalance() + amount);
                return account;
            }
            else
            {
            	throw new InvalidAccountException(accno);
            }
        }
 
        public Account withdraw(long accno, double amount) throws InvalidAccountException,InsufficientBalanceException {
        	
            Account account = entityManager.find(Account.class, accno);
            
            if (account != null) {
                if (account.getBalance() >= amount)
                {
            	   entityManager.lock(account, LockModeType.OPTIMISTIC);
            	   account.setBalance(account.getBalance() - amount);
                }
                else
                {
                	throw new InsufficientBalanceException(accno);
                }
                return account;
            }
            else
            {
            	throw new InvalidAccountException(accno);
            }
        }
 
        public Account findAccount(long accno) throws InvalidAccountException{
            Account account = entityManager.find(Account.class, accno);
            if (account != null) {
                return account;
            }
            else
            {
            	throw new InvalidAccountException(accno);
            }      
        }
 
        public List<Account> listAllAccount() {
            TypedQuery<Account> query = entityManager.createNamedQuery(
                           "findAllAccount", Account.class);
            return query.getResultList();
        }

}