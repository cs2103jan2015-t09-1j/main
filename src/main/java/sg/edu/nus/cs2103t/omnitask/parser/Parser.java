package sg.edu.nus.cs2103t.omnitask.parser;

import sg.edu.nus.cs2103t.omnitasks.command.Command;

public abstract class Parser {
	public abstract Command parseUserInput(String input);
}
