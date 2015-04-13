package sg.edu.nus.cs2103t.omnitask.item;

import org.joda.time.DateTime;

import sg.edu.nus.cs2103t.omnitask.item.Task.Priority;

// CommandInput object holds the parsed user commands parsed by Parser
/**
 * CommandInput is a object that holds the parsed user commands parsed by Parser
 *
 */
public class CommandInput {

	/**
	 * These enumerators are command types which will be recognized by the data class 
	 * for respective processing of adding, deleting, etc...
	 *
	 */
	public static enum CommandType {
		ADD, DELETE, SHOW, EDIT, EXIT, HELP, INVALID, MARK, NEXT, PREV, REDO, SEARCH, UNDO, STORAGE, REMOVEDATE, ARCHIVE, UNARCHIVE
	}

	private CommandType commandType;

	private DateTime endDate;

	private long id;

	private boolean isCompleted;

	private String name;

	private Priority priority;

	private boolean recurrence;

	private DateTime startDate;
/**
 * This creates the CommandInput object.
 */
	public CommandInput() {
	}
/**
 * This method reads the CommandType parsed by Parser.
 * @param commandType 
 *               commandType of a commandInput that is being processed
 */
	public CommandInput(CommandType commandType) {
		this.commandType = commandType;
	}
/**
 * This method converts the priorities of High, Medium, Low, None to numerical representations
 * so that it is recognized by data and storage classes.
 * @param priority 
 *                priority of a certain task
 * @return
 *                returns the numerical representation of priorities
 */
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
	
/**
 * This method returns the commandType of a task when it is called.
 * 
 * @return commandType
 */
	public CommandType getCommandType() {
		return commandType;
	}
	
/**
 * This method returns the due date of a task in dateTime form.
 * @return endDate
 */
	public DateTime getEndDate() {
		return endDate;
	}
	
/**
 * This method returns the reference index of the task.
 * @return reference id
 */
	public long getId() {
		return id;
	}

/**
 * This method returns the taskName of a task.
 * @return taskName
 */
	public String getName() {
		return name;
	}

/**
 * This method returns the priority of a task.	
 * @return priority
 */
	public Priority getPriority() {
		return priority;
	}

/**
 * This method returns the start date of a task in dateTime form.	
 * @return startDate
 */
	public DateTime getStartDate() {
		return startDate;
	}

/**
 * This method returns a boolean value indicating if the task is completed.
 * @return true if task is completed; false if not completed
 */
	public boolean isCompleted() {
		return isCompleted;
	}

/**
 * This method returns a boolean value indicating if the task is a recurring task.	
 * @return true if task is recurring; false if not recurring
 */
	public boolean isRecurrence() {
		return recurrence;
	}

/**
 * This method set the commandType of a task to the new input value.	
 * @param commandType new commadType from input
 */
	public void setCommandType(CommandType commandType) {
		this.commandType = commandType;
	}

/**
 * This method updates the status of a task on whether it is completed or not.
 * @param isCompleted new status of completion from input
 */
	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

/**
 * This method updates the endDate of task with the new input dateTime.	
 * @param endDate new endDate from user input in dateTime form
 */
	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

/**
 * This method updates the reference id of a task.	
 * @param id new reference id entered by user.
 */
	public void setId(long id) {
		this.id = id;
	}

/**
 * This method updates the taskName of a task.	
 * @param name new taskName entered by user
 */
	public void setName(String name) {
		this.name = name;
	}

/**
 * This method updates the priority of a task.
 * @param priority new priority entered by user
 */
	public void setPriority(Priority priority) {
		this.priority = priority;
	}

/**
 * This method updates the recurrence attribute of a task.	
 * @param recurrence new boolean value indicating recurrence entered by user
 */
	public void setRecurrence(boolean recurrence) {
		this.recurrence = recurrence;
	}

/**
 * This method updates the startDate of a task.
 * @param startDate new startDate in dateTime form entered by user
 */
	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}

/**
 * This method transform the attributes of a task into a String so that it can be stored 
 * and write to storage file.
 */
	@Override
	public String toString() {
		return "CommandInput [commandType=" + commandType.toString()
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", id="
				+ id + ", name=" + name + "]";
	}

}
