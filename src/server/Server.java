package server;

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
			 System.out.println("Server listening on port "+3037);
			 
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		while(true) {
			Socket socket=null;
			String password =null;
			String username = null; 
			String accountType = null;
			try {
				
				socket = server.accept();
				DataInputStream dos = new DataInputStream(socket.getInputStream());
				accountType = dos.readUTF();
				System.out.println( accountType + " connected");
			} catch (IOException e) {
				
				System.out.println("Client cannot be connected to");
				return;
			}	
			InputStreamReader input=null;
			try {
				DataInputStream dos = new DataInputStream(socket.getInputStream());
				String fromClient = dos.readUTF();
				String [] details = fromClient.split(",");
				username = details[0].trim().toLowerCase();
				password = details[1].trim();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			new Thread(new ClientRunnable(socket,username,password,accountType.toLowerCase())).start();
		}
		

	}

}
