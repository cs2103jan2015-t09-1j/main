package sg.edu.nus.cs2103t.omnitask.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javafx.application.Platform;
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
import sg.edu.nus.cs2103t.omnitask.item.Task;
import sg.edu.nus.cs2103t.omnitask.item.Task.Priority;
import sg.edu.nus.cs2103t.omnitask.parser.ParserMainImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandDisplayImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandEditImpl;

public class MainViewController {

	public static enum ViewMode {
		ALL, ALTERNATE, ARCHIVED
	}
	
	final public static String SECTION_OVERDUE = "Overdue!";
	
	final public static String SECTION_FLOATING = "No Due Date";

	public ObservableList<Task> tasks;

	@FXML
	private WebView agendaView;

	private boolean agendaViewLoaded;

	private ObservableList<Task> allTasks;

	private ArrayList<String> commandHistory;

	private int currentCommandHistoryIndex = -1;

	@FXML
	private TextField omniBar;

	private ObservableList<Task> altTasks;

	private UI ui;

	private ViewMode viewMode = ViewMode.ALL;

	@FXML
	private Text viewModeText;

	public MainViewController() {
		allTasks = FXCollections.observableArrayList();
		altTasks = FXCollections.observableArrayList();
		tasks = allTasks;
		commandHistory = new ArrayList<String>();
	}

	public void focusOmniBar() {
		omniBar.requestFocus();
	}
	
	public double getOmniBarHeight() {
		return omniBar.getHeight();
	}

	public void scrollDown() {
		if (agendaViewLoaded) {
			agendaView.getEngine().executeScript("scrollDown();");
		}
	}

	public void scrollUp() {
		if (agendaViewLoaded) {
			agendaView.getEngine().executeScript("scrollUp();");
		}
	}
	
	public void scrollToSection(DateTime endDate) {
		scrollToSection(null, endDate);
	}
	
	public void scrollToSection(DateTime startDate, DateTime endDate) {
		Task task = new Task();
		task.setStartDate(startDate);
		task.setEndDate(endDate);
		
		scrollToSection(task.getFormattedDate());
	}
	
	public void scrollToSection(String section) {
		if (agendaViewLoaded) {
			ui.showMessage("Showing \"" + section + "\" Tasks");
			agendaView.getEngine().executeScript("scrollToSection('" + section + "');");
		}
	}

	public void setAlternateTasks(String title, List<Task> tasks) {
		Collections.sort(tasks, Task.taskSorterComparator);
		
		viewModeText.setText(title);

		this.altTasks.clear();
		this.altTasks.addAll(tasks);
	}

	public void setUI(UI ui) {
		this.ui = ui;
	}

	public void setViewMode(ViewMode viewMode) {
		if (viewMode == ViewMode.ALL) {
			tasks = allTasks;
		} else if (viewMode == ViewMode.ALTERNATE || viewMode == ViewMode.ARCHIVED) {
			tasks = altTasks;
		}

		this.viewMode = viewMode;
		addAllCards();

		updateViewModeText();
	}

	public void showError(String msg) {
		showToast("Error: " + msg);
	}

	public void showMessage(String msg) {
		showToast(msg);
	}

	public void updateAllTasks(List<Task> tasks,
			javafx.collections.ListChangeListener.Change<? extends Task> changes) {
		if (this.allTasks.size() == 0) {
			this.allTasks.addAll(tasks);
			if (viewMode == ViewMode.ALL) {
				addAllCards();
			}
		} else {
			int ind = 0;
			while (changes.next()) {
				// System.out.println("changeIndex: " + ind++);
				for (Task task : changes.getRemoved()) {
					// System.out.println("Removed: " + task.getId() + " - " +
					// task.getName());
					int pos = tasks.indexOf(task);
					if (pos == -1) {
						if (viewMode == ViewMode.ALL) {
							removeCard(task.getUuid().toString());
						}
						this.allTasks.remove(task);
					}
				}

				for (Task task : changes.getAddedSubList()) {
					// System.out.println("Added: " + task.getId() + " - " +
					// task.getName());
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

				// System.out.println("");
			}
		}

		// If we are in alternate view mode, delete the task which is no longer
		// found, edit the one existing inside
		if (viewMode != ViewMode.ALL) {
			for (int i = 0; i < altTasks.size(); i++) {
				Task task = altTasks.get(i);
				int indexInAllTasks = this.allTasks.indexOf(task);

				if (indexInAllTasks == -1) {
					altTasks.remove(i--);
				} else {
					// This is a pretty slow operation :(
					altTasks.set(i, this.allTasks.get(indexInAllTasks));
				}
			}

			addAllCards();
		}
	}

	@FXML
	protected void initialize() {
		setViewMode(ViewMode.ALL);

		agendaView.setContextMenuEnabled(false);
		agendaView.setZoom(javafx.stage.Screen.getPrimary().getDpi() / 96);
		agendaView.getEngine().getLoadWorker().stateProperty()
				.addListener(new ChangeListener<State>() {
					@Override
					public void changed(ObservableValue ov, State oldState,
							State newState) {
						if (newState == Worker.State.SUCCEEDED) {
							agendaViewLoaded = true;
						}
					}
				});

		agendaView.getEngine().load(
				getClass().getResource("/agendaView.html").toExternalForm());
		JSObject jsobj = (JSObject) agendaView.getEngine().executeScript(
				"window");
		jsobj.setMember("java", new Bridge());

		omniBar.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String oldValue, String newValue) {
				ui.invokeShowMiniHelpIfAvailable(newValue);
			}

		});
	}

	@FXML
	protected void onOmniBarEnter(ActionEvent event) {
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

	@FXML
	protected void onOmniBarKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.UP) {
			cyclePrevHistory();
			event.consume();
		} else if (event.getCode() == KeyCode.DOWN) {
			cycleNextHistory();
			event.consume();
		} else if (event.getCode() == KeyCode.TAB) {
			doAutoComplete();
			event.consume();
		} else if (event.getCode() == KeyCode.PAGE_DOWN) {
			scrollDown();
			event.consume();
		} else if (event.getCode() == KeyCode.PAGE_UP) {
			scrollUp();
			event.consume();
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
			agendaView.getEngine().executeScript(
					"addSingleCard(" + index + ");");
		}
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

	// replace current input with prev command (if any)
	private void cyclePrevHistory() {
		if (currentCommandHistoryIndex == 0) {
			putOmniBarCaretAtEnd();
			return;
		}

		cycleHandleHistory();

		if (currentCommandHistoryIndex == -1 && commandHistory.size() > 0) {
			currentCommandHistoryIndex = commandHistory.size();
		} else if (currentCommandHistoryIndex == -1
				&& commandHistory.size() == 0) {
			putOmniBarCaretAtEnd();
			return;
		}

		omniBar.setText(commandHistory.get(--currentCommandHistoryIndex));
		putOmniBarCaretAtEnd();
	}

	private void doAutoComplete() {
		ArrayList<String> autocomplete = ui
				.invokeDoAutocompleteListener(omniBar.getText());
		if (autocomplete.size() > 0) {
			omniBar.setText(autocomplete.get(0));
			putOmniBarCaretAtEnd();
		}
	}

	private void editCard(String uuid, int index) {
		if (agendaViewLoaded) {
			agendaView.getEngine().executeScript(
					"editCard('" + uuid + "', " + index + ");");
		}
	}

	private void editSingleCard(String uuid, int index) {
		if (agendaViewLoaded) {
			agendaView.getEngine().executeScript(
					"editSingleCard('" + uuid + "', " + index + ");");
		}
	}

	private List<Task> getAlternateTasksAsList() {
		ArrayList<Task> tasks = new ArrayList<Task>();
		for (Task task : this.altTasks) {
			tasks.add(task);
		}
		return tasks;
	}

	private Task getTaskByUuid(String uuid) {
		for (Task task : this.tasks) {
			if (task.getUuid().toString().equals(uuid)) {
				return task;
			}
		}

		return null;
	}

	private List<Task> getTasksAsList() {
		ArrayList<Task> tasks = new ArrayList<Task>();
		for (Task task : this.tasks) {
			tasks.add(task);
		}
		return tasks;
	}

	private void putOmniBarCaretAtEnd() {
		Platform.runLater(new Runnable() {

			public void run() {
				omniBar.end();
			}

		});
	}

	private void removeCard(String uuid) {
		if (agendaViewLoaded) {
			agendaView.getEngine().executeScript("removeCard('" + uuid + "');");
		}
	}
	
	private void showToast(String msg) {
		if (agendaViewLoaded) {
			msg = msg.replaceAll("'", "\'");
			agendaView.getEngine().executeScript("showToast('" + msg + "');");
		}
	}

	private void updateViewModeText() {
		if (viewMode == ViewMode.ALL) {
			viewModeText.setText("All Tasks");
		}

	}

	public class Bridge {
		public void autofillOmniBarWithEditDate(String uuid) {
			Task task = getTaskByUuid(uuid);
			if (task.getStartDate() == null && task.getEndDate() == null) {
				return;
			}
			cycleHandleHistory();

			DateTimeFormatter fmt = DateTimeFormat
					.forPattern("dd MMM YYYY hh:mma");

			String date = fmt.print(task.getEndDate());
			if (task.getStartDate() != null) {
				date = fmt.print(task.getStartDate()) + " to " + date;
			} else {
				date = "due " + date;
			}
			String command = CommandEditImpl.COMMAND_ALIASES_EDIT[0] + " "
					+ task.getId() + " ";
			omniBar.setText(command + date);
			focusOmniBar();
			omniBar.selectRange(command.length(),
					command.length() + date.length());
		}

		public void autofillOmniBarWithEditId(String uuid) {
			Task task = getTaskByUuid(uuid);
			cycleHandleHistory();
			omniBar.setText(CommandEditImpl.COMMAND_ALIASES_EDIT[0] + " "
					+ task.getId() + " ");
			focusOmniBar();
			omniBar.selectRange(0, CommandEditImpl.COMMAND_ALIASES_EDIT[0].length());
		}

		public void autofillOmniBarWithEditName(String uuid) {
			Task task = getTaskByUuid(uuid);
			cycleHandleHistory();
			String command = CommandEditImpl.COMMAND_ALIASES_EDIT[0] + " "
					+ task.getId() + " ";
			omniBar.setText(command + task.getName());
			focusOmniBar();
			omniBar.selectRange(command.length(), command.length()
					+ task.getName().length());
		}

		public void autofillOmniBarWithEditPriority(String uuid) {
			Task task = getTaskByUuid(uuid);
			cycleHandleHistory();
			String priority = ParserMainImpl.PRIORITY_INDICATORS[task
					.getPriority().ordinal()];
			String command = CommandEditImpl.COMMAND_ALIASES_EDIT[0] + " "
					+ task.getId() + " ";
			omniBar.setText(command + priority);
			focusOmniBar();
			omniBar.selectRange(command.length() + 1, command.length() + 1
					+ priority.length());
		}
		
		public int compareDates(long startDate1, long endDate1, long startDate2, long endDate2) {
			Task task1 = new Task();
			task1.setPriority(Priority.NONE);
			task1.setId(0);
			
			Task task2 = task1.clone();
			
			if (startDate1 != -1) {
				task1.setStartDate(new DateTime().withMillis(startDate1));
			}
			task1.setEndDate(new DateTime().withMillis(endDate1));
			
			if (startDate2 != -1) {
				task2.setStartDate(new DateTime().withMillis(startDate2));
			}
			task2.setEndDate(new DateTime().withMillis(endDate2));
			
			return Task.taskSorterComparator.compare(task1, task2);
		}

		public void debug(String msg) {
			System.out.println(msg);
		}

		public void focusOmniBar() {
			MainViewController.this.focusOmniBar();
		}

		public String getSectionHeader(int type) {
			if (type == 0) {
				return SECTION_OVERDUE;
			} else {
				return SECTION_FLOATING;
			}
		}
		
		public Task getTask(int index) {
			return tasks.get(index);
		}

		public List<Task> getTasks() {
			if (viewMode == ViewMode.ALL) {
				return getTasksAsList();
			} else {
				return getAlternateTasksAsList();
			}
		}
		
		public int getViewMode() {
			return viewMode.ordinal();
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

		public void redraw() {
			agendaView.requestLayout();
			ui.redraw();
		}
		
		public void showCategory(String category) {
			ui.invokeCommandReceivedListener(CommandDisplayImpl.COMMAND_ALIASES_DISPLAY[0] + " " + category);
			omniBar.clear();
		}
		
		public void showError(String msg) {
			MainViewController.this.showError(msg);
		}
		
		public void showMessage(String msg) {
			MainViewController.this.showMessage(msg);
		}
	}

}
