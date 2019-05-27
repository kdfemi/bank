package structures;

public final class SavingsAccount extends Account {

	private final int minimumBalance=1000;
	private final static String AccountType = "Savings";

	/**
	 * Savings account constructor to generate savings account
	 * @param title
	 * @param firstName
	 * @param surname
	 * @param bvn
	 * @param deposit
	 */
	public SavingsAccount(String title,String firstName,String surname, int bvn, double deposit ) {
		super(title, firstName, surname, bvn, deposit,AccountType);
	}
	/**
	 * Savings account constructor to generate savings account without deposit
	 * @param title
	 * @param firstName
	 * @param surname
	 * @param bvn
	 * @param deposit
	 */
	public SavingsAccount(String title,String firstName,String surname, int bvn) {
		super(title, firstName, surname, bvn,AccountType);
	}
	
	/**
	 * Constructor use to store account details from the database. calls Account constructor
	 * The accountNumber is generated on the server so normal constructor cannot
	 * get user full details.
	 * @param accountNumber
	 * @param title
	 * @param firstName
	 * @param surname
	 * @param bvn
	 * @param deposit
	 * @param accountType
	 */
	public SavingsAccount(int accountNumber,String title, String firstName,String surname, int bvn,double deposit, String accountType ) {
		super(accountNumber,title,firstName,surname,bvn,deposit,accountType);
	}
	
	@Override
	public boolean withdrawal(double amount) {

		double balance = this.getBalance();
			if(balance <=minimumBalance) {
				System.out.println("you can't withdraw this amount check your account balance");
				return false;
			}
			return super.withdrawal(amount);
	}
		
}