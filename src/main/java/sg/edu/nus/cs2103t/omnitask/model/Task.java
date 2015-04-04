package sg.edu.nus.cs2103t.omnitask.model;

import java.util.Comparator;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Task {
	final private static String[] priorityColors = new String[] { "none",
			"#0099CC", "#FF8800", "#CC0000" };

	final private static String[] priorityStrings = new String[] { "", "low",
			"med", "high" };

	public static enum Priority {
		NONE, LOW, MEDIUM, HIGH
	}

	private DateTime startDate;

	private DateTime endDate;

	private long id;

	private UUID uuid;

	private Priority priority = Priority.NONE;

	private boolean recurrence;

	private boolean isCompleted;

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

	public boolean isDue() {
		if (endDate == null)
			return false;

		return new DateTime().now().isAfter(endDate);
	}

	private String formatTime(DateTime date) {
		String timeFormat = "";
		if (date.millisOfDay().get() != 0) {
			timeFormat = "hh:mm a";
		}

		if (timeFormat.equals("")) {
			return "";
		}

		DateTimeFormatter fmt = DateTimeFormat.forPattern(timeFormat);
		String formatted = fmt.print(date);

		return formatted.toUpperCase();
	}

	public String getFormattedTimeRange() {
		DateTime startDate = getStartDate();
		DateTime endDate = getEndDate();

		if (startDate == null && endDate == null) {
			return "";
		}

		String formatted = "";

		if (startDate != null) {
			String startDateFormatted = formatTime(startDate);
			formatted += startDateFormatted;
		}
		if (endDate != null) {
			String endDateFormatted = formatTime(endDate);
			if (!formatted.isEmpty()) {
				formatted += " - ";
			}
			formatted += endDateFormatted;
		}

		return formatted;
	}

	public String getFormattedDate() {
		DateTime startDate = getStartDate();
		DateTime endDate = getEndDate();

		if (startDate == null && endDate == null) {
			return null;
		}

		DateTime today = DateTime.now();
		DateTime tomorrow = today.plusDays(1);
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd MMM hh:mm");

		if (today.year().get() == endDate.year().get()) {
			fmt = DateTimeFormat.forPattern("EEEE, MMMM dd");
		} else {
			fmt = DateTimeFormat.forPattern("EEEE, MMMM dd, YYYY");
		}

		String endDateFormatted = fmt.print(endDate);

		if (today.getYear() == endDate.getYear()
				&& today.getDayOfYear() == endDate.getDayOfYear()) {
			endDateFormatted = "Today";
		} else if (tomorrow.getYear() == endDate.getYear()
				&& tomorrow.getDayOfYear() == endDate.getDayOfYear()) {
			endDateFormatted = "Tomorrow";
		}

		if (startDate == null) {
			return endDateFormatted;
		} else {
			if (today.year().get() == startDate.year().get()) {
				fmt = DateTimeFormat.forPattern("EEEE, MMMM dd");
			} else {
				fmt = DateTimeFormat.forPattern("EEEE, MMMM dd, YYYY");
			}

			String startDateFormatted = fmt.print(startDate);

			if (today.getYear() == startDate.getYear()
					&& today.getDayOfYear() == startDate.getDayOfYear()) {
				startDateFormatted = "Today";
			} else if (tomorrow.getYear() == startDate.getYear()
					&& tomorrow.getDayOfYear() == startDate.getDayOfYear()) {
				startDateFormatted = "Tomorrow";
			}

			if (startDateFormatted.equals(endDateFormatted)) {
				return startDateFormatted;
			}

			return fmt.print(startDate) + " - " + endDateFormatted;
		}
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

	public boolean isRecurrence() {
		return recurrence;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
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

	public String getPriorityString() {
		if (priority != null && priority.ordinal() < 4) {
			return priorityStrings[priority.ordinal()];
		}

		return priorityStrings[0];
	}

	public String getPriorityColor() {
		if (priority != null && priority.ordinal() < 4) {
			return priorityColors[priority.ordinal()];
		}

		return priorityColors[0];
	}

	public Task clone() {
		Task task = new Task();

		if (startDate != null) {
			task.setStartDate(new DateTime().withMillis(startDate.getMillis()));
		}
		if (endDate != null) {
			task.setEndDate(new DateTime().withMillis(endDate.getMillis()));
		}
		task.setId(id);
		if (uuid != null) {
			task.setUuid(UUID.fromString(uuid.toString()));
		}
		task.setPriority(priority);
		task.setRecurrence(recurrence);
		task.setName(name);
		task.setType(type);

		return task;
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

	// Sort task list according to 3 fields: due date, priority, id
	// General explanation for algorithm:
	// 1. If both task have no end dates OR both have but equal end dates -> If
	// priority not equal, sort by priority
	// 2. If either of the task have no end date, place the one with no end date
	// lower down the list (return 1) and the other, higher up (return -1
	// 3. If both task have end dates and they're not equal, use the DateTime
	// compareTo to resolve
	// 4. Default case: Sort by task id
	public static Comparator<Task> taskSorterComparator = new Comparator<Task>() {

		@Override
		public int compare(Task task1, Task task2) {
			if ((task1.getEndDate() == null && task2.getEndDate() == null)
					|| ((task1.getEndDate() != null && task2.getEndDate() != null) && task1
							.getEndDate().equals(task2.getEndDate()))) {
				if (task1.getPriority() != task2.getPriority()) {
					return task2.getPriority().ordinal()
							- task1.getPriority().ordinal();
				}
			} else if (task1.getEndDate() == null || task2.getEndDate() == null) {
				if (task1.getEndDate() == null) {
					return 1;
				} else {
					return -1;
				}
			} else {
				return task1.getEndDate().compareTo(task2.getEndDate());
			}

			return (int) (task1.getId() - task2.getId());
		}
	};

}
