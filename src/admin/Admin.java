package admin;


import java.io.*;
import java.net.Socket;

public class Admin {

	public static void main(String[] args) throws InterruptedException {

		String transactionOption = "1.) Create Account\t\t2.) Edit Account\n3.) Delete Account\t\t4.) Deposit to Account\n5.) Get Account\t\t\t6.) Withdraw Amount\n7.) Log out";
		System.out.println("Welcome to Admin page");
		Thread.sleep(2000);
		BufferedReader readln = new BufferedReader(new InputStreamReader(System.in));
		String username ="";
		String password ="";
		Socket socket = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		Boolean detailsCorrect = false;
		
		while(!detailsCorrect) {
		try {
			System.out.print("\n****************************************\n****************************************\n");
			System.out.println("Enter your username");
			System.out.print(">>> ");
			username = readln.readLine();
			System.out.println();
			System.out.println("Enter your password");
			System.out.print(">>> ");
			password = readln.readLine();
			System.out.println();
			socket = new Socket("localhost",3037);
			dos = new DataOutputStream(socket.getOutputStream());
	        dos.writeUTF("Admin");
	        dos.flush();
	        dos.writeUTF(username+','+password+','+"admin");
	        dos.flush();
	        dis = new DataInputStream(socket.getInputStream());
	        detailsCorrect = dis.readBoolean();
	        if(detailsCorrect) System.out.println("You are Logged in!!!");
		} catch (IOException e) {
			
			e.printStackTrace();
			}
		}
		
		 //selection of operation Admin wants to perform loop terminates when write option is selected
        boolean validOption = false;
           do {
        	   Thread.sleep(2000);
        	   System.out.print("\n****************************************\n****************************************\n");
               System.out.println("Please click a number that correspond to any operation you wish to perform\n");
               System.out.println(transactionOption);
               System.out.print(">>>");
               String option;
			try {
				option = readln.readLine();
	               System.out.println();
	               validOption = callTranscationType(option,socket);
	               if(validOption ==true) {
	            	   System.out.print("\n****************************************\n****************************************\n");
		               System.out.println("Do you want to perform another operation press 1 or 2");
		               System.out.println("1.)YES\t\t\t.2)NO");
		               System.out.print(">>>");
		               option = readln.readLine();
		               if(option.equals("1")) validOption = true;
		               System.out.println();
	               }

			} catch (IOException e) {
			
				e.printStackTrace();
			}

               }while(validOption==false);
	}
	/**
	  * Direct Admin to their selected option of choice.
	  * @param option
	  * @return boolean
	  * @throws InterruptedException 
	 * @throws IOException 
	  */
	 private static boolean callTranscationType(String option,Socket socket) throws InterruptedException, IOException {

		 AdminMethods methods = new AdminMethods(socket);
	     switch(option) {
	      
	     case "1":
	    	 //TODO bvn check if previous bvn name =new account name
	    	methods.createAccount("Miss", "Oyinda", "Badmus", 453290857,"savings");
	         return true;
	     case "2":
	    	 /*
	    	  * editAccount((int)accountNumber,(String)detailType,(Object)detailValue)
	    	  * detailType: (String)surname, (String)firstname, (number)bvn,(string)accounttype,(int)accessibility
	    	  */
	         methods.editAccount(0, "firstname", "tom");
	         return true;
	         
	     case "3":
	    	 // deletAccount((int)accountNumber,(String)firstname,(String)surname)   	  
	         methods.deleteAccount(0, "jane", "doe");
	         return true;
	     case "4":
	         methods.deposit(1052146804, 0.0);
	         return true;
	     case "5":
	         methods.getAccount(1);
	         return true;
	     case "6":
	         //TODO withdraw money for client 
	         return true;
	     case "7":
	         System.out.println("You Logged out");
	         try {
				socket.close();
				
			} catch (IOException e) {
	
				e.printStackTrace();
			}
	         Thread.sleep(2000);
	    	 
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
