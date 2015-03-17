package sg.edu.nus.cs2103t.omnitask.ui;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.cs2103t.omnitask.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ViewController {
	
	@FXML private ListView<Task> listView;
	
	@FXML private Text outputText;
	
	@FXML private TextField omniBar;
	
	private ArrayList<Task> tasks;
	
	public ViewController() {
		tasks = new ArrayList<Task>();
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
	
	public void updateListView(List<Task> tasks) {
		this.tasks.clear();
		this.tasks.addAll(tasks);
	}
}
