package structures;

public interface Transactional {

	/**
	 * Deposit the inserted amount
	 * @param amount
	 */
	void deposit(double amount);
	
	/**
	 * Withdraw inputed amount and deduct from account balance
	 * @param amount
	 */
	boolean withdrawal(double amount);
	
	/**
	 * Gets the account balance
	 */
	void checkBalance();
	
	/**
	 * Calls the withdrawal method to load credit to phone
	 * @param amount
	 */
	void loadCredit(int amount);
	
	/**
	 * use to change account password, newPassword and ConfirmPassword must be same.
	 * Returns true if account pin changed successfully and false if new password is
	 * the same with the old password or newPassword and confirmPassword are not same.
	 * @param Newpassword
	 * @param confirmPassword
	 * @return boolean
	 */
	boolean changePin(String newpassword, String confirmPassword);
}
