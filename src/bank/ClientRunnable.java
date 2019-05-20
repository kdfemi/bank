package bank;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;

public class ClientRunnable implements Runnable {

	Socket socket;
	static String url ="jdbc:mysql://localhost:3306/bank?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	static String dbuser = "root";
	static String dbpassword ="pass";
	private String password;
	private String username;
	
	public ClientRunnable(Socket socket, String username, String password) {
		this.socket = socket;
		this.username = username;
		this.password = password;
	}
	@Override
	public void run() {
		try {
			
//			PrintWriter write = new PrintWriter(socket.getOutputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			//if true do allow functions to be performed
			if(verification(username, password)) {
//				write.print(true);
				dos.writeBoolean(true);
				dos.flush();
			}
			else {
				
//				write.print(false);
				dos.writeBoolean(false);
				dos.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Return true if user exist.
	 * @param username
	 * @param password
	 * @return Boolean
	 */
	public Boolean verification(String username, String userPassword) {
		try {
			Connection db = DriverManager.getConnection(url, dbuser, dbpassword);
			Statement s = db.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM account");
			System.out.println(rs.getString("firstname"));
			PreparedStatement verifyPassAndUsername = db.prepareStatement("SELECT * FROM account WHERE firstname =? AND password =?");
			verifyPassAndUsername.setString(1, username);
			verifyPassAndUsername.setString(2, userPassword);
			ResultSet resultSet = verifyPassAndUsername.executeQuery();
			if(resultSet.getString("firstname")==username & resultSet.getString("password")==userPassword)
				return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
