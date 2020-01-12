package ir.saeidbabaei.bank.moneytransfer.service;

import java.util.Date;
import java.util.List;

import ir.saeidbabaei.bank.moneytransfer.exception.DuplicateAccountException;
import ir.saeidbabaei.bank.moneytransfer.exception.InsufficientBalanceException;
import ir.saeidbabaei.bank.moneytransfer.exception.InvalidAccountException;
import ir.saeidbabaei.bank.moneytransfer.model.Account;

/**
 * An interface for simple bank account operation.
 * 
 * @author Saeid Babaei
 * @version 1.0
 * @since 1.0
 */
public interface AccountServiceIntf {
	 	
		/** Creates an account with the specified account number
		 * and name and create date and balance.
		 * 
		 * @param 		accno						Number of account.
		 * @param		name						Name of account owner.
		 * @param		createDate					Create date of account.
		 * @param		balance						Initial balance of account.
		 * 
		 * @return									Account
		 * @exception	DuplicateAccountException	if account number already exist.
		*/
 		public Account createAccount(long accno, String name, Date createDate, double balance) throws DuplicateAccountException;
 
		/** Close an account with the specified account number
		 * 
		 * @param 		accno						Number of account.
		 * @exception	InvalidAccountException		if account number does not exist.
		*/
        public void closeAccount(long accno) throws InvalidAccountException;
 
		/** Deposit an amount to an account with the specified account number
		 * 
		 * @param 		accno						Number of account.
		 * @param		amount						Amount to be deposit
		 * 
		 * @return									Account
		 * @exception	InvalidAccountException		if account number does not exist.		 
		*/
        public Account deposit(long accno, double amount) throws InvalidAccountException;
 
		/** Withdraw an amount from an account with the specified account number
		 * 
		 * @param 		accno							Number of account.
		 * @param		amount							Amount to be withdraw
		 * 
		 * @return										Account
		 * @exception	InvalidAccountException			if account number does not exist.
		 * @exception	InsufficientBalanceException	if the balance of account is less than amount.  
		*/
        public Account withdraw(long accno, double amount) throws InvalidAccountException,InsufficientBalanceException;
     
		/** Find an amount with the specified account number
		 * 
		 * @param 		accno						Number of account.
		 * 
		 * @return									Account
		 * @exception	InvalidAccountException		if account number does not exist.
		*/
        public Account findAccount(long accno) throws InvalidAccountException;
 
		/** Return list of all account
		 *  
		 * @return					List of all accounts
		*/
        public List<Account> listAllAccount();
}