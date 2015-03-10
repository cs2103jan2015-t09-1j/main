package sg.edu.nus.cs2103t.omnitask.parser;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;

public class ParserMainImpl extends Parser {

	@Override
	public CommandInput parseUserInput(String input) {
		// TODO: Fix prototype implementation, need to think of the proper way
		// to parse text modularly
		String[] inputSplit = input.split(" ");

		// need to add !inputSplit[0].equals(Commands.COMMAND_DELETE) for
		// subsequent commands to validate if user input a valid command
		if (!inputSplit[0].equals(CommandInput.COMMAND_ADD)
				&& !inputSplit[0].equals(CommandInput.COMMAND_DISPLAY)
				&& !inputSplit[0].equals(CommandInput.COMMAND_DELETE)
				&& !inputSplit[0].equals(CommandInput.COMMAND_EDIT)) {
			return null;
		}

		CommandInput commandInput = new CommandInput(inputSplit[0]);
		if (inputSplit[0].equals(CommandInput.COMMAND_ADD)) {
			String name = "";
			for (int i = 1; i < inputSplit.length; i++) {
				name += inputSplit[i] + " ";
			}
			commandInput.setName(name.trim());
		}

		// this is to parse command specific to delete

		if (inputSplit[0].equals(CommandInput.COMMAND_DELETE)) {
			long deleteId;
			deleteId = Long.parseLong(inputSplit[1]);
			commandInput.setId(deleteId);
		}

		// parse for update command

		if (inputSplit[0].equals(CommandInput.COMMAND_EDIT)) {
			long updateId;
			updateId = Long.parseLong(inputSplit[1]);
			commandInput.setId(updateId);
			String name = "";
			for (int i = 2; i < inputSplit.length; i++) {
				name += inputSplit[i] + " ";
			}
			commandInput.setName(name.trim());
		}

		return commandInput;
	}

}
