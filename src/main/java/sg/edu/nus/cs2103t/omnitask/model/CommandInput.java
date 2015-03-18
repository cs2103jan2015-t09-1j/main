package sg.edu.nus.cs2103t.omnitask.model;

import org.joda.time.DateTime;

// CommandInput object holds the parsed user commands parsed by Parser
public class CommandInput {
	public static String COMMAND_ADD = "add";

	public static String COMMAND_DISPLAY = "display";

	public static String COMMAND_DELETE = "delete";

	public static String COMMAND_EDIT = "edit";
	
	public static String COMMAND_EXIT = "exit";

	private String commandName;

	private DateTime startDate;

	private DateTime endDate;

	private long id;
	
	private int priority;

	private String name;

	// TODO: Might want to change this to an enum
	private int type;

	public CommandInput(String commandName) {
		super();
		this.commandName = commandName.toLowerCase();
	}

	// TODO: Add more constructors for mostly used combination of fields
	public CommandInput(String commandName, String name) {
		super();
		this.commandName = commandName.toLowerCase();
		this.name = name;
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName.toLowerCase();
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
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "CommandInput [commandName=" + commandName + ", startDate="
				+ startDate + ", endDate=" + endDate + ", id=" + id + ", name="
				+ name + ", type=" + type + "]";
	}



}
