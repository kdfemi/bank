package structures;

public final class CurrentAccount extends Account{

	private final static String AccountType = "Current";

	/**
	 * Current account constructor to generate current account
	 * @param title
	 * @param firstName
	 * @param surname
	 * @param bvn
	 * @param deposit
	 */
	public CurrentAccount(String title, String firstName,String surname,int bvn,double deposit) {
		super(title,surname, firstName, bvn, deposit,AccountType);
	
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
	public CurrentAccount(int accountNumber,String title, String firstName,String surname, int bvn,double deposit, String accountType) {
		super(accountNumber,title,firstName,surname,bvn,deposit,accountType);
	}

}
