package sg.edu.nus.cs2103t.omnitask.model;

import org.joda.time.DateTime;

import sg.edu.nus.cs2103t.omnitask.model.Task.Priority;

// CommandInput object holds the parsed user commands parsed by Parser
public class CommandInput {

	public static enum CommandType {
		INVALID,
		ADD,
		DISPLAY,
		DELETE,
		EDIT,
		EXIT,
		SEARCH,
		HELP,
		UNDO,
		REDO,
		MARK,
		NEXT,
		PREV
	}
	
	private CommandType commandType;

	private DateTime startDate;

	private DateTime endDate;

	private long id;
	
	private boolean recurrence;
	
	private boolean isCompleted;
	
	private Priority priority = Priority.NONE;

	private String name;

	public CommandInput() {
	}
	
	public CommandInput(CommandType commandType) {
		this.commandType = commandType;
	}
	
	public CommandType getCommandType() {
		return commandType;
	}

	public void setCommandType(CommandType commandType) {
		this.commandType = commandType;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public boolean isRecurrence() {
		return recurrence;
	}

	public void setRecurrence(boolean recurrence) {
		this.recurrence = recurrence;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	public int convertPriorityToNum(Priority priority){
		int prioNum=0;
		switch (priority) {
		case NONE:
			prioNum=0;
			break;
		case LOW:
			prioNum=1;
			break;
		case MEDIUM:
			prioNum=2;
			break;
		case HIGH:
			prioNum=3;
			break;
		default:
			prioNum=0;
			break;
		}
		return prioNum;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}*/

	@Override
	public String toString() {
		return "CommandInput [commandType=" + commandType.toString() + ", startDate="
				+ startDate + ", endDate=" + endDate + ", id=" + id + ", name="
				+ name + "]";
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}



}
