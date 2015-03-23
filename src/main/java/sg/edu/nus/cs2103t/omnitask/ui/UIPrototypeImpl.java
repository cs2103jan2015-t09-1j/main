package sg.edu.nus.cs2103t.omnitask.ui;

import java.util.ArrayList;
import java.util.Scanner;

import sg.edu.nus.cs2103t.omnitask.controller.Controller;
import sg.edu.nus.cs2103t.omnitask.controller.Controller.OnMessageListener;
import sg.edu.nus.cs2103t.omnitask.logic.Data.DataUpdatedListener;
import sg.edu.nus.cs2103t.omnitask.logic.DataImpl;
import sg.edu.nus.cs2103t.omnitask.model.Task;

public class UIPrototypeImpl implements UI {

	private Scanner input;
	
	private Controller controller;
	
	public UIPrototypeImpl(Controller controller) {
		this.controller = controller;
		
		input = new Scanner(System.in);
	}
	
	private void showError(String msg) {
		System.out.println("Error: " + msg);
	}

	private void showMessage(String msg) {
		System.out.println(msg);
	}
	
	private void printPrompt(String msg) {
		System.out.print(msg + ": ");
	}
	
	private String getUserInput() {
		return input.nextLine();
	}
	
	//@Override
	public void start() {
		// Subscribe to Data changes
		DataImpl.GetSingleton().addDataUpdatedListener(dataUpdatedListener);
		
		// Subscribe to messages by Controller
		controller.addOnMessageListener(onMessageListener);
		
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
	
	//@Override
	public void exit() {
		input.close();
	}
	
	private DataUpdatedListener dataUpdatedListener = new DataUpdatedListener() {

		@Override
		public void dataUpdated(ArrayList<Task> tasks) {
			showMessage("List of tasks: ");
			
			for (Task task : tasks) {
				showMessage(task.getId() + " - " + task.getName());
			}
		}
		
	};

	private OnMessageListener onMessageListener = new OnMessageListener() {

		@Override
		public void onResultMessage(String msg) {
			showMessage(msg);
		}

		@Override
		public void onErrorMessage(String msg) {
			showError(msg);
		}
		
	};
	
}
