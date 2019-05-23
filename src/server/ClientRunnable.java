package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import structures.Account;

public class ClientRunnable implements Runnable {

	Socket socket;
	private static final String url ="jdbc:mysql://localhost:3306/bank?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	static String dbuser = "root";
	static String dbpassword ="pass";
	private String password;
	private String username;
	private String accountType;
	private Gson gson;
	private Connection db;
	
	/**
	 * Constructor
	 * @param socket
	 * @param username
	 * @param password
	 * @param accountType
	 * @throws SQLException
	 */
	public ClientRunnable(Socket socket, String username, String password, String accountType) throws SQLException {
		this.socket = socket;
		this.username = username;
		this.password = password;
		this.accountType = accountType;
		this.gson =new Gson();
		this.db = DriverManager.getConnection(url, dbuser, dbpassword);
	}
	
	//thread main method
	@Override
	public void run() {
			
		DataOutputStream dos=null;
		DataInputStream dis =null;
		
			try {
				//verification of admin and client
				 dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
				dos.writeBoolean((userExist(username, password, accountType)));
				dos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			//admin operation is done here
			if(this.accountType.equals("admin")) {
				
				String message = "";
				try {
					
					//read operation admin wants to perform
					message = dis.readUTF().toLowerCase();
					
					if(message.equals("createaccount"))
						createAccount(dis,dos);
					else if(message.equals("getaccount"))
						getAccount(dis,dos);	
					else if (message.equals("editaccount")) 
						editAccount(dis,dos);
					else if (message.equals("deleteaccount")) 
						deleteAccount(dis,dos);
					else if (message.equals("deposit")) 
						deposit(dis,dos);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				} 

			}
			
			//client operations here
			else if(this.accountType.equals("account")) {
				
			}	
	}
	
	private synchronized void deposit(DataInputStream dis, DataOutputStream dos) {
		
		int accountNumber;
		double amount;
		String json;
		try {
			accountNumber = dis.readInt();
			amount = dis.readDouble();
			json = internalGetAccount(accountNumber);
			if(json.isEmpty()) {
				return;
			}
			dos.writeUTF(json);
			Account account = gson.fromJson(json, Account.class);
			account.setBalance(amount);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * delete account from database
	 * @param dis
	 * @param dos
	 */
	private void deleteAccount(DataInputStream dis, DataOutputStream dos) {
		String parameterString;
		try {
			//gets parameter from client
			parameterString = dis.readUTF();
			String [] parameters = parameterString.split(",");
			int accountNumber = Integer.parseInt(parameters[0]);
			String firstname = parameters[1].toLowerCase();
			String surname = parameters[2].toLowerCase();
			if(internalGetAccount(accountNumber).isEmpty()) {
				System.out.println("operation failed");
				dos.writeUTF("");
				dos.flush();
				return;
			}
			//sends account to delete to admin
			dos.writeUTF(internalGetAccount(accountNumber));
			Account account = gson.fromJson(internalGetAccount(accountNumber), Account.class);
			String firstname2 = account.getFirstName().toLowerCase();
			String surname2 = account.getSurname().toLowerCase();
			if(firstname.equals(firstname2)&&surname.equals(surname2)) {
				String query = "DELETE FROM account WHERE accountNumber=?";
				PreparedStatement statement = db.prepareStatement(query);
				statement.setInt(1, accountNumber);
				statement.executeUpdate();
				dos.writeUTF(internalGetAccount(accountNumber));
			}
			//sends empty string to admin if account has been deleted
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Edit client account
	 * @param dis
	 * @param dos
	 */
	private synchronized void editAccount(DataInputStream dis, DataOutputStream dos) {
		String expectDataType;
		String detailType;
		Object detailValue;//get value of the detail admin wants to change the old detail to e.g surname from old to new
		int accountNumber;
		try {
				//account number of client to edit
				accountNumber = dis.readInt();
				dos.writeUTF(internalGetAccount(accountNumber));
				
				if(internalGetAccount(accountNumber).isEmpty()) {
					return;
				}
				
				//data type of parameter(e.g String, and int) server should expect from admin
				expectDataType = dis.readUTF().toLowerCase();

				//detail admin wants to change e.g surname
				detailType = dis.readUTF();			

				
				//format query string
				String query = String.format("UPDATE account SET %s=? WHERE accountNumber=?", detailType);
				PreparedStatement statment = db.prepareStatement(query);
				statment.setInt(2, accountNumber);
				
				//dealing with strings
				if(expectDataType.equals("string")) {	
					//get value of the detail admin wants to change the old detail to e.g surname from old to new
					detailValue =dis.readUTF();
					statment.setString(1, (String)detailValue);
				}
				else if(expectDataType.equals("int")) {	
					//get value of the detail admin wants to change the old detail to e.g surname from old to new
					detailValue =dis.readInt();
					statment.setInt(1, (int) detailValue);
				}
				
				//Execute statement and send to admin
				statment.executeUpdate();
				dos.writeUTF(internalGetAccount(accountNumber));
		} catch (IOException e) {
			
			e.printStackTrace();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Gets account details from the database and send to admin
	 * @param dis
	 * @param dos
	 */
	private synchronized void getAccount(DataInputStream dis, DataOutputStream dos) {
		int accountNumber;
		try {
			
			//listen for account number from admin
			accountNumber = dis.readInt();
			//send data in json format to admin	
			dos.writeUTF(internalGetAccount(accountNumber));
			dos.flush();
		} catch (IOException e) {
			
			e.printStackTrace();
			return;
		}
		
	}
	
	/**
	 * Gets account details from the database
	 * @param accountNumber
	 * @return Json String
	 */
	private String internalGetAccount(int accountNumber) {
		
		PreparedStatement statement;
		String message="";
		try {
			//create MySQL statement and set it
			statement = db.prepareStatement("SELECT * FROM account WHERE accountNumber=?");
			statement.setInt(1, accountNumber);
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				//store retrieved data 
				Account account = new Account(result.getInt("accountNumber"),result.getString("title"), result.getString("firstname"), result.getString("surname"), result.getInt("bvn"), result.getDouble("accountBalance"), result.getString("accountType"));
				//send data in json format to admin
				 message = gson.toJson(account);
				 if(message.trim()=="")
					 return message="";
				 return message;
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
			return null;
		}
		return message;
	
	}

	/**
	 * Creates account in the database  and send confirmation to admin
	 * @param dis
	 * @throws IOException
	 * @throws JsonSyntaxException
	 * @throws SQLException
	 */
	private synchronized void createAccount(DataInputStream dis,DataOutputStream dos) throws IOException, JsonSyntaxException {
		
		Account account;
		String message;
		
		//expecting a json object of type account from admin
		message = dis.readUTF();
		//converts json to Account object
		account =gson.fromJson(message, Account.class);
		
		//set parameters to db
		int accountNumber;
		try {
			accountNumber = generateAccountNumber();
			String sqlQuery = "INSERT INTO account(accountNumber,firstname,surname,bvn,title,accountType) VALUES(?,?,?,?,?,?);";
			PreparedStatement statement = db.prepareStatement(sqlQuery);
			statement.setInt(1, accountNumber);
			statement.setString(2, account.getFirstName());
			statement.setString(3, account.getSurname());
			statement.setInt(4, account.getBvn());
			statement.setString(5, account.getTitle());
			statement.setString(6, account.getAccountTtype());
			statement.execute();
			statement = db.prepareStatement("SELECT * FROM account WHERE accountNumber=?");
			statement.setInt(1, accountNumber);
			ResultSet result = statement.executeQuery();
			result.next();
			//stores data from database to account of Account data type
			account = new Account(result.getString("title"), result.getString("firstname"), result.getString("surname"), result.getInt("bvn"), result.getDouble("accountBalance"), result.getString("accountType"));
			System.out.println("Account created");
			System.out.print("\n****************************************\n****************************************\n");
			message = gson.toJson(account);
			dos.writeUTF(message);
			dos.flush();
		} catch (SQLException e) {
			System.out.println("Account cannot be created");
			dos.writeUTF("Account cannot be created");
			return;
		}

	}
	
	/**
	 * Return true if user exist.
	 * @param username
	 * @param accountType
	 * @param password
	 * @return Boolean
	 */
	private synchronized  Boolean userExist(String username, String userPassword, String accountType) {
		try {
			
			//MySQL Query admin or client is inserted where we have %s
			String sqlQuery = String.format("SELECT * FROM %s WHERE firstname =? AND password =?", accountType);
			//prepareed query to excute on MySQL
			PreparedStatement verifyPassAndUsername = db.prepareStatement(sqlQuery);
			//Set preparedStatement parameters
			verifyPassAndUsername.setString(1, username);
			verifyPassAndUsername.setString(2, userPassword);
			//get result from database
			ResultSet resultSet = verifyPassAndUsername.executeQuery();
			String passwordString = null;
			String firstnameString = null;
			//resultSet.next() checks whether there still rows in the db loops through all
			while(resultSet.next()) {
				//stores name and password
				firstnameString = resultSet.getString("firstname").toLowerCase();
				passwordString = resultSet.getString("password");
				
				//verify name and password equals any account on the db
				if(firstnameString.equals(username) && passwordString.equals(password)) {
					//Verifying if user account not blocked
					//not implemented
					if(accountType.equals("account")) {
						if(resultSet.getBoolean("accessibility")) {
							System.out.println("user Found");
							 System.out.print("\n****************************************\n****************************************\n");
							return true;
						}
					}
					System.out.println("user Found");
					 System.out.print("\n****************************************\n****************************************\n");
					return true;
				}
					
			}			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		System.out.println("user not Found");
		return false;
	}
	
	/**
	 * generates account number randomly which is Unique to the Database.
	 * @return int
	 * @throws SQLException
	 */
	private synchronized int generateAccountNumber() throws SQLException {
		
	Random rand = new Random();
	//generate random number of 10 figures
	int  accountNumber = 1000000000 + rand.nextInt(900000000);
	
	//connect to database
	Connection db = DriverManager.getConnection(url, dbuser, dbpassword);
	
	//create query to excute
	String sqlQuery = "SELECT * FROM account WHERE accountNumber=?";
	PreparedStatement statement = db.prepareStatement(sqlQuery);
	//set the parameter of prepared statement to generated account number
	statement.setInt(1, accountNumber);
	ResultSet rs = statement.executeQuery();
	
	//verifies account number is not duplicated
	while(rs.next()){
		if(accountNumber == rs.getInt("accountNumber"))
			accountNumber = 1000000000 + rand.nextInt(900000000);
		}
	return accountNumber;
	}
	
}
