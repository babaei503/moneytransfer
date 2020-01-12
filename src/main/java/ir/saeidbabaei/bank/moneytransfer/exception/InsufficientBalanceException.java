package ir.saeidbabaei.bank.moneytransfer.exception;

import ir.saeidbabaei.bank.moneytransfer.common.ApplicationConstants;

/**
 * An exception throw in case of the balance of
 * account is insufficient for withdraw
 * 
 * @author Saeid Babaei
 * @version 1.0
 * @since 1.0
 */
public class InsufficientBalanceException extends Exception{

	private static final long serialVersionUID = 1L;

	public InsufficientBalanceException(long accountId){
        super(ApplicationConstants.INSUFFICIENT_BALANCE + accountId);
    }
}