package ir.saeidbabaei.bank.moneytransfer.model;

/**
 * An entity for account transaction
 * define sender and recipient and the amount of money
 * to transfer between them
 * 
 * @author Saeid Babaei
 * @version 1.0
 * @since 1.0
 */
public class AccountTransaction {
	
    private long senderAccountId;
    private long recipientAccountId;
    
    private double amount;
       
	public AccountTransaction() {
		super();
	}

	/** Creates an AccountTransaction with the specified sender account id
	 * and recipient account id and amount of money.
	 * 
	 * @param 	senderAccountId		Id of sender account.
	 * @param	recipientAccountId	Id of recipient account.
	 * @param	amount				Amount of money to be transfer.
	*/
	public AccountTransaction(long senderAccountId, long recipientAccountId, double amount) {
		super();
		this.senderAccountId = senderAccountId;
		this.recipientAccountId = recipientAccountId;
		this.amount = amount;
	}

	public long getSenderAccountId() {
		return senderAccountId;
	}

	public void setSenderAccountId(long senderAccountId) {
		this.senderAccountId = senderAccountId;
	}

	public long getRecipientAccountId() {
		return recipientAccountId;
	}

	public void setRecipientAccountId(long recipientAccountId) {
		this.recipientAccountId = recipientAccountId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
       
} 