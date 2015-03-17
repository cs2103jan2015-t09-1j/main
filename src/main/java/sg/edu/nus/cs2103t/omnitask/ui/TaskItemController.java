package sg.edu.nus.cs2103t.omnitask.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import sg.edu.nus.cs2103t.omnitask.model.Task;

public class TaskItemController {
	@FXML private GridPane taskCellItem;
	
	@FXML private Label taskId;
	
	@FXML private Label taskPriority;
	
	@FXML private Label taskName;
	
	@FXML private Label taskDue;
	
	public TaskItemController() {
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("taskCellItemLayout.fxml"));
	    fxmlLoader.setController(this);
	    try {
	        fxmlLoader.load();
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}
	
	private String formatTime(DateTime date) {
		String timeFormat = "";
		if (date.millisOfDay().get() != 0) {
			if (date.minuteOfHour().get() == 0) {
				timeFormat = ", ha";
			} else {
				timeFormat = ", h:mma";
			}
		}
		
		if (timeFormat.equals("")) {
			return "";
		} 
		
		DateTimeFormatter fmt = DateTimeFormat.forPattern(timeFormat);
		String formatted = fmt.print(date);
		
		return formatted.toLowerCase();
	}
	
	private String formatTimeRange(Task task) {
		DateTime startDate = task.getStartDate();
		DateTime endDate = task.getEndDate();
		
		if (startDate == null && endDate == null) {
			return "";
		}
		
		String startDateFormatted = formatTime(startDate);
		String endDateFormatted = formatTime(endDate);
		
		return ", " + startDateFormatted.replace(", ", "") + " - " + endDateFormatted.replace(", ", "");
	}
	
	private String formatDate(Task task) {
		DateTime startDate = task.getStartDate();
		DateTime endDate = task.getEndDate();
		
		if (startDate == null && endDate == null) {
			return null;
		}
		
		DateTime today = DateTime.now();
		DateTime tomorrow = today.plusDays(1);
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd MMM hh:mm");
		
		if (today.year().get() == endDate.year().get()) {
			fmt = DateTimeFormat.forPattern("dd MMMM");
		} else {
			fmt = DateTimeFormat.forPattern("dd MMMM YYYY");
		}
		
		String endDateFormatted = fmt.print(endDate);
		
		if (today.getYear() == endDate.getYear() && today.getDayOfYear() == endDate.getDayOfYear()) {
			endDateFormatted = "today";
		} else if (tomorrow.getYear() == endDate.getYear() && tomorrow.getDayOfYear() == endDate.getDayOfYear()) {
			endDateFormatted = "tomorrow";
		}
		
		if (startDate == null) {
			return endDateFormatted + formatTime(endDate);
		} else {
			if (today.year().get() == startDate.year().get()) {
				fmt = DateTimeFormat.forPattern("dd MMMM");
			} else {
				fmt = DateTimeFormat.forPattern("dd MMMM YYYY");
			}
			
			String startDateFormatted = fmt.print(startDate);
			
			if (today.getYear() == startDate.getYear() && today.getDayOfYear() == startDate.getDayOfYear()) {
				startDateFormatted = "today";
			} else if (tomorrow.getYear() == startDate.getYear() && tomorrow.getDayOfYear() == startDate.getDayOfYear()) {
				startDateFormatted = "tomorrow";
			}
			
			if (startDateFormatted.equals(endDateFormatted)) {
				return startDateFormatted + formatTimeRange(task);
			}
			
			return fmt.print(startDate) + formatTime(startDate) + " - " + endDateFormatted + formatTime(endDate);
		}
	}
	
	public void setData(Task task) {
		taskId.setText(task.getId() + "");
		
		taskPriority.setText(task.getPriority() + "");
		
		taskName.setText(task.getName());
		
		String formattedDate = formatDate(task);
		if (formattedDate == null) {
			taskDue.setText("No Due Date");
			taskDue.setTextFill(Color.GRAY);
		} else {
			taskDue.setText(formattedDate);
		}
		
		if (task.getEndDate() != null && task.getEndDate().isBeforeNow()) {
			taskId.setTextFill(Color.RED);
			taskPriority.setTextFill(Color.RED);
			taskName.setTextFill(Color.RED);
			taskDue.setTextFill(Color.RED);
		}
	}
	
	public GridPane getView() {
		return taskCellItem;
	}
}
