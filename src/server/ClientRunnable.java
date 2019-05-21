package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;

public class ClientRunnable implements Runnable {

	Socket socket;
	private static final String url ="jdbc:mysql://localhost:3306/bank?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	static String dbuser = "root";
	static String dbpassword ="pass";
	private String password;
	private String username;
	private String accountType;
	
	public ClientRunnable(Socket socket, String username, String password, String accountType) {
		this.socket = socket;
		this.username = username;
		this.password = password;
		this.accountType = accountType;
	}
	@Override
	public void run() {
			
//			PrintWriter write = new PrintWriter(socket.getOutputStream());
			
			try {
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				dos.writeBoolean((userExist(username, password, accountType)));
				//dos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	
	/**
	 * Return true if user exist.
	 * @param username
	 * @param accountType
	 * @param password
	 * @return Boolean
	 */
	public Boolean userExist(String username, String userPassword, String accountType) {
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
//			System.out.println("runnable "+resultSet.getString("firstname"));
//			System.out.println("runnable "+resultSet.getString("password"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("user not Found");
		return false;
	}
}
