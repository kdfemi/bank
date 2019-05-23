package structures;
import java.util.Random;
public class Account implements Transactional{
	
	private double balance=0;
	private int accountNumber;
	private int bvn;
	private String title;
	private String firstName;
	private String surname;
	private String accountTtype;
	private String pin;
	
	//constructor to create account with balance ;
	
	public Account(String title, String firstName,String surname, int bvn,double deposit, String accountType ) {
 
		this.bvn = bvn;
		this.firstName = firstName;
		this.surname = surname;
		this.title = title;
		this.accountTtype = accountType;
		this.setAccountNumber();//buggy
		this.balance +=deposit;

	}
	//constructor to create account without balance i.e savings account;
	public Account(String title, String firstName,String surname, int bvn, String accountType) {
		
		this(title, firstName,surname, bvn, 0, accountType);
	}
	
	//constructor to get account
	public Account(int accountNumber,String title, String firstName,String surname, int bvn,double deposit, String accountType ){
		this.bvn = bvn;
		this.firstName = firstName;
		this.surname = surname;
		this.title = title;
		this.accountTtype = accountType;
		this.accountNumber = accountNumber;
		this.balance = deposit;
	}
	public double getBalance() {
		return balance;
	}
	
	public void setBalance(double balance) {
		this.balance += balance;
	}
	
	public int getAccountNumber() {
		return accountNumber;
	}
	
	private void setAccountNumber() {
		Random rand = new Random();
		int  accountNumber = 1000000000 + rand.nextInt(900000000);
		this.accountNumber = accountNumber;
	}
	
	public int getBvn() {
		return bvn;
	}
	
	public void setBvn(int BVN) {
		this.bvn = BVN;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	

	public String getAccountTtype() {
		return accountTtype;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}
	
	
	@Override
	public void deposit(double amount) {
		
		setBalance(amount);
	}

	@Override
	public void withdrawal(long amount) {

		double balance = this.getBalance();
		this.setBalance(balance-amount);
		System.out.printf(">>> %S amount was withdrawn \n Balance remaining >>> %s", balance,this.getBalance());
	}

	@Override
	public void checkBalance() {
		this.getBalance();
	}

	@Override
	public void loadCredit(int amount) {
		withdrawal(amount);
	}

	@Override
	public boolean changePin(String newPassword, String confirmPassword) {
	
		String oldpassword = this.getPin();
		if(oldpassword == newPassword) {
			System.out.println("old and new password cant be same");
			return false;
		}
		if(newPassword.equals(confirmPassword)) {		
			this.setPin(newPassword);
			System.out.println("New password set");
			return true;
		}
		System.out.println("password doesn't match with confirm password");
		return false;
	}


}
