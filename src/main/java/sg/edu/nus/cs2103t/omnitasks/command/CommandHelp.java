package sg.edu.nus.cs2103t.omnitasks.command;

import java.io.IOException;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput.CommandType;
import sg.edu.nus.cs2103t.omnitask.ui.Ui;





/**This class contains the logic required for help command. 
 * keying in "help" will retrieve overview of all available comamnds 
 * If the user key in "help <Comamnd Name>" and if the command name user keyed in is valid this class 
 * will fetch the specific command. 
 * <p>
 *
 */
//@author A0119643A
public class CommandHelp extends Command {

	public static String[] COMMAND_ALIASES = new String[] { "help",
			"manual" };

	public CommandHelp(CommandInput commandInput) {
		super(commandInput);
		// TODO Auto-generated constructor stub
	}

	// TODO: This class shouldn't be calling UI directly
	@Override
	public boolean processCommand(Data data, Ui ui) {
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

				try {
					commandDescription = data.getHelpDescriptors(
							specificCommandName, false);
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
				commandDescription = data.getHelpDescriptors("OVERVIEW", false);
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
