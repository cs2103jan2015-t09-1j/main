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
			commandHistory.add(omniBar.getText());
			currentCommandHistoryIndex = -1;
			
			if (ui.invokeCommandReceivedListener(omniBar.getText())) {
				omniBar.setText("");
			} else {
				omniBar.selectAll();
			}
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
	
	// replace current input with prev command (if any)
	private void cyclePrevHistory() {
		if (currentCommandHistoryIndex == 0) {
			putOmniBarCaretAtEnd();
			return;
		}
		
		cycleHandleHistory();
		
		if (currentCommandHistoryIndex == -1 && commandHistory.size() > 0) {
			currentCommandHistoryIndex = commandHistory.size();
		} else if (currentCommandHistoryIndex == -1 && commandHistory.size() == 0) {
			putOmniBarCaretAtEnd();
			return;
		}
		
		omniBar.setText(commandHistory.get(--currentCommandHistoryIndex));
		putOmniBarCaretAtEnd();
	}
	
	// replace current input with next command (if any)
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
	
	// saves current input into commandHistory (if not empty)
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
			if (viewMode == ViewMode.ALL) {
				addAllCards();
			}
		} else {
			int ind = 0;
			while (changes.next()) {
				//System.out.println("changeIndex: " + ind++);
				for (Task task : changes.getRemoved()) {
					//System.out.println("Removed: " + task.getId() + " - " + task.getName());
					int pos = tasks.indexOf(task);
					if (pos == -1) {
						if (viewMode == ViewMode.ALL) {
							removeCard(task.getUuid().toString());
						}
						this.allTasks.remove(task);
					}
				}
				
				for (Task task : changes.getAddedSubList()) {
					//System.out.println("Added: " + task.getId() + " - " + task.getName());
					boolean editing = false;
					
					int oldPos = this.allTasks.indexOf(task);
					if (oldPos != -1) {
						this.allTasks.remove(task);
						editing = true;
					}
					
					int pos = tasks.indexOf(task);
					this.allTasks.add(pos, task);
					
					if (editing && viewMode == ViewMode.ALL) {
						if (changes.getAddedSize() > 1) {
							editCard(task.getUuid().toString(), pos);
						} else {
							editSingleCard(task.getUuid().toString(), pos);
						}
					} else if (viewMode == ViewMode.ALL) {
						addSingleCard(pos);
					}
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
			
			addAllCards();
		}
	}
	
	public void setSearchedTasks(String searchKeyword, List<Task> tasks) {
		this.searchKeyword = searchKeyword;
		
		Collections.sort(tasks, Task.taskSorterComparator);
		
		this.searchedTasks.clear();
		this.searchedTasks.addAll(tasks);
	}
	
	public void setViewMode(ViewMode viewMode) {
		if (viewMode == ViewMode.ALL) {
			tasks = allTasks;
		} else if (viewMode == ViewMode.SEARCH) {
			tasks = searchedTasks;
		}
		
		this.viewMode = viewMode;
		addAllCards();
		
		updateViewModeText();
	}
	
	private void updateViewModeText() {
		if (viewMode == ViewMode.ALL) {
			viewModeText.setText("All Tasks");
		} else if (viewMode == ViewMode.SEARCH) {
			viewModeText.setText("Search results for \"" + searchKeyword + "\"");
		}

	}
	
	private void addAllCards() {
		if (agendaViewLoaded) {
			agendaView.getEngine().executeScript("addAllCards();");
		}
	}
	
	private void addCard(int index) {
		if (agendaViewLoaded) {
			agendaView.getEngine().executeScript("addCard(" + index + ");");
		}
	}
	
	private void addSingleCard(int index) {
		if (agendaViewLoaded) {
			agendaView.getEngine().executeScript("addSingleCard(" + index + ");");
		}
	}
	
	private void editCard(String uuid, int index) {
		if (agendaViewLoaded) {
			agendaView.getEngine().executeScript("editCard('" + uuid + "', " + index + ");");
		}
	}
	
	private void editSingleCard(String uuid, int index) {
		if (agendaViewLoaded) {
			agendaView.getEngine().executeScript("editSingleCard('" + uuid + "', " + index + ");");
		}
	}
	
	private void removeCard(String uuid) {
		if (agendaViewLoaded) {
			agendaView.getEngine().executeScript("removeCard('" + uuid + "');");
		}
	}
	
	private List<Task> getTasksAsList() {
		ArrayList<Task> tasks = new ArrayList<Task>();
		for (Task task : this.tasks) {
			tasks.add(task);
		}
		return tasks;
	}
	
	private Task getTaskByUuid(String uuid) {
		for (Task task : this.tasks) {
			if (task.getUuid().toString().equals(uuid))  {
				return task;
			}
		}
		
		return null;
	}
	
	private List<Task> getSearchedTasksAsList() {
		ArrayList<Task> tasks = new ArrayList<Task>();
		for (Task task : this.searchedTasks) {
			tasks.add(task);
		}
		return tasks;
	}
	
	public class Bridge {
		public void debug(String msg) {
			System.out.println(msg);
		}
		
		public void redraw() {
			agendaView.requestLayout();
			ui.redraw();
		}
		
		public Task getTask(int index) {
			return tasks.get(index);
		}
		
		public List<Task> getTasks() {
			if (viewMode == ViewMode.ALL) {
				return getTasksAsList();
			} else {
				return getSearchedTasksAsList();
			}
		}
		
		public boolean markTaskAsDone(String uuid) {
			Task task = getTaskByUuid(uuid);
			task.setCompleted(true);
			return ui.invokeUpdateTask(task);
		}
		
		public boolean markTaskAsNotDone(String uuid) {
			Task task = getTaskByUuid(uuid);
			task.setCompleted(false);
			return ui.invokeUpdateTask(task);
		}
	}

}
