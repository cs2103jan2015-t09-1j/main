package sg.edu.nus.cs2103t.omnitask.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Callback;
import sg.edu.nus.cs2103t.omnitask.model.Task;

public class ViewController {
	
	private UI ui;
	
	@FXML private ListView<Task> listView;
	
	@FXML private Text outputText;
	
	@FXML private TextField omniBar;
	
	private ObservableList<Task> tasks;
	
	public ViewController() {
		tasks = FXCollections.observableArrayList();
	}
	
	@FXML
    protected void initialize() {
		listView.setItems(tasks);
	    listView.setCellFactory(new Callback<ListView<Task>, javafx.scene.control.ListCell<Task>>() {
	       // @Override
	        public ListCell<Task> call(ListView<Task> listView) {
	            return new ListViewCell();
	        }
	    });
	}

	public void setUI(UI ui) {
		this.ui = ui;
	}

	public void showMessage(String msg) {
		outputText.setText(msg);
	}

	public void showError(String msg) {
		outputText.setText("Error: " + msg);
	}
	
	public void focusOmniBar() {
		omniBar.requestFocus();
	}
	
	@FXML protected void onOmniBarEnter(ActionEvent event) {
		if (!omniBar.getText().trim().equals("")) {
			ui.invokeCommandReceivedListener(omniBar.getText());
			omniBar.setText("");
		}
    }
	
	// Sort task list according to 3 fields: due date, priority, id 
	// General explanation for algorithm:
	// 1. If both task have no end dates OR both have but equal end dates -> If priority not equal, sort by priority
	// 2. If either of the task have no end date, place the one with no end date lower down the list (return 1) and the other, higher up (return -1
	// 3. If both task have end dates and they're not equal, use the DateTime compareTo to resolve
	// 4. Default case: Sort by task id
	private int taskSorterComparator(Task task1, Task task2) {
		if ((task1.getEndDate() == null && task2.getEndDate() == null)
				|| ((task1.getEndDate() != null && task2.getEndDate() != null) 
						&& task1.getEndDate().equals(task2.getEndDate()))) {
			if (task1.getPriority() != task2.getPriority()) {
				return task1.getPriority() - task2.getPriority();
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
	
	public void sortTasks(List<Task> tasks) {
		Collections.sort(tasks, new Comparator<Task>() {

			//@Override
			public int compare(Task t1, Task t2) {
				return taskSorterComparator(t1, t2);
			}
			
		});
	}
	
	public void updateListView(List<Task> tasks) {
		// Sort tasks again
		// ...while it may be counter productive to sort every time, the sorting is fast if it is already (mostly) sorted
		sortTasks(tasks);
		
		this.tasks.clear();
		this.tasks.addAll(tasks);
	}
	
	private class ListViewCell extends ListCell<Task> {
		@Override
		public void updateItem(Task task, boolean empty) {
		    super.updateItem(task, empty);
		    
		    if (empty || task == null) {
		        setGraphic(null);
		        setText(null);
		    } else if (task != null) {
		    	TaskItemController data = new TaskItemController();
		        data.setData(task);
		        setGraphic(data.getView());
		    }
		}
	}

}
