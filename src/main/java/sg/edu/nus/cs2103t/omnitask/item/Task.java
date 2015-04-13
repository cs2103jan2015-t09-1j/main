package sg.edu.nus.cs2103t.omnitask.item;

import java.util.Comparator;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

//@author A0111795A
/**
 * This class holds info on Task details. The usual way to construct this object is to fill in the data based on CommandInput object.
 * If a Task has end date but no start date, it is a Deadline Task.
 * If a Task has both start and end dates, it is a Timed Task.
 * If a Task has no dates, it is a Floating Task.
 *
 */
public class Task {
	/**
	 * There are 4 levels of Priority for a Task.
	 */
	public static enum Priority {
		/**
		 * No Priority
		 */
		NONE,
		/**
		 * Low Priority
		 */
		LOW,
		/**
		 * Medium Priority
		 */
		MEDIUM,
		/**
		 * High Priority
		 */
		HIGH
	}

	/**
	* Sort task list according to 3 fields: dates, priority, id
	* General explanation for algorithm:
	* 1. If both task have no end dates OR both have but equal end dates -> If
	* priority not equal, sort by priority
	* 2. If either of the task have no end date, place the one with no end date
	* lower down the list (return 1) and the other, higher up (return -1
	* 3. If both task have end dates and they're not equal, use the DateTime
	* compareTo to resolve
	* 4. Default case: Sort by task id
	*/
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
				if (task1.getStartDate() != null
						&& task2.getStartDate() != null) {
					return task1.getStartDate().compareTo(task2.getStartDate());
				} else if (task1.getStartDate() != null) {
					return task1.getStartDate().compareTo(task2.getEndDate());
				} else if (task2.getStartDate() != null) {
					return task1.getEndDate().compareTo(task2.getStartDate());
				} else {
					return task1.getEndDate().compareTo(task2.getEndDate());
				}
			}

			return (int) (task1.getId() - task2.getId());
		}
	};

	/**
	 * Contains values of colors to be used for rendering by UI
	 */
	final private static String[] priorityColors = new String[] { "none",
			"#0099CC", "#FF8800", "#CC0000" };

	/**
	 * Contains the strings to be used for rendering by UI
	 */
	final private static String[] priorityStrings = new String[] { "", "low",
			"med", "high" };

	private boolean isArchived;

	private DateTime endDate;

	private long id;

	private boolean isCompleted;

	private String name;

	private Priority priority = Priority.NONE;

	private DateTime startDate;

	private UUID uuid;

	/**
	 * Default constructor has no arguments.
	 */
	public Task() {
	}

	/**
	* Clone this object. Used by Data component when returning Task so that the object does not get modified in Data cache when changes are made in other components.
	*/
	// When you add a new field to the class, this method needs to be updated too
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
		task.setArchived(isArchived);
		task.setPriority(priority);
		task.setName(name);
		task.setCompleted(isCompleted);

		return task;
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

	/**
	 * Get the end date for this Task.
	 * 
	 * @return null If there is no end date
	 * @return DateTime If there is a end date
	 */
	public DateTime getEndDate() {
		return endDate;
	}

	/**
	 * Get the date in formatted form suitable for display in UI.
	 * It will automatically format the date depending on what details are available and what type of Task it is.
	 * 
	 * @return String Formatted date
	 */
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

	/**
	 * Get the time in formatted form suitable for display in UI.
	 * It will automatically format the time depending on what details are available and what type of Task it is.
	 * 
	 * @return String Formatted time
	 */
	public String getFormattedTimeRange() {
		DateTime startDate = getStartDate();
		DateTime endDate = getEndDate();

		if (startDate == null && endDate == null) {
			return "";
		}

		String formatted = "";
		boolean twoDifferentDays = false;
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd MMMM, ");

		if (startDate != null && endDate != null) {
			twoDifferentDays = !startDate.withMillisOfDay(0).equals(
					endDate.withMillisOfDay(0));
		}

		if (startDate != null) {
			String startDateFormatted = formatTime(startDate);
			if (twoDifferentDays && !startDateFormatted.isEmpty()) {
				formatted += fmt.print(startDate);
			}
			formatted += startDateFormatted;
		}
		if (endDate != null) {
			String endDateFormatted = formatTime(endDate);
			if (!formatted.isEmpty()) {
				formatted += " - ";
			}
			if (twoDifferentDays && !endDateFormatted.isEmpty()) {
				formatted += fmt.print(endDate);
			}
			formatted += endDateFormatted;
		}

		return formatted;
	}

	/**
	 * Get Task Id/Index/Ref Id.
	 * 
	 * @return int Task Id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Get Task name.
	 * 
	 * @return String Task name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get Task priority.
	 * 
	 * @return Priority Task priority
	 */
	public Priority getPriority() {
		return this.priority;
	}

	/**
	 * Get Task priority color to be used by UI.
	 * 
	 * @return String Color of priority in hex format
	 */
	public String getPriorityColor() {
		if (priority != null && priority.ordinal() < 4) {
			return priorityColors[priority.ordinal()];
		}

		return priorityColors[0];
	}

	/**
	 * Get Task priority label to be used by UI.
	 * 
	 * @return String Label of priority
	 */
	public String getPriorityString() {
		if (priority != null && priority.ordinal() < 4) {
			return priorityStrings[priority.ordinal()];
		}

		return priorityStrings[0];
	}

	/**
	 * Get the start date for this Task.
	 * 
	 * @return DateTime Start date of this Task
	 */
	public DateTime getStartDate() {
		return startDate;
	}

	/**
	 * Get the Uuid for this Task. Uuid is a unique identifier for this Task.
	 * 
	 * @return UUID Unique id for this Task
	 */
	public UUID getUuid() {
		return uuid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	/**
	 * Return if this Task is done.
	 * 
	 * @return true if Task is completed
	 * @return false if Task is not completed
	 */
	public boolean isCompleted() {
		return isCompleted;
	}
	
	/**
	 * Return if this Task is due.
	 * 
	 * @return true if Task is due
	 * @return false if Task is not due
	 */
	public boolean isDue() {
		if (endDate == null)
			return false;

		return DateTime.now().isAfter(endDate);
	}

	/**
	 * Set if the Task is completed.
	 * 
	 * @param isCompleted true if Task is completed
	 */
	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
	
	/**
	 * Set the value for Task end date.
	 * 
	 * @param endDate Task end date
	 */
	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

	/**
	 * Set Task id.
	 * 
	 * @param id Task id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Set Task name.
	 * 
	 * @param name Task name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set Task priority.
	 * 
	 * @param priority Task priority
	 */
	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	/**
	 * Set start date for Task.
	 * 
	 * @param startDate Task start date
	 */
	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * Set Task Uuid.
	 * 
	 * @param uuid Task UUID
	 */
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "Task [startDate=" + startDate + ", endDate=" + endDate
				+ ", id=" + id + ", name=" + name + "]";
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

	/**
	 * Return if Task is archived.
	 * 
	 * @return true if Task is archived
	 * @return false if Task is not archived
	 */
	public boolean isArchived() {
		return isArchived;
	}

	/**
	 * Set if the Task is archived.
	 * 
	 * @param isArchived true if Task is archived.
	 */
	public void setArchived(boolean isArchived) {
		this.isArchived = isArchived;
	}

}
