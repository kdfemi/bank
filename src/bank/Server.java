package bank;

import java.io.*;
import java.net.*;
import java.sql.*;

public class Server {

	public static void main(String[] args) {
		
		ServerSocket server=null;
		
		int port = 3037;
		try {
			System.out.println("Welcome to XYZ bank");
			 server = new ServerSocket(port);
			 System.out.println("Server listening on port"+3037);
			 
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		while(true) {
			Socket socket=null;
			String password =null;
			String username = null; 
			try {
				socket = server.accept();
				System.out.println("Client connected");
			} catch (IOException e) {
				
				System.out.println("Client cannot be connected to");
				return;
			}	
			InputStreamReader input=null;
			try {
//				input = new InputStreamReader(socket.getInputStream());
//				BufferedReader br = new BufferedReader(input);
//				String i = br.readLine();
//				String [] details = i.split(",");
//				username = details[0];
//				password = details[1];
				DataInputStream dos = new DataInputStream(socket.getInputStream());
				String j = dos.readUTF();
				String [] details = j.split(",");
				username = details[0];
				password = details[1];
				System.out.println(username);
				System.out.println(password);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

//			new Thread(new ClientRunnable(socket,username,password)).start();
		}
		

	}

}
