package sg.edu.nus.cs2103t.omnitask.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import sg.edu.nus.cs2103t.omnitask.model.Task;

public class MainViewController {
	
	private UI ui;
	
	@FXML private Text viewModeText;
	
	@FXML private WebView agendaView;
	
	private boolean agendaViewLoaded;
	
	@FXML private Text outputText;
	
	@FXML private TextField omniBar;
	
	public ObservableList<Task> tasks;
	
	private ObservableList<Task> allTasks;
	
	private ObservableList<Task> searchedTasks;
	
	private String searchKeyword;
	
	private ArrayList<String> commandHistory;
	
	private int currentCommandHistoryIndex = -1;
	
	public static enum ViewMode {
		ALL,
		SEARCH
	}
	
	private ViewMode viewMode = ViewMode.ALL;
	
	public MainViewController() {
		allTasks = FXCollections.observableArrayList();
		searchedTasks = FXCollections.observableArrayList();
		tasks = allTasks;
		commandHistory = new ArrayList<String>();
	}
	
	@FXML
    protected void initialize() {
		setViewMode(ViewMode.ALL);

		agendaView.setContextMenuEnabled(false);
		agendaView.setZoom(javafx.stage.Screen.getPrimary().getDpi() / 96);
		agendaView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override public void changed(ObservableValue ov, State oldState, State newState) {
				if (newState == Worker.State.SUCCEEDED) {
					agendaViewLoaded = true;
				}
			}
		});
		
		agendaView.getEngine().load(getClass().getResource("/agendaView.html").toExternalForm());
		JSObject jsobj = (JSObject) agendaView.getEngine().executeScript("window");
		jsobj.setMember("java", new Bridge());
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
			
			commandHistory.add(omniBar.getText());
			currentCommandHistoryIndex = -1;
			
			omniBar.setText("");
		}
    }
	
	@FXML protected void onOmniBarKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.UP) {
			cyclePrevHistory();
			event.consume();
		} else if (event.getCode() == KeyCode.DOWN) {
			cycleNextHistory();
			event.consume();
		} else if (event.getCode() == KeyCode.TAB) {
			doAutoComplete();
			event.consume();
		}
	}
	
	private void doAutoComplete() {
		ArrayList<String> autocomplete = ui.invokeDoAutocompleteListener(omniBar.getText());
		if (autocomplete.size() > 0) {
			omniBar.setText(autocomplete.get(0));
			putOmniBarCaretAtEnd();
		}
	}
	
	private void cyclePrevHistory() {
		if (currentCommandHistoryIndex == 0) {
			putOmniBarCaretAtEnd();
			return;
		}
		
		cycleHandleHistory();
		
		if (currentCommandHistoryIndex == -1 && commandHistory.size() > 0) {
			currentCommandHistoryIndex = commandHistory.size();
		}
		
		omniBar.setText(commandHistory.get(--currentCommandHistoryIndex));
		putOmniBarCaretAtEnd();
	}
	
	private void cycleNextHistory() {
		if (currentCommandHistoryIndex == -1) {
			putOmniBarCaretAtEnd();
			return;
		}
		
		cycleHandleHistory();
		
		if (currentCommandHistoryIndex >= commandHistory.size() - 1) {
			currentCommandHistoryIndex = -1;
			omniBar.setText("");
			putOmniBarCaretAtEnd();
			return;
		}
		
		omniBar.setText(commandHistory.get(++currentCommandHistoryIndex));
		putOmniBarCaretAtEnd();
	}
	
	private void cycleHandleHistory() {
		if (omniBar.getText().trim().isEmpty()) {
			return;
		}
		
		if (currentCommandHistoryIndex == -1) {
			commandHistory.add(omniBar.getText());
		} else {
			commandHistory.set(currentCommandHistoryIndex, omniBar.getText());
		}
	}
	
	private void putOmniBarCaretAtEnd() {
		Platform.runLater(new Runnable() {

		
			public void run() {
				omniBar.end();
			}
			
		});
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
				return task2.getPriority().ordinal() - task1.getPriority().ordinal();
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
	

	public void setAllTasks(List<Task> tasks) {
		sortTasks(tasks);
		
		this.allTasks.clear();
		this.allTasks.addAll(tasks);
		
		// If we are in search view mode, delete the task which is no longer found, edit the one existing inside
		if (viewMode == ViewMode.SEARCH) {
			for (int i = 0; i < searchedTasks.size(); i++) {
				Task task = searchedTasks.get(i);
				int indexInAllTasks = this.allTasks.indexOf(task);
				
				if (indexInAllTasks == -1) {
					searchedTasks.remove(i--);
				} else {
					// This is a pretty slow operation :(
					searchedTasks.set(i, this.allTasks.get(indexInAllTasks));
				}
			}
		}
	}
	
	public void setSearchedTasks(String searchKeyword, List<Task> tasks) {
		this.searchKeyword = searchKeyword;
		
		sortTasks(tasks);
		
		this.searchedTasks.clear();
		this.searchedTasks.addAll(tasks);
	}
	
	private InvalidationListener tasksInvalidationListener = new InvalidationListener() {
		@Override
		public void invalidated(Observable arg0) {
			refreshCards();
		}
	};
	
	public void setViewMode(ViewMode viewMode) {
		tasks.removeListener(tasksInvalidationListener);
		
		if (viewMode == ViewMode.ALL) {
			tasks = allTasks;
		} else if (viewMode == ViewMode.SEARCH) {
			tasks = searchedTasks;
		}
		
		tasks.addListener(tasksInvalidationListener);
		
		this.viewMode = viewMode;
		refreshCards();
		
		updateViewModeText();
	}
	
	private void updateViewModeText() {
		if (viewMode == ViewMode.ALL) {
			viewModeText.setText("All Tasks");
		} else if (viewMode == ViewMode.SEARCH) {
			viewModeText.setText("Search results for \"" + searchKeyword + "\"");
		}

	}
	
	private void refreshCards() {
		if (agendaViewLoaded) {
			agendaView.getEngine().executeScript("refreshCards();");
		}
	}
	
	private List<Task> getTasksAsList() {
		ArrayList<Task> tasks = new ArrayList<Task>();
		for (Task task : this.tasks) {
			tasks.add(task);
		}
		return tasks;
	}
	
	public class Bridge {
		public String helloWorld() {
			return "Hello! " + new Random().nextInt(100);
		}
		
		public List<Task> getTasks() {
			return getTasksAsList();
		}
		
		public void markTaskAsDone(String uuid) {
			// TODO: Implement
			System.out.println("markTaskAsDone: " + uuid);
		}
		
		public void markTaskAsNotDone(String uuid) {
			// TODO: Implement
			System.out.println("markTaskAsNotDone: " + uuid);
		}
	}

}
