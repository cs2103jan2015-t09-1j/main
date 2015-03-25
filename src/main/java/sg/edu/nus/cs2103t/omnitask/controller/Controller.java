package sg.edu.nus.cs2103t.omnitask.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.Main;
import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.logic.DataImpl;
import sg.edu.nus.cs2103t.omnitask.parser.Parser;
import sg.edu.nus.cs2103t.omnitask.parser.ParserMainImpl;
import sg.edu.nus.cs2103t.omnitasks.command.Command;
import sg.edu.nus.cs2103t.omnitasks.command.Command.CommandResultListener;

public class Controller {
	private static Controller controller;

	protected Parser parser;

	protected Data data;
	
	protected ArrayList<OnMessageListener> onMessageListeners;
	
	public static interface OnMessageListener {
		void onResultMessage(String msg);
		
		void onErrorMessage(String msg);
	}
	
	private Controller() {
		onMessageListeners = new ArrayList<OnMessageListener>();
	}
	
	public static Controller GetSingleton() {
		if (controller == null) controller = new Controller();
		
		return controller;
	}

	public void addOnMessageListener(OnMessageListener listener) {
		onMessageListeners.add(listener);
	}
	
	public void removeOnMessageListener(OnMessageListener listener) {
		onMessageListeners.remove(listener);
	}
	
	private void showResult(String msg) {
		for (OnMessageListener listener : onMessageListeners) {
			listener.onResultMessage(msg);
		}
	}
	
	private void showError(String msg) {
		for (OnMessageListener listener : onMessageListeners) {
			listener.onErrorMessage(msg);
		}
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
			System.err.println("No permission to access file.");
			Main.Exit();
			return;
		}
	}
	
	public void processUserInput(String input) {
		Command command = parser.parseUserInput(input);

		if (command == null) {
			showError("Invalid command entered. Please try again.");
		} else {
			command.processCommand(data, new CommandResultListener() {

				public void onSuccess(String msg) {
					showResult(msg);
				}

				public void onFailure(String msg) {
					showError(msg);
				}
				
			});
		}
	}

}
