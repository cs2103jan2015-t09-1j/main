package sg.edu.nus.cs2103t.omnitask.controller;

import java.io.File;
import java.io.IOException;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.logic.DataImpl;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.parser.Parser;
import sg.edu.nus.cs2103t.omnitask.parser.ParserMainImpl;
import sg.edu.nus.cs2103t.omnitask.storage.IO;
import sg.edu.nus.cs2103t.omnitask.ui.UI;
import sg.edu.nus.cs2103t.omnitask.ui.UIMainImpl;

public class ControllerMainImpl extends Controller {

	protected UI ui;

	protected Parser parser;

	protected Data data;

	@Override
	public void start(String[] args) {
		// Initialize components
		ui = new UIMainImpl(this);
		parser = new ParserMainImpl();

		// Get file from argument
		File storageFile = checkForAndInitArgument(args);

		// Check if we have the filename from the argument, quit if not
		if (storageFile == null) {
			ui.exit();
			return;
		}

		// Initialize data logic (which would create the storage file if needed)
		// Exit application if fails
		try {
			data = new DataImpl(storageFile);
		} catch (IOException ex) {
			ui.showError("No permission to access file.");
			ui.exit();
			return;
		}

		// Pass control to UI to receive user input
		ui.start();
	}

	@Override
	public void processUserInput(String input) {
		CommandInput commandInput = parser.parseUserInput(input);

		if (commandInput == null) {
			ui.showError("Invalid command entered. Please try again.");
		} else {
			processCommand(commandInput);
		}
	}

	@Override
	public void processCommand(CommandInput commandInput) {
		// TODO: switch only support constants, maybe bad idea to use it here as
		// it cause magic string
		switch (commandInput.getCommandName()) {
		case "add":
			processAddCommand(commandInput);
			break;

		case "display":
			processDisplayCommand(commandInput);
			break;

		case "delete":
			processDeleteCommand(commandInput);
			break;

		case "edit":
			processEditCommand(commandInput);
			break;
		
		case "exit":
			ui.exit();
			break;

		default:
			new Exception("Not implemented").printStackTrace();
		}
	}

	@Override
	public void processAddCommand(CommandInput commandInput) {
		Task task = data.addTask(commandInput);

		// TODO: Fix magic string
		if (task != null) {
			ui.showMessage("Task \"" + task.getName()
					+ "\" added successfully!");
		} else {
			ui.showMessage("Failed to add task \"" + commandInput.getName()
					+ "\".");
		}
		updateTaskListings();
	}

	@Override
	public void processDisplayCommand(CommandInput commandInput) {
		updateTaskListings();
	}

	@Override
	public void processDeleteCommand(CommandInput commandInput) {

		if (data.deleteTask(commandInput)) {
			ui.showMessage("Task \"" + commandInput.getId()
					+ "\" deleted successfully!");
		} else {
			ui.showMessage("Unable to delete Task \"" + commandInput.getId()
					+ "\". Please choose a valid id!");
		}

		updateTaskListings();
	}

	@Override
	public void processEditCommand(CommandInput commandInput) {

		if (data.editTask(commandInput)) {
			ui.showMessage("Task \"" + commandInput.getId()
					+ "\" updated successfully!");
		} else {
			ui.showMessage("Unable to update Task \"" + commandInput.getId()
					+ "\". Please choose a valid id!");
		}

		updateTaskListings();
	}

	// Update UI
	private void updateTaskListings() {
		ui.updateTaskListings(data.getTasks());
	}

	private File checkForAndInitArgument(String[] args) {
		// Print error if argument is not given
		if (args.length == 0) {
			ui.showError("Please specify a text file as an argument.");
			return null;
		}

		return new File(args[0]);
	}

}
