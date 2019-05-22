package admin;

import java.io.BufferedReader;
import java.io.DataInputStream;
import com.google.gson.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import structures.Account;
import structures.CurrentAccount;
import structures.SavingsAccount;

public class AdminMethods {

	DataOutputStream dos; 
	BufferedReader dis;
	Socket socket;
	Gson gson;
	/**
	 * Set outputStream and inputStream
	 * @param socket
	 * @throws IOException
	 */
	public AdminMethods(Socket socket) throws IOException {
		this.socket = socket;
		this.dos = new DataOutputStream(socket.getOutputStream());
		this.dis = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		Gson gson = new Gson();
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
	 * @throws IOException 
	 */
	protected void createAccount(String title, String firstName,String surname, int bvn,double deposit, String accountType) throws IOException {
		if(accountType.toLowerCase() == "savings") {
			
			SavingsAccount savingsAccount = new SavingsAccount(title,firstName, surname, bvn, deposit);
			//converts account object to json
			 accountCreatedVerification(savingsAccount);
		}
		else if(accountType.toLowerCase() == "current") {
			CurrentAccount currentAccount = new CurrentAccount(title,firstName, surname, bvn, deposit);
			//converts account object to json
		}
	
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
	 * @throws IOException 
	 */
	protected void createAccount(String title, String firstName,String surname, int bvn, String accountType) throws IOException {
		if(accountType == "savings") {
			createAccount(title, firstName, surname, bvn,0, accountType);
			//TODO push account to db
		}
	}
	
	/**
	 * @param savingsAccount
	 * @return Account
	 * @throws IOException
	 */
	private Account accountCreatedVerification(Account account) throws IOException {
		
		//server listens for method type
		dos.writeUTF("createAccount");
		dos.flush();
		//convert Object Account to string
		String message = gson.toJson(account); 
		//server listen for object to be sent after method type has been called
		dos.writeUTF(message);
		dos.flush();
		//receiving message from server returning json
		message = null;
		message = dis.readLine();
		if(message.equals(null)) {
			System.out.println("Account cant be created");
			return null;
		}
		System.out.println("Account created");
		return account;
	}
	
	
		//TODO write documentation
	protected Account editAccount(int accountNumber, String option) throws IOException {
		Account account = getAccount(accountNumber);
		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
		switch(option.toLowerCase()) {
		
		case "surname":
		case "firstname":{
			String name=null;
			String message =null;
			if(option=="surname") {
				System.out.print("Enter new Surname");
				System.out.print(">>> ");
				name = read.readLine();
				System.out.println();
				account.setSurname(name);
				message =gson.toJson(account);
				dos.writeUTF(message);
				dos.flush();
				//Verifying if the update has been performed.
				account = getAccount(accountNumber);
				String newName = account.getSurname();
				if(newName.toLowerCase().equals(name.toLowerCase())) {
					System.out.printf("%s has been successfully changed to %s ",name,newName );
					break;
				}
				System.out.printf("%s failed to change to %s ",name,newName);
				break;
				
			}
			System.out.print("Enter new Firstname");
			System.out.print(">>> ");
			name = read.readLine();
			System.out.println();
			account.setSurname(name);
			message =gson.toJson(account);
			dos.writeUTF(message);
			dos.flush();
			
			//Verifying if the update has been performed.
			account = getAccount(accountNumber);
			String newName = account.getSurname();
			if(newName.toLowerCase().equals(name.toLowerCase())) {
				System.out.printf("%s has been successfully changed to %s ",name,newName );
				break;
			}
			System.out.printf("%s failed to change to %s ",name,newName);
			break;
		}
		case "bvn":{
			System.out.print("Enter new bvn");
			System.out.print(">>> ");
			int bvn = Integer.parseInt(read.readLine());
			System.out.println();
			account.setBvn(bvn);;
			String message =gson.toJson(account);
			dos.writeUTF(message);
			dos.flush();
			
			//Verifying if the update has been performed.
			account = getAccount(accountNumber);
			int newBvn = account.getBvn();
			if(newBvn == bvn) {
				System.out.printf("%s has been successfully changed to %s ",bvn,newBvn );
				break;
			}
			System.out.printf("%s failed to change to %s ",bvn,newBvn);
			break;
			}
		}
		return account;
	}
		//TODO Delete Account
		//TODO Deposit to Account 
	/**
	 * Send's command to server to get account details specified by
	 * the account number. Get's a json object from the server and 
	 * set it as a type Account which can explicitly be casted into
	 * Current or Savings data type. 
	 * @param accountNumber
	 * @return Account
	 * @throws IOException
	 */
	protected Account getAccount(int accountNumber) throws IOException {
		
		//server listen for method type
		dos.writeUTF("getAccount:"+accountNumber);
		dos.flush(); 
		
		//expecting a json file
		String incoming = dis.readLine();
		Account account = gson.fromJson(incoming, Account.class);
		 return account;
	}	

		//TODO Withdraw Amount
		//TODO Log out
	
	
}
