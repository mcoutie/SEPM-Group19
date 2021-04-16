package main;

public class Account {

	private String email;
	private String name;
	private String phone;
	private String password;
	private AccountType type;

	public Account(String email, String name, String phone, String password, AccountType type) {
		this.email = email;
		this.name = name;
		this.phone = phone;
		this.password = password;
		this.type = type;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String newPassword) {
		password = newPassword;
	}

	public AccountType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

}
