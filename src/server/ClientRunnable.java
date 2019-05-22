package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
	
	public ClientRunnable(Socket socket, String username, String password, String accountType) throws SQLException {
		this.socket = socket;
		this.username = username;
		this.password = password;
		this.accountType = accountType;
		this.gson =new Gson();
		this.db = DriverManager.getConnection(url, dbuser, dbpassword);
	}
	@Override
	public void run() {
			
		DataOutputStream dos=null;
		BufferedReader dis =null;
		
			try {
				dis = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				dos = new DataOutputStream(socket.getOutputStream());
				dos.writeBoolean((userExist(username, password, accountType)));
				//dos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//admin operation is don here
			if(this.accountType.equals("admin")) {
				
				Account account = null;
				System.out.println("Reaching admin");
				try {
					String message = "";
					while(!message.equals("")){
						message = dis.readLine().toLowerCase().trim();
						}
					System.out.println("message: "+message);
					if(message.equals("createaccount")) {
						System.out.println("message: "+message);
						createAccount(dis,dos);
					}
				} catch (IOException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
			}
			//client operations here
			else if(this.accountType.equals("account")) {
				
			}
			
	}
	
	/**
	 * Creates account and send confirmation to admin
	 * @param dis
	 * @throws IOException
	 * @throws JsonSyntaxException
	 * @throws SQLException
	 */
	private synchronized void createAccount(BufferedReader dis,DataOutputStream dos) throws IOException, JsonSyntaxException, SQLException {
		Account account;
		String message;
		//expecting a json object of type account from admin
		message = dis.readLine();
		//converts json to Account object
		account =gson.fromJson(message, Account.class);
		//set parameters to db
		int accountNumber = generateAccountNumber();
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
		message = gson.toJson(account);
		dos.writeUTF(message);
		dos.flush();
	}
	
	/**
	 * Return true if user exist.
	 * @param username
	 * @param accountType
	 * @param password
	 * @return Boolean
	 */
	public synchronized  Boolean userExist(String username, String userPassword, String accountType) {
		try {
			
			Connection db = DriverManager.getConnection(url, dbuser, dbpassword);
			String sqlQuery = String.format("SELECT * FROM %s WHERE firstname =? AND password =?", accountType);
			PreparedStatement verifyPassAndUsername = db.prepareStatement(sqlQuery);
			verifyPassAndUsername.setString(1, username);
			verifyPassAndUsername.setString(2, userPassword);
			ResultSet resultSet = verifyPassAndUsername.executeQuery();
			String passwordString = null;
			String firstnameString = null;
			while(resultSet.next()) {
				firstnameString = resultSet.getString("firstname").toLowerCase();
				passwordString = resultSet.getString("password");
				if(firstnameString.equals(username) && passwordString.equals(password)) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	while(rs.next()){
		if(accountNumber == rs.getInt("accountNumber"))
			accountNumber = 1000000000 + rand.nextInt(900000000);
		}
	return accountNumber;
	}
	
}
