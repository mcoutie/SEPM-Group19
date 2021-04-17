package main;

import java.util.ArrayList;

public class Technician extends Account {

	private int level;
	private ArrayList<Ticket> tickets;
	
	public Technician(String email, String name, String phone, String password, AccountType type, int level) {
		super(email, name, phone, password, type);
		this.level = level;
		tickets = new ArrayList<Ticket>();
	}
	
	public int getLevel() {
		return level;
	}
	
	public int numTickets() {
		return tickets.size();
	}
	
	public void assignTicket(Ticket ticket) {
		tickets.add(ticket);
	}
	
	public void unassignTicket(Ticket ticket) {
		tickets.remove(ticket);
	}
	
	public ArrayList<Ticket> getAssignedTickets() {
		return tickets;
	}

}
