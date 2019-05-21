package bank;


import java.net.*;
import java.io.*;

public class Bank {

	public static void main(String[] args) throws Exception {

		//Section: Welcome user
		String transactionOption = "1.) Deposit\t\t\t2.) Withdrawal\n3.) Account Balance\t\t4.) Load credit\n5.) Change pin\t\t\t6.) Check account details";
		System.out.println("****************************************\n****************************************\n\n");
		System.out.println("\tWelcome to XYZ Bank");
		System.out.println("****************************************\n****************************************\n\n");
		Thread.sleep(2000); //added to slow processing so user can can get a real interface feel
	
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));//input to collect user's data
		BufferedReader serverMessage = null;//input to collect server message
		String username = "";
		String password = "";
		boolean isNotValid = true; // stores false if user's username and password are correct
		int chances = 3;
		Socket socket=null;
		Boolean doTransaction = true; //Store client option to do another transaction.
		
		//if user wants to do another transaction the while loop is true
		while(doTransaction){
			do {
				  System.out.print("Enter your details to proceed");
		          System.out.print("\n****************************************\n****************************************\n");
		          Thread.sleep(3000);
		          System.out.print("Enter USERNAME>> ");
		          username = userInput.readLine(); // collect username from user
		          System.out.println();
		          System.out.print("Enter Password>> ");
		          password = userInput.readLine(); //collect password
		          System.out.println();
		          socket= new Socket("localhost",3037);
		          DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		          dos.writeUTF("Account");
		          dos.flush();
		          dos.writeUTF(username+','+password);
		          dos.flush();
		          isNotValid = inputIncorrect(username, password, socket);
		          chances--;
		    
				}while(isNotValid && chances >0);
			if(chances==0 && isNotValid) {
				System.out.println("You dont have any chances again");
				System.out.print("\n****************************************\n****************************************\n");
				socket.close();
				return;
			}
            //section: welcomes user and print name
            Thread.sleep(2000);
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
           	doTransaction = false;
	}


			 
	}

	  
    /**
     * Method returns false if the inputs are not empty and user details are correct.
     * @param userName
     * @param password
     * @param socket 
     * @return Boolean
     * @throws IOException 
     * @throws InterruptedException 
     */
 public static Boolean inputIncorrect(String userName, String password, Socket socket) throws IOException, InterruptedException {
     if(!inputIncorrect(userName, password)) { 
             DataInputStream dis = new DataInputStream(socket.getInputStream());
             boolean userExist  = dis.readBoolean();
             if(userExist) {
                 return false;
             }
             System.out.println("Username or password wrong try again");
             System.out.println("Or contact adminstrator to check if your account has been blocked");
             System.out.print("\n****************************************\n****************************************\n\n");
             Thread.sleep(2000);
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
 
 
 /**
  * Direct Client to their selected transaction choice.
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
     case "6":
         //TODO Check account details
         return true;
     default:
         System.out.println("You have selected an invalid option");
         System.out.println("Please try again");
         Thread.sleep(2000);
         System.out.print("\n****************************************\n****************************************\n");
         return false;
     }   
 }
}
