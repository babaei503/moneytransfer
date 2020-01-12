package ir.saeidbabaei.bank.moneytransfer.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;



/**
 * An entity for bank account
 * 
 * @author Saeid Babaei
 * @version 1.0
 * @since 1.0
 */
@Entity
@NamedQuery(name="findAllAccount", query="SELECT a FROM Account a")
public class Account implements Serializable{

		private static final long serialVersionUID = 1L;

		@Id
        private long accountNumber;
 
        @Version
        private int version;
        
        private String name;
        
        @Temporal(TemporalType.DATE)
        private Date createDate;
        
        private double balance;        
        
		public Account() {
			super();
		}

		/** Creates an account with the specified account number
		 * and name and create date and balance.
		 * 
		 * @param 	accountNumber	Number of account.
		 * @param	version			Using for optimistic lock.
		 * @param	name			Name of account owner.
		 * @param	createDate		Create date of account.
		 * @param	balance			Initial balance of account.
		*/
		public Account(long accountNumber, int version, String name, Date createDate, double balance) {
			super();
			this.accountNumber = accountNumber;
			this.version = version;
			this.name = name;
			this.createDate = createDate;
			this.balance = balance;
		}

		public long getAccountNumber() {
			return accountNumber;
		}

		public void setAccountNumber(long accountNumber) {
			this.accountNumber = accountNumber;		
		}
		
		public int getVersion() {
			return version;
		}

		public void setVersion(int version) {
			this.version = version;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Date getCreateDate() {
			return createDate;
		}

		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}

		public double getBalance() {
			return balance;
		}

		public void setBalance(double balance) {
			this.balance = balance;
		}
		
		/**
		 * Return Json value of account
		 * 
		 * @return	Account info in Json string format.
		 */
		@Override
		public String toString() {
			
			String accountInfo = "{\n\r"
					+ "\"accountNumber\":"
					+ Long.toString(this.accountNumber)
					+ ",\n\r\"name\":"
					+ "\"" + this.name + "\""
					+ ",\n\r\"createDate\":"
					+ "\"" + this.createDate +"\""
					+ ",\n\r\"balance\":" 
					+ Double.toString(this.balance)
					+ "\n\r}";
			return accountInfo;
		}
            
}