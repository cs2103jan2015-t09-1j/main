package sg.edu.nus.cs2103t.omnitask.model;

import org.joda.time.DateTime;

import java.util.UUID;

public class Task {
	public static enum Priority {
		NONE, LOW, MEDIUM, HIGH
	}

	private DateTime startDate;

	private DateTime endDate;

	private long id;

	private UUID uuid;

	private Priority priority;

	private boolean recurrence;

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

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Priority getPriority() {
		
		return this.priority;
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

	public boolean isRecurrence() {
		return recurrence;
	}

	public void setRecurrence(boolean recurrence) {
		this.recurrence = recurrence;
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
				+ ", id=" + id + ", name=" + name + ", type=" + type + "]";
	}

	public void setPriorityByNum(int prioNum) {
		switch (prioNum) {
		case 0:
			this.priority = Priority.NONE;
			break;
		case 1:
			this.priority = Priority.LOW;
			break;
		case 2:
			this.priority = Priority.MEDIUM;
			break;
		case 3:
			this.priority = Priority.HIGH;
			break;
		default:
			this.priority = Priority.LOW;
			break;
		}		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
	
}
