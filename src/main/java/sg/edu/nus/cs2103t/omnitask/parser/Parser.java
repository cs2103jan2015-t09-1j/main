package sg.edu.nus.cs2103t.omnitask.parser;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;

public abstract class Parser {
	public abstract CommandInput parseUserInput(String input);
}
