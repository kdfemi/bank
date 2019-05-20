package bank;


import java.net.*;
import java.io.*;

public class Bank {

	public static void main(String[] args) throws Exception {

		//Section: Welcome user
		String transactionOption = "1.) Deposit\t\t\t2.) Withdrawal\n3.) Account Balance\t\t4.) Load credit\n5.) Change pin\t\t4.) Check account type";
		System.out.println("****************************************\n****************************************\n\n");
		System.out.println("\tWelcome to XYZ Bank");
		System.out.println("****************************************\n****************************************\n\n");
//		Thread.sleep(2000); //added to slow processing so user can can get a real interface feel
	
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));//input to collect user's data
		BufferedReader serverMessage = null;//input to collect server message
		String username = "";
		String password = "";
		boolean anotherTransaction = true;
		
		 
		  System.out.print("Enter your details to proceed");
          System.out.print("\n****************************************\n****************************************\n");
//          Thread.sleep(3000);
          System.out.print("Enter USERNAME>> ");
          username = userInput.readLine(); // collect username from user
          System.out.println();
          System.out.print("Enter Password>> ");
          password = userInput.readLine(); //collect password
          System.out.println();
          Socket socket = new Socket("localhost",3037);
//          PrintWriter output = new PrintWriter(socket.getOutputStream());
          DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
          dos.writeUTF(username+','+password);
          dos.flush();
//          output.println(username+','+password); 
          DataInputStream dis = new DataInputStream(socket.getInputStream());
//          boolean userExist  = dis.readBoolean();
	}

}
