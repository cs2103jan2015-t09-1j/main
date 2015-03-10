package sg.edu.nus.cs2103t.omnitask.parser;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;

public class ParserMainImpl extends Parser {

	@Override
	public CommandInput parseUserInput(String input) {
		// TODO: Fix prototype implementation, need to think of the proper way to parse text modularly
		String[] inputSplit = input.split(" ");
		
		if (!inputSplit[0].equals(CommandInput.COMMAND_ADD) && !inputSplit[0].equals(CommandInput.COMMAND_DISPLAY)) {
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
		
		return commandInput;
	}

}
