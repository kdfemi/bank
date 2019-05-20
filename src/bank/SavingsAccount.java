package bank;

public final class SavingsAccount extends Account implements Transactional{

	private final int minimumBalance=1000;
	private final double interestRate = 4.0d;

	public SavingsAccount(String title,String firstName,String surname, int bvn ) {
		super(title, firstName, surname, bvn);
	}

	@Override
	public void deposit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void withdrawal() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkBalance() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadCredit() {
		// TODO Auto-generated method stub
		
	}

}
