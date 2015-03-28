package sg.edu.nus.cs2103t.omnitask.ui;

import java.util.ArrayList;
import java.util.Scanner;

import sg.edu.nus.cs2103t.omnitask.logic.Data.DataUpdatedListener;
import sg.edu.nus.cs2103t.omnitask.logic.DataImpl;
import sg.edu.nus.cs2103t.omnitask.model.Task;

public class UIPrototypeImpl extends UI {

	private Scanner input;
	
	public UIPrototypeImpl() {
		input = new Scanner(System.in);
	}
	
	public void showError(String msg) {
		System.out.println("Error: " + msg);
	}

	public void showMessage(String msg) {
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

		showMessage("Welcome to OmniTask.");
		
		String userInput = "";
		
		// Infinite loop
		while (true) {
			printPrompt("command");
			
			// Wait for command
			userInput = getUserInput();
			
			// Process user input by passing it to controller
			invokeCommandReceivedListener(userInput);
		}
	}
	
	//@Override
	public void exit() {
		input.close();
	}
	
    private DataUpdatedListener dataUpdatedListener = new DataUpdatedListener() {

        public void dataUpdated(ArrayList<Task> tasks) {
            showMessage("List of tasks: ");
            
            for (Task task : tasks) {
                showMessage(task.getId() + " - " + task.getName());
            }
        }
        
    };

	@Override
	public void showHelp(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showSearchResults(ArrayList<Task> tasks) {
		// TODO Auto-generated method stub
		
	}

	
}
