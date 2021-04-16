package main;

public class Ticket {
	private String description;
	private TicketSeverity severity;
	private TicketStatus status;
	private Account owner;

	public Ticket(String description, TicketSeverity severity, Account owner) {
		this.description = description;
		this.severity = severity;
		status = TicketStatus.OPEN;
		this.owner = owner;
	}
	
	public void printDetails(int index) {
		System.out.format("%-7d%-15s%-15s%-15s%n", index, description, severity.toString(), status.toString());
	}
	
	public TicketStatus getStatus() {
		return status;
	}
	
	public TicketSeverity getSeverity() {
		return severity;
	}
	
	public void setSeverity(TicketSeverity severity) {
		this.severity = severity;
	}
	
	public Account getOwner() {
		return owner;
	}
}
