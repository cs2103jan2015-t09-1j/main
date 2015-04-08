package sg.edu.nus.cs2103t.omnitask.model;

import org.joda.time.DateTime;

import sg.edu.nus.cs2103t.omnitask.model.Task.Priority;

// CommandInput object holds the parsed user commands parsed by Parser
public class CommandInput {

	public static enum CommandType {
		ADD, DELETE, DISPLAY, EDIT, EXIT, HELP, INVALID, MARK, NEXT, PREV, REDO, SEARCH, UNDO, STORAGE, REMOVEDATE, ARCHIVE, UNARCHIVE
	}

	private CommandType commandType;

	private DateTime endDate;

	private long id;

	private boolean isCompleted;

	private String name;

	private Priority priority;

	private boolean recurrence;

	private DateTime startDate;

	public CommandInput() {
	}

	public CommandInput(CommandType commandType) {
		this.commandType = commandType;
	}

	public int convertPriorityToNum(Priority priority) {
		int prioNum = 0;
		switch (priority) {
			case NONE:
				prioNum = 0;
				break;
			case LOW:
				prioNum = 1;
				break;
			case MEDIUM:
				prioNum = 2;
				break;
			case HIGH:
				prioNum = 3;
				break;
			default:
				prioNum = 0;
				break;
		}
		return prioNum;
	}

	public CommandType getCommandType() {
		return commandType;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Priority getPriority() {
		return priority;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public boolean isRecurrence() {
		return recurrence;
	}

	public void setCommandType(CommandType commandType) {
		this.commandType = commandType;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	/*
	 * public int getType() { return type; }
	 * 
	 * public void setType(int type) { this.type = type; }
	 */

	public void setRecurrence(boolean recurrence) {
		this.recurrence = recurrence;
	}

	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}

	@Override
	public String toString() {
		return "CommandInput [commandType=" + commandType.toString()
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", id="
				+ id + ", name=" + name + "]";
	}

}
