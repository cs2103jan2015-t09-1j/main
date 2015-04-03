package sg.edu.nus.cs2103t.omnitask.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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

	public void updateAllTasks(List<Task> tasks, javafx.collections.ListChangeListener.Change<? extends Task> changes) {
		if (this.allTasks.size() == 0) {
			this.allTasks.addAll(tasks);
		} else {
			while (changes.next()) {
				for (Task task : changes.getRemoved()) {
					//System.out.println("Removed: " + task.getId() + " - " + task.getName());
					if (tasks.indexOf(task) == -1) {
						this.allTasks.remove(task);
					}
				}
				
				for (Task task : changes.getAddedSubList()) {
					//System.out.println("Added: " + task.getId() + " - " + task.getName());
					int pos = this.allTasks.indexOf(task);
					if (pos != -1) {
						this.allTasks.remove(task);
					}
					
					pos = tasks.indexOf(task);
					this.allTasks.add(pos, task);
				}
				
				//System.out.println("");
			}
		}
		
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
		
		Collections.sort(tasks, Task.taskSorterComparator);
		
		this.searchedTasks.clear();
		this.searchedTasks.addAll(tasks);
	}
	
	private ListChangeListener<Task> tasksChangeListener = new ListChangeListener<Task>() {

		@Override
		public void onChanged(javafx.collections.ListChangeListener.Change<? extends Task> changes) {
			ArrayList<Integer> indexChanged = new ArrayList<Integer>();
			while (changes.next()) {
				for (Task task : changes.getAddedSubList()) {
					indexChanged.add(allTasks.indexOf(task));
				}
			}
			
			int[] indexChangedArray = new int[indexChanged.size()];
			for (int i = 0; i < indexChanged.size(); i++) {
				indexChangedArray[i] = indexChanged.get(i);
			}
			
			refreshCards(indexChangedArray);
		}
		
	};
	
	public void setViewMode(ViewMode viewMode) {
		tasks.removeListener(tasksChangeListener);
		
		if (viewMode == ViewMode.ALL) {
			tasks = allTasks;
		} else if (viewMode == ViewMode.SEARCH) {
			tasks = searchedTasks;
		}
		
		tasks.addListener(tasksChangeListener);
		
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
		refreshCards(new int[]{});
	}
	
	private void refreshCards(int[] indexChanged) {
		if (agendaViewLoaded) {
			String indexChangedString = "";
			for (int index : indexChanged) {
				indexChangedString += index + ",";
			}
			if (!indexChangedString.isEmpty()) {
				indexChangedString = indexChangedString.substring(0, indexChangedString.length()-1);
			}
			
			agendaView.getEngine().executeScript("refreshCards([" + indexChangedString + "]);");
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
		public void debug(String msg) {
			System.out.println(msg);
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
