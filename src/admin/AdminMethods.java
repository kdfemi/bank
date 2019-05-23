package admin;

import java.io.DataInputStream;
import com.google.gson.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import structures.Account;
import structures.CurrentAccount;
import structures.SavingsAccount;

public class AdminMethods {

	//Global variable
	Socket socket;
	DataOutputStream dos;
	DataInputStream dis;
	Gson gson;

	/**
	 * Set outputStream and inputStream and socket address
	 * @param socket
	 * @throws IOException
	 */
	public AdminMethods(Socket socket) throws IOException {
		
		this.socket = socket;
		this.dos = new DataOutputStream(socket.getOutputStream());
		this.dis = new DataInputStream(socket.getInputStream());	
		this.gson = new Gson();
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
			
			//converts account object to json and send to server
			 accountCreatedVerification(new SavingsAccount(title,firstName, surname, bvn, deposit));
		}
		else if(accountType.toLowerCase() == "current") {
			
			//converts account object to json and send to server
			accountCreatedVerification(new CurrentAccount(title,firstName, surname, bvn, deposit));
		}
	
	}


	/**
	 * Creates a savings if deposit amount is not specified,
	 * returns null if account is not valid type method only
	 * works for creating Savings account only.
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
		}
	}
	
	/**
	 * Sends account details to be created to server. it verifies if the account has been created
	 * @param Account
	 * @return Account
	 */
	private void accountCreatedVerification(Account account) throws IOException {
		
		//server listens for method type
		dos.writeUTF("createAccount");
		dos.flush();
		
		//convert Object Account to string
		String message = gson.toJson(account); 
		//server listen for object to be sent after method type has been called
		dos.writeUTF(message);  //server method parameter
		dos.flush();
		//receiving message from server returning json
		message = null;
		message = dis.readUTF();
		if(message.isEmpty()) {
			//if server sent nothing
			System.out.println("Account cant be created");
			return;
		}
		//notify user account created and print the json account out
		System.out.println("Account created");
		System.out.println(message);
		
		try {
			//delay purposes to simulate network
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Method takes in:<br/>
	 *<b>accountNumber:</b>  account number you want to edit<br/>
	 *<b>String option:</b> user detail you want to edit <br>
	 *<b>Object details:</b> what you want to change it to
	 * @param accountNumber
	 * @param option
	 * @param details
	 * @return
	 * @throws IOException
	 */
	protected void editAccount(int accountNumber, String detailType, Object detailValue) throws IOException {
		Account account, newAccount;
		String json, n0, n1;//stores json account, new and old account details value 
		//pass method you want to call from server
		dos.writeUTF("editaccount");
		dos.flush();
		
		//send account number, server is waiting for
		dos.writeInt(accountNumber);
		//server gets account, send to admin as a json string
		json =dis.readUTF();
		//if account empty stop further process
		if(json.isEmpty()) {
			System.out.println("Cannot find acount");
			return;
		}
			//convert json to Account Object
		account = gson.fromJson(json, Account.class);
		
		//server returns account details
		System.out.println("Account details: "+json);
		
		
		//edit option
		switch(detailType.toLowerCase()) {
		
		case "surname":
		case "firstname":{
			
			
			
			//telling what data type method on server to expect
			dos.writeUTF("string");

			//cast details from object to string
			String name = (String)detailValue;
			
			if(detailType=="surname") {
				//send detail type that wants to be changed to server
				dos.writeUTF("surname");
				//send detail value to server
				dos.writeUTF(name);
				json = dis.readUTF();
				newAccount = gson.fromJson(json, Account.class);
				System.out.println("Account details: "+json);
				n0=account.getSurname().toLowerCase();
				n1 = newAccount.getSurname().toLowerCase();
				if(n1.equals(n0)) {
					System.out.println("Changes not successful");
					break;
				}
				System.out.println("Changes made successfully");
				break;
			}
			//send detail type that wants to be changed to server
			dos.writeUTF("firstname");
			//send detail value to server
			dos.writeUTF(name);
			json = dis.readUTF();
			newAccount = gson.fromJson(json, Account.class);
			System.out.println("Account details: "+ json);
			n0=account.getFirstName().toLowerCase();
			n1 = newAccount.getFirstName().toLowerCase();
			if(n1.equals(n0)) {
				System.out.println("Changes not successful");
				break;
			}
			System.out.println("Changes made successfully");
			break;
		}

		case "bvn":{
			//send data type server should expect
			dos.writeUTF("int");
			//send detail type to edit to server
			dos.writeUTF("bvn");
			//send detail value to server
			int newBvn = (int)detailValue;
			dos.writeInt(newBvn);
			json = dis.readUTF();
			System.out.println("Account details: "+ json);
			newAccount = gson.fromJson(json, Account.class);
			n0=Integer.toString(account.getBvn());
			n1 = Integer.toString(newAccount.getBvn());
			if(n1.equals(n0)) {
				System.out.println("Changes not successful");
				break;
			}
			System.out.println("Changes made successfully");
			break;
		}
		case "title":{
			
			//send data type method on server to expect
			dos.writeUTF("string");

			//cast details from object to string
			String name = (String)detailValue;
			
			//send detail type that wants to be changed to server
			dos.writeUTF("title");
			//send detail value to server
			dos.writeUTF(name);
			json = dis.readUTF();
			newAccount = gson.fromJson(json, Account.class);
			System.out.println("Account details: "+json);
			n0=account.getTitle().toLowerCase();
			n1 = newAccount.getTitle().toLowerCase();
			if(n1.equals(n0)) {
				System.out.println("Changes not successful");
				break;
			}
			System.out.println("Changes made successfully");
			break;
		}
		case "accounttype":{
			
			//send data type method on server to expect
			dos.writeUTF("string");

			//cast details from object to string
			String name = (String)detailValue;
			
			//send detail type that wants to be changed to server
			dos.writeUTF("accounttype");
			//send detail value to server
			dos.writeUTF(name);
			json = dis.readUTF();
			newAccount = gson.fromJson(json, Account.class);
			System.out.println("Account details: "+json);
			n0=account.getAccountTtype().toLowerCase();
			n1 = newAccount.getAccountTtype().toLowerCase();
			if(n1.equals(n0)) {
				System.out.println("Changes not successful");
				break;
			}
			System.out.println("Changes made successfully");
			break;
		}
		//Not yet implemented
//		case "accessibility":{
//			
//			//send data type server should expect
//			dos.writeUTF("int");
//			//send detail type to edit to server
//			dos.writeUTF("accessibility");
//			//send detail value to server
//			int newBvn = (int)detailValue;
//			dos.writeInt(newBvn);
//			json = dis.readUTF();
//			System.out.println("Account details: "+ json);
//			newAccount = gson.fromJson(json, Account.class);
//			n0=Integer.toString(account.getBvn());
//			n1 = Integer.toString(newAccount.getBvn());
//			if(n1.equals(n0)) {
//				System.out.println("Changes not successful");
//				break;
//			}
//			System.out.println("Changes made successfully");
//			break;
//		}
		default:
			System.out.println("Invalid input");
			break;
	}
		
}
	
	/**
	 * Send's command to server to get account details specified by
	 * the account number. Get's a json object from the server and 
	 * set it as a type Account which can explicitly be casted into
	 * Current or Savings data type. 
	 * account json format is also printed on the console
	 * @param accountNumber
	 * @return Account
	 * @throws IOException
	 */
	protected void getAccount(int accountNumber) throws IOException {
		
		//server listen for method type
		dos.writeUTF("getAccount");
		dos.flush(); 
		
		//send server method parameter
		dos.writeInt(accountNumber);
		dos.flush();
		
		//expecting a json file
		String incoming = dis.readUTF();
		if(incoming.isEmpty()) {
			System.out.println("Account not found");
			return;
		}
//		Account account = gson.fromJson(incoming, Account.class);
		//prints the json file
		System.out.println(incoming);
	}

	
	/**
	 * calls server to delete account
	 * @param accountNumber
	 * @param firstname
	 * @param surname
	 */
	
	protected void deleteAccount(int accountNumber, String firstname, String surname) {
		try {
			//tells server operation to perform
			dos.writeUTF("deleteaccount");
			//writes method parameter to server
			dos.writeUTF(Integer.toString(accountNumber)+','+firstname+','+surname);
			//server returns account details
			String json = dis.readUTF();
			if(json.isEmpty()) {
				System.out.println("Account not found");
				return;
			}
			System.out.println(json);
			json = dis.readUTF();
			if(json.isEmpty()) {
				System.out.println("Account successfully deleted");
				return;
			}
			System.out.println("Account not deleted");
		} catch (IOException e) {
			
			e.printStackTrace();
			return;
		};
		
	}	

	//TODO Withdraw Amount
	//TODO Log out

	protected void deposit(int accountNumber, double amount) {
	
		try {
			//notify server operation to perform
			dos.writeUTF("deposit");
			//server expect account number next
			dos.writeInt(accountNumber);
			//server expect amount to deposit next
			dos.writeDouble(amount);
			String json = dis.readUTF();
			if(json.isEmpty()) {
				System.out.println("Account cannot be found");
				return;
			}
			System.out.println(json);
			Account account = gson.fromJson(json, Account.class);
			System.out.println("Previous account balance: "+ account.getBalance());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
