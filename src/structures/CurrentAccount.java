package structures;

public final class CurrentAccount extends Account{

	private final static String AccountType = "Current";


	public CurrentAccount(String title, String firstName,String surname,int bvn,double deposit) {
		super(title,surname, firstName, bvn, deposit,AccountType);
		// TODO Auto-generated constructor stub
	}
	

}
