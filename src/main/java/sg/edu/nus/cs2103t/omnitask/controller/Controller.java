package sg.edu.nus.cs2103t.omnitask.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.logic.DataImpl;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.parser.Parser;
import sg.edu.nus.cs2103t.omnitask.parser.ParserMainImpl;
import sg.edu.nus.cs2103t.omnitask.ui.UI;
import sg.edu.nus.cs2103t.omnitasks.command.Command;

public class Controller {
	private static Controller controller;

	protected UI ui;

	protected Parser parser;

	protected Data data;
	
	private Controller() {
		
	}
	
	public static Controller GetSingleton() {
		if (controller == null) controller = new Controller();
		
		return controller;
	}

	public void setUi(UI ui) {
		this.ui = ui;
	}

	public void start(String[] args) {
		// Initialize components
		parser = new ParserMainImpl();

		// Get file from argument
		File storageFile = null;
		
		// Use default filename if no argument specified
		if (args.length == 0) {
			storageFile = new File("storage.txt");
		} else {
			storageFile = new File(args[0]);
		}

		// Initialize data logic (which would create the storage file if needed)
		// Exit application if fails
		try {
			data = DataImpl.GetSingleton().init(storageFile);
		} catch (IOException ex) {
			ui.showError("No permission to access file.");
			ui.exit();
			return;
		}

		// Pass control to UI to receive user input
		ui.start();
	}
	
	public void processUserInput(String input) {
		Command command = parser.parseUserInput(input);

		if (command == null) {
			ui.showError("Invalid command entered. Please try again.");
		} else {
			// if command is successful, update ui
			if (command.processCommand(ui, data)) {
				updateTaskListings();
			}
		}
	}
	
	// TODO: Controller should not be calling UI, UI should subsribed to data changes in Controller
	private void updateTaskListings() {
		ui.updateTaskListings(data.getTasks());
	}
	
	private void updateSearchTaskListing(ArrayList<Task> searchResult){
		ui.updateTaskListings(searchResult);
	}

	// TODO: Migrate to new architecture
	/*private void processAddCommand(CommandInput commandInput) {
		if (CommandAddImpl.GetSingleton().processCommand(ui, data, commandInput)) {
			updateTaskListings();
		}
	}

	private void processDisplayCommand(CommandInput commandInput) {
		updateTaskListings();
	}

	private void processDeleteCommand(CommandInput commandInput) {

		if (data.deleteTask(commandInput)) {
			ui.showMessage("Task \"" + commandInput.getId()
					+ "\" deleted successfully!");
		} else {
			ui.showMessage("Unable to delete Task \"" + commandInput.getId()
					+ "\". Please choose a valid id!");
		}

		updateTaskListings();
	}

	private void processEditCommand(CommandInput commandInput) {

		if (data.editTask(commandInput)) {
			ui.showMessage("Task \"" + commandInput.getId()
					+ "\" updated successfully!");
		} else {
			ui.showMessage("Unable to update Task \"" + commandInput.getId()
					+ "\". Please choose a valid id!");
		}

		updateTaskListings();
	}

	private void processSearchCommand(CommandInput commandInput) {
		
		ArrayList<Task> searchTaskResult = data.searchTask(commandInput);

		if(searchTaskResult.size()!=0){
			updateSearchTaskListing(searchTaskResult);
		}else{
			if(commandInput.getName().isEmpty()){
				ui.showMessage("Search failed. Please key in the search key!");
			}else{
				ui.showMessage("Search did not find any matching task content.");
			}
		}
		
	}*/

}
