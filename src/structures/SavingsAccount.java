package structures;

public final class SavingsAccount extends Account {

	private final int minimumBalance=1000;
	private final double interestRate = 4.0d;
	private final static String AccountType = "Savings";

	public SavingsAccount(String title,String firstName,String surname, int bvn, double deposit ) {
		super(title, firstName, surname, bvn, deposit,AccountType);
	}
	public SavingsAccount(String title,String firstName,String surname, int bvn) {
		super(title, firstName, surname, bvn,AccountType);
	}
	
	@Override
	public void withdrawal(long amount) {

		double balance = this.getBalance();
			if(balance <=minimumBalance) {
				System.out.println("you can't withdraw this amount check your account balance");
				return;
			}
			super.withdrawal(amount);
	}
		
}