package sg.edu.nus.cs2103t.omnitask.model;

import org.joda.time.DateTime;

public class Task {
	private DateTime startDate;
	
	private DateTime endDate;
	
	private long id;
	
	private int priority;
	
	private String name;
	
	// TODO: Might want to change this to an enum
	private int type;

	public Task() {
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
		return "Task [startDate=" + startDate + ", endDate=" + endDate
				+ ", id="
				+ id + ", name=" + name + ", type=" + type + "]";
	}
}
