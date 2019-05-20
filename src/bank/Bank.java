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
		Thread.sleep(2000); //added to slow processing so user can can get a real interface feel
	
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));//input to collect user's data
		BufferedReader serverMessage = null;//input to collect server message
		String username = "";
		String password = "";
		boolean anotherTransaction = true;
		
		
		

		//it is true if customer wants to perform another transaction
		do {
			 Socket socket = new Socket("localhost",3037);

//			 InputStreamReader inputFromServer = new InputStreamReader(socket.getInputStream());
//			 serverMessage = new BufferedReader(inputFromServer);
//			 userExist = new Boolean(serverMessage.readLine());
			 
			//loop terminates when user enters  correct details
			while(inputIncorrect(username,password)) { 
				
				System.out.print("Enter your details to proceed");
				System.out.print("\n****************************************\n****************************************\n");
				Thread.sleep(3000);
				System.out.print("Enter USERNAME>> ");
				username = userInput.readLine(); // collect username from user
				System.out.println();
				System.out.print("Enter Password>> ");
				password = userInput.readLine(); //collect password
				System.out.println();
				//prompts user if they enter invalid selection
				 if(inputIncorrect(username, password)) {
					 System.out.print("Inputs cannot be empty please try again");
					 System.out.print("\n\n****************************************\n****************************************\n\n");
					 continue;
					 } 
				 if(!inputIncorrect(username,password,socket)) break;
				 System.out.println("Stir");
			}
				
				//section: welcomes user and print name
				//Thread.sleep(2000);
				System.out.print("\n\n****************************************\n****************************************\n\n");
				System.out.printf("Welcome to your account"); //TODO Get details from created date base
				System.out.print("\n****************************************\n****************************************\n\n");
				Thread.sleep(2000);
				
				//selection of operation user wants to perform loop terminates when write option is selected
				 boolean validOption;
					do {
						System.out.println("Please click a number that correspond to any operation you wish to perform\n");
						System.out.println(transactionOption);
						System.out.print(">>>");
						String option = userInput.readLine();
						System.out.println();
						validOption = callTranscationType(option);
						}while(validOption==false);

		}while(anotherTransaction);
		
			//TODO validate users input prompt to enter correct details if the details are not correct	
	}
	
	
	
	/**
	 * Direct user to their selected transaction option.
	 * @param option
	 * @return boolean
	 * @throws InterruptedException 
	 */
	private static boolean callTranscationType(String option) throws InterruptedException {

		switch(option) {
		 
		case "1":
			//TODO Deposit
			return true;
		case "2":
			//TODO withdrawal
			return true;
			
		case "3":
			//TODO Check Account balance
			return true;
		case "4":
			//TODO Load credit
			return true;
		case "5":
			//TODO Change pin
			return true;
		default:
			System.out.println("You have selected an invalid option");
			System.out.println("Please try again");
			Thread.sleep(2000);
			System.out.print("\n****************************************\n****************************************\n");
			return false;
		}
		
	}
	
	/**
	 * Method returns false if the inputs are not empty and user details are correct.
	 * @param userName
	 * @param password
	 * @param socket 
	 * @return Boolean
	 * @throws IOException 
	 */
 public static Boolean inputIncorrect(String userName, String password, Socket socket) throws IOException {
	 if(!inputIncorrect(userName, password)) {
			 PrintWriter output = new PrintWriter(socket.getOutputStream());
			 output.println(userName+','+password);	
			 DataInputStream dis = new DataInputStream(socket.getInputStream());
			 boolean userExist  = dis.readBoolean();
			 if(userExist) {
				 return false;
			 }
			 System.out.println("Username or password wrong try again");
	 }
		 return true; 
 }
 
	/**
	 * Method returns false if the inputs are not empty.
	 * @param userName
	 * @param password
	 * @return Boolean
	 */
 public static Boolean inputIncorrect(String userName, String password) throws IOException {
	 if(!(userName.isEmpty() | password.isEmpty())) {
			 return false;
	 }
		 return true; 
 }
}
