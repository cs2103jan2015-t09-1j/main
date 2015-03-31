package sg.edu.nus.cs2103t.omnitasks.command;

import java.io.IOException;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput.CommandType;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandHelpImpl extends Command {

	public static String[] COMMAND_ALIASES_HELP = new String[] { "help",
			"manual" };

	public CommandHelpImpl(CommandInput commandInput) {
		super(commandInput);
		// TODO Auto-generated constructor stub
	}

	// TODO: This class shouldn't be calling UI directly
	@Override
	public boolean processCommand(Data data, UI ui) {
		String commandDescription = "";
		String specificCommandName = "";
		if (commandInput.getName() != null) {

			specificCommandName = commandInput.getName().toUpperCase();
			// if the specific commands user entered exist in the list of
			// available commands display the detail each specific commands
			boolean exist = false;
			for (CommandType commandType : CommandType.values()) {
				if (specificCommandName.equals(commandType.toString())) {
					exist = true;
					break;
				}
			}

			if (exist) {

				/*
				 * switch (specificCommandName) { case "ADD": commandDescription
				 * = "add description"; break; case "DISPLAY":
				 * commandDescription = "display description"; break; case
				 * "DELETE": commandDescription = "delete description"; break;
				 * case "EDIT": commandDescription = "edit description"; break;
				 * case "SEARCH": commandDescription = "search description";
				 * break; case "EXIT": commandDescription = "Exits the program";
				 * break; }
				 */
				try {
					commandDescription = data.getHelpDescriptors(specificCommandName);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ui.showHelp(commandDescription);
			} else {
				ui.showError("the specific command you entered does not exist in this system!");
			}

		} else {
			// general help description
			try {
				commandDescription = data.getHelpDescriptors("OVERVIEW");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ui.showHelp(commandDescription);
		}

		// returning false to not execute update listing
		return false;
	}

}
