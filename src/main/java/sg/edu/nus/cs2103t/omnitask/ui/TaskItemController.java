package sg.edu.nus.cs2103t.omnitask.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
	
	public void setData(Task task) {
		taskId.setText(task.getId() + "");
		taskPriority.setText(task.getPriority() + "");
		taskName.setText(task.getName());
		
		if (task.getEndDate() != null) {
			taskDue.setText(task.getEndDate().toString());
		}
	}
	
	public GridPane getView() {
		return taskCellItem;
	}
}
