package sg.edu.nus.cs2103t.omnitask.model;

import org.joda.time.DateTime;

// CommandInput object holds the parsed user commands parsed by Parser
public class CommandInput {
	public static String COMMAND_ADD = "add";
	
	public static String COMMAND_DISPLAY = "display";
	
	private String commandName;
	
	private DateTime startDate;
	
	private DateTime endDate;
	
	private DateTime startTime;
	
	private DateTime endTime;
	
	private long id;
	
	private String name;
	
	// TODO: Might want to change this to an enum
	private int type;

	public CommandInput(String commandName) {
		super();
		this.commandName = commandName;
	}

	// TODO: Add more constructors for mostly used combination of fields
	public CommandInput(String commandName, String name) {
		super();
		this.commandName = commandName;
		this.name = name;
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
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

	public DateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
				+ startDate + ", endDate=" + endDate + ", startTime="
				+ startTime + ", endTime=" + endTime + ", id=" + id + ", name="
				+ name + ", type=" + type + "]";
	}
	
}
