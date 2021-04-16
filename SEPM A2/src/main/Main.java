package main;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	Account user = null;
	Scanner in;
	ArrayList<Account> accounts;
	ArrayList<Ticket> tickets;

	public Main() {
		in = new Scanner(System.in);
		accounts = new ArrayList<Account>();
		tickets = new ArrayList<Ticket>();
		
		// Hardcoded user accounts
		accounts.add(new Account("test@domain.com", "Test User", "xxxx xxx xxx", "abc", AccountType.STAFF));
		accounts.add(new Technician("harry@support.domain.com", "Harry Styles", "xxxx xxx xxx", "abc",
				AccountType.TECHNICIAN, 1));
		accounts.add(new Technician("niall@support.domain.com", "Niall Horan", "xxxx xxx xxx", "abc",
				AccountType.TECHNICIAN, 1));
		accounts.add(new Technician("liam@support.domain.com", "Liam Payne", "xxxx xxx xxx", "abc",
				AccountType.TECHNICIAN, 1));
		accounts.add(new Technician("louis@support.domain.com", "Louis Tomlinson", "xxxx xxx xxx", "abc",
				AccountType.TECHNICIAN, 2));
		accounts.add(new Technician("zayn@support.domain.com", "Zayn Malik", "xxxx xxx xxx", "abc",
				AccountType.TECHNICIAN, 2));
		mainMenu();
	}

	// Login menu
	public void mainMenu() {
		printTitle("Cinco Login");
		System.out.println("Select an option from the following:");
		System.out.println("1. Login");
		System.out.println("2. Forgot Password");
		System.out.println("3. Create Account");

		String choice = in.nextLine();

		switch (choice) {
		case "1":
			login();
			break;

		case "2":
			resetPassword();
			break;

		case "3":
			createAccount();
			break;

		default:
			System.err.format("%s is not a valid option.\n", choice);
		}
	}

	// Staff main menu
	public void systemMenu() {
		printTitle("Cinco");
		System.out.println("1. Submit a ticket");
		System.out.println("2. View submitted tickets");
		System.out.println("3. Log out");

		String choice = getInput("Please select an option:");

		switch (choice) {
		case "1":
			submitTicket();
			break;
		case "2":
			printTitle("Open Tickets");
			getUsersTickets(user);
			break;
		case "3":
			logout();
			break;
		default:
			System.err.println(choice + " is not a valid option.");
		}
	}

	// Technician main menu
	public void technicianMenu() {
		ArrayList<Ticket> validTickets = new ArrayList<Ticket>();
		printTitle("Cinco Technician");
		printTitle("Tickets");
		Technician tech = (Technician) user;
		int i = 1;
		for(Ticket ticket : tech.getAssignedTickets()) {
			ticket.printDetails(i);
			validTickets.add(ticket);
			i++;
		}
		
		for(Ticket ticket : getClosedTickets() ) {
			ticket.printDetails(i);
			validTickets.add(ticket);
			i++;
		}
		System.out.println("1. Change ticket severity");
		System.out.println("2. Change ticket status");
		System.out.println("3. Logout");
		
		String choice = getInput("Please select an option:");
		switch (choice) {
		case "1":
			changeTicketSeverity(validTickets);
			break;
		case "2":
			changeTicketStatus(validTickets);
			break;
		case "3":
			logout();
			break;
		default:
			System.err.println(choice + " is not a valid option.");
		}
	}
	
	public void changeTicketSeverity(ArrayList<Ticket> ticketList) {
		printTitle("Change Ticket Severity");
		if(ticketList.size() == 0) {
			System.err.println("No tickets found.");
			technicianMenu();
		}
		
		//TODO error check
		int index = Integer.parseInt(getInput("Which ticket do you want to modify:")) - 1;
		Ticket ticket = ticketList.get(index);
		System.out.println("That ticket currently has a severity of " + ticket.getSeverity().toString()+".");
		int newSeverity = Integer.parseInt(getInput("Please chose a number between 1 and 3 for the new severity:"));
		int oldSeverity = ticket.getSeverity().getSeverityInt();
		
		
		ticket.setSeverity(TicketSeverity.values()[newSeverity]);
		System.out.println("Ticket severity has been updated.");
		technicianMenu();
	}
	
	public void changeTicketStatus(ArrayList<Ticket> ticketList) {
		printTitle("Change Ticket Status");
		if(ticketList.size() == 0) {
			System.err.println("No tickets found.");
			technicianMenu();
		}
	}
	
	public ArrayList<Ticket> getClosedTickets() {
		ArrayList<Ticket> closedTickets = new ArrayList<Ticket>();
		for(Ticket ticket : tickets) {
			if(ticket.getStatus() != TicketStatus.OPEN) {
				closedTickets.add(ticket);
			}
		}
		return closedTickets;
	}
	
	// Staff submit ticket menu
	public void submitTicket() {
		printTitle("Submit a Ticket");
		String desc = getInput("Please provide a description of the problem:");
		System.out.println("Please rate the severity of the issue on a scale of 1 to 3.");
		// TODO error check
		int severity = Integer.parseInt(getInput("1 being low severity and 3 being high:"));
		Ticket ticket = new Ticket(desc, TicketSeverity.values()[severity-1], user);
		tickets.add(ticket);
		assignTicket(ticket);
	}
	
	public void assignTicket(Ticket ticket) {
		Technician technician = null;
		// Get technician with least assigned tickets
		int minAssignedTickets = Integer.MAX_VALUE;
		ArrayList<Technician> techs = getTechniciansOfLevel(ticket.getSeverity().getSeverityInt());
		for(Technician tech : techs) {
			if(tech.numTickets() < minAssignedTickets) {
				technician = tech;
				minAssignedTickets = technician.numTickets();
			}
		}
		System.out.println("Your ticket has been assigned to " + technician.getName() + ".\n");
		technician.assignTicket(ticket);
		systemMenu();
	}
	
	public void logout() {
		user = null;
		System.out.println("You have been logged out.");
		mainMenu();
	}
	
	public void getUsersTickets(Account account) {
		ArrayList<Ticket> openTickets = new ArrayList<Ticket>();
		for(Ticket ticket : tickets) {
			if(ticket.getOwner().equals(account) && ticket.getStatus() == TicketStatus.OPEN) {
				openTickets.add(ticket);
			}
		}
		
		if(openTickets.size() == 0) {
			System.out.println("You have no open tickets\n");
			systemMenu();
		}
		
		System.out.format("%-7s%-15s%-15s%-15s%n", "Number", "Description", "Severity", "Status");
		int i = 1;
		for(Ticket ticket : openTickets) {
			ticket.printDetails(i);
			i++;
		}
		getInput("Press enter to continue.\n");
		systemMenu();
	}
	
	public ArrayList<Technician> getTechniciansOfLevel(int level) {
		ArrayList<Technician> techs = new ArrayList<Technician>();
		for(Account account : accounts) {
			if(account.getType() == AccountType.TECHNICIAN) {
				Technician tech = (Technician) account;
				if(level < 3 && tech.getLevel() == 1) {
					techs.add(tech);
				} else if(level == 3 && tech.getLevel() == 2) {
					techs.add(tech);
				}
			}
		}
		return techs;
	}

	public void login() {
		printTitle("Login");
		String email = getInput("Email:");
		String password = getInput("Password:");
		user = validateLogin(email, password);
		if (user == null) {
			System.err.println("Your username or password are incorrect");
			mainMenu();
		}
		System.out.println("You have been logged in.\n");
		if(user.getType() == AccountType.STAFF) {
			systemMenu();
		} else {
			technicianMenu();
		}
	}

	public Account validateLogin(String email, String password) {
		for (Account a : accounts) {
			if (a.getEmail().equals(email) && a.getPassword().equals(password)) {
				return a;
			}
		}
		return null;
	}

	public void resetPassword() {
		printTitle("Reset Password");
		String email = getInput("Email:");
		for (Account a : accounts) {
			if (a.getEmail().equals(email)) {
				String password = getInput("New Password:");
				a.setPassword(password);
				System.out.println("Your password has been changed.\n");
				mainMenu();
			}
		}
		System.err.println(email + " could not be found.");
		mainMenu();
	}

	public void createAccount() {
		printTitle("Create Account");
		String email = getInput("Email:");
		String name = getInput("Full Name:");
		String phone = getInput("Phone Number:");
		String password = "";
		while (password.equals("")) {
			password = validatePassword(getInput("Password:"));
			if (password.equals("")) {
				System.err.println("Invalid Password.\n");
			}
		}
		System.out.println("Account Created.\n");
		user = new Account(email, name, phone, password, AccountType.STAFF);
		accounts.add(user);
		systemMenu();
	}

	public String validatePassword(String password) {
		boolean hasLower = false;
		boolean hasUpper = false;

		if (password.length() < 20) {
			return "";
		}

		for (int i = 0; i < password.length(); i++) {
			Character c = password.charAt(i);
			if (Character.isUpperCase(c)) {
				hasUpper = true;
			} else if (Character.isLowerCase(c)) {
				hasLower = true;
			}

			if (hasLower && hasUpper) {
				return password;
			}
		}

		return "";
	}

	// String Util classes

	public String getInput(String text) {
		if (text != "") {
			System.out.println(text);
		}
		return in.nextLine();
	}

	public void printTitle(String title) {
		System.out.format("\n=== %s ===\n\n", title);
	}

	public static void main(String args[]) {
		new Main();
	}
}