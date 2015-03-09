package sg.edu.nus.cs2103t.omnitask.ui;

import java.util.List;
import java.util.Scanner;

import sg.edu.nus.cs2103t.omnitask.controller.Controller;
import sg.edu.nus.cs2103t.omnitask.model.Task;

public class UIPrototypeImpl implements UI {

	private Scanner input;
	
	private Controller controller;
	
	public UIPrototypeImpl(Controller controller) {
		this.controller = controller;
		
		input = new Scanner(System.in);
	}
	
	@Override
	public void showError(String msg) {
		System.out.println("Error: " + msg);
	}

	@Override
	public void showMessage(String msg) {
		System.out.println(msg);
	}
	
	public void printPrompt(String msg) {
		System.out.print(msg + ": ");
	}
	
	public String getUserInput() {
		return input.nextLine();
	}
	
	@Override
	public void start() {
		showMessage("Welcome to OmniTask.");
		
		String userInput = "";
		
		// Infinite loop
		while (true) {
			printPrompt("command");
			
			// Wait for command
			userInput = getUserInput();
			
			// Process user input by passing it to controller
			controller.processUserInput(userInput);
		}
	}
	
	@Override
	public void exit() {
		input.close();
	}

	@Override
	public void updateTaskListings(List<Task> tasks) {
		showMessage("List of tasks: ");
		
		for (Task task : tasks) {
			showMessage(task.getId() + " - " + task.getName());
		}
	}
}
