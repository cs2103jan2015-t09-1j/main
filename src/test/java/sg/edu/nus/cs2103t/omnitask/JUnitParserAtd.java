package sg.edu.nus.cs2103t.omnitask;

import static org.junit.Assert.*;

import org.junit.Test;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.parser.Parser;
import sg.edu.nus.cs2103t.omnitask.parser.ParserMainImpl;

public class JUnitParserAtd {
	
	@Test
	public void TestParseUserInput() {
		CommandInput commandInput = null;
		
		// Test parsing of "add" and "task name" parameter
		commandInput = parseUserInputHelper("add Hello");
		assertNotNull(commandInput);
		assertEquals(commandInput.getCommandName(), CommandInput.COMMAND_ADD);
		assertEquals(commandInput.getName(), "Hello");
	}
	
	private CommandInput parseUserInputHelper(String input) {
		Parser parser = new ParserMainImpl();
		return parser.parseUserInput(input);
	}
}
