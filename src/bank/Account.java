package bank;

public class Account {
	
	private double balance;
	private int accountNumber;
	private int bvn;
	private String title;
	private String firstName;
	private String surname;
	
	public Account(String title, String firstName,String surname, int bvn ) {

		this.bvn = bvn;
		this.firstName = firstName;
		this.surname = surname;
		this.title = title;
	}
	
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public int getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(int accountNumber) {
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

}
