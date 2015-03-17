package sg.edu.nus.cs2103t.omnitask.ui;

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
import sg.edu.nus.cs2103t.omnitask.controller.Controller;
import sg.edu.nus.cs2103t.omnitask.model.Task;

public class ViewController {
	
	private Controller controller;
	
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
	        @Override
	        public ListCell<Task> call(ListView<Task> listView) {
	            return new ListViewCell();
	        }
	    });
	}

	public void setController(Controller controller) {
		this.controller = controller;
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
			controller.processUserInput(omniBar.getText());
			omniBar.setText("");
		}
    }
	
	public void updateListView(List<Task> tasks) {
		this.tasks.clear();
		this.tasks.addAll(tasks);
		
	}
	
	private class ListViewCell extends ListCell<Task> {
		@Override
		public void updateItem(Task task, boolean empty) {
		    super.updateItem(task, empty);
		    
		    if (task != null) {
		    	TaskItemController data = new TaskItemController();
		        data.setData(task);
		        setGraphic(data.getView());
		    }
		}
	}
}
