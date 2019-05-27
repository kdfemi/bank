package bank;


import java.net.*;

import com.google.gson.Gson;

import admin.AdminMethods;
import structures.Account;

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
		String username = "";
		String password = "";
		boolean isNotValid = true; // stores false if user's username and password are correct
		int chances = 3;
		Socket socket=null;
		Boolean doTransaction = true; //Store client option to do another transaction.
       //get's user input and receive server's output
		DataOutputStream dos = null;
        DataInputStream dis =null;
        Gson gson = new Gson();
        Account account = null;
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
		          dos = new DataOutputStream(socket.getOutputStream());
		          dis = new DataInputStream(socket.getInputStream());
		          dos.writeUTF("Account");
		          dos.flush();
		          dos.writeUTF(username+','+password+','+"Account");
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
            String json = dis.readUTF();
            account = gson.fromJson(json, Account.class);
            System.out.print("\n\n****************************************\n****************************************\n\n");
            System.out.printf("Welcome %s %s %s to your account",account.getTitle(),account.getFirstName(),account.getSurname());
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
                   validOption = callTranscationType(option, socket, account);
                   }while(validOption==false);
        	   System.out.print("\n****************************************\n****************************************\n");
               System.out.println("Do you want to perform another operation press 1 or 2");
               System.out.println("1.)YES\t\t\t.2)NO");
               System.out.print(">>>");
               String choice = userInput.readLine();
               
               if(choice.equals("1")) {
            	   doTransaction = true;
            	   continue;
               }
               dos.writeBoolean(false);
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
 * @throws IOException 
  */
 private static boolean callTranscationType(String option, Socket socket, Account account) throws InterruptedException, IOException {
	 AdminMethods methods = new AdminMethods(socket);
     switch(option) {
      
     case "1":
        methods.deposit(account.getAccountNumber(), 15000);
         return true;
     case "2":
         methods.withdraw(account.getFirstName(), account.getSurname(), account.getAccountNumber(), 2000);
         return true;
         
     case "3":
         String json = methods.getAccount(account.getAccountNumber());
         Gson gson = new Gson();
         Account errm = gson.fromJson(json, Account.class);
         System.out.println("Your account balance is: "+errm.getBalance());
         return true;
     case "4":
         loadCredit(account,socket,500,08034000000d);
         return true;
     case "5":
         methods.editAccount(account.getAccountNumber(), "password", "1234");
         return true;
     case "6":
         methods.getAccount(account.getAccountNumber());
         return true;
     default:
         System.out.println("You have selected an invalid option");
         System.out.println("Please try again");
         Thread.sleep(2000);
         System.out.print("\n****************************************\n****************************************\n");
         return false;
     }   
 }




private static void loadCredit(Account account, Socket socket,int amount, double phoneNumber) throws IOException {
	
	 AdminMethods methods = new AdminMethods(socket);
	 methods.withdraw(account.getFirstName(), account.getSurname(), account.getAccountNumber(), amount);
	 System.out.println("Credit sent to "+ phoneNumber);
}
}
