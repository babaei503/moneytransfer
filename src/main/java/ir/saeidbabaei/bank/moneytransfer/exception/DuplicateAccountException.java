package ir.saeidbabaei.bank.moneytransfer.exception;

import ir.saeidbabaei.bank.moneytransfer.common.ApplicationConstants;

/**
 * An exception throw in case of request for new 
 * account with existing id
 * 
 * @author Saeid Babaei
 * @version 1.0
 * @since 1.0
 */
public class DuplicateAccountException extends Exception{

	private static final long serialVersionUID = 1L;

	public DuplicateAccountException(long accountId){
        super(ApplicationConstants.ACCOUNT_ALREADY_EXISTS + accountId);
    }
}