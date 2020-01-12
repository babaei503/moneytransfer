package ir.saeidbabaei.bank.moneytransfer.exception;

import ir.saeidbabaei.bank.moneytransfer.common.ApplicationConstants;

/**
 * An exception throw in case of access to invalid account
 * 
 * @author Saeid Babaei
 * @version 1.0
 * @since 1.0
 */
public class InvalidAccountException extends Exception{

	private static final long serialVersionUID = 1L;

	public InvalidAccountException(long accountId){
        super(ApplicationConstants.INVALID_ACCOUNT + accountId);
    }
}