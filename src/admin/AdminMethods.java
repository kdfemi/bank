package admin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import structures.Account;
import structures.CurrentAccount;
import structures.SavingsAccount;

public class AdminMethods {

	DataOutputStream dos;
	DataInputStream dis;
	Socket socket;
	
	/**
	 * Set outputStream and inputStream
	 * @param socket
	 * @throws IOException
	 */
	public AdminMethods(Socket socket) throws IOException {
		this.socket = socket;
		dos = new DataOutputStream(socket.getOutputStream());
		dis = new DataInputStream(socket.getInputStream());
	}

	/**
	 * Creates a savings account or a Current account
	 * returns null if account is not valid
	 * @param title
	 * @param firstName
	 * @param surname
	 * @param bvn
	 * @param deposit
	 * @param accountType
	 * @return Account
	 */
	protected Account createAccount(String title, String firstName,String surname, int bvn,double deposit, String accountType) {
		if(accountType == "savings") {
			SavingsAccount savingsAccount = new SavingsAccount(title,firstName, surname, bvn, deposit);
			return savingsAccount;
		}
		else if(accountType == "current") {
			CurrentAccount currentAccount = new CurrentAccount(title,firstName, surname, bvn, deposit);
			return currentAccount;
		}
		return null;
	}
	/**
	 * Creates a savings if deposit amount is not specified,
	 * returns null if account is not valid type method only
	 * works for creating Savings account.
	 * @param title
	 * @param firstName
	 * @param surname
	 * @param bvn
	 * @param deposit
	 * @param accountType
	 * @return Account
	 */
	protected Account createAccount(String title, String firstName,String surname, int bvn, String accountType) {
		if(accountType == "savings") {
			Account savingsAccount = createAccount(title, firstName, surname, bvn,0, accountType);
			return savingsAccount;
		}
		return null;
	}
		//TODO Edit Account
	protected Account editAccount() {
		
		return null;
	}
		//TODO Delete Account
		//TODO Deposit to Account 
		//TODO Get Account
	protected void getAccount(int accountNumber, Socket socket) throws IOException {
		 dos = new DataOutputStream(socket.getOutputStream());
		//accpecting a json file
	}
		//TODO Withdraw Amount
		//TODO Log out

}
