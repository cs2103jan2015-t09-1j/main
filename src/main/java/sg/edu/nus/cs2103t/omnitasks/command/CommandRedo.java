package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.ui.Ui;

/**This class contains the logic for Redo command. When Redo command is triggered, it will call data class to carry out the manipulation of the Tasks List state. 
 * <p>
 */
//@author A0119742A
public class CommandRedo extends Command {

	public static String[] COMMAND_ALIASES = new String[] { "redo" };

	public CommandRedo(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, Ui ui) {
		if (data.redo()) {
			ui.showMessage("Redo completed!");
			return true;
		} else {
			ui.showMessage("You have no Redo entries");
			return false;
		}
	}
}