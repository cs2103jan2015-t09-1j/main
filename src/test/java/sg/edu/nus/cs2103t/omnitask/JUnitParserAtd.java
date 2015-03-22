package sg.edu.nus.cs2103t.omnitask;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput.CommandType;
import sg.edu.nus.cs2103t.omnitask.parser.Parser;
import sg.edu.nus.cs2103t.omnitask.parser.ParserMainImpl;

public class JUnitParserAtd {
	@Test
	public void TestParseUserInput() {
		CommandInput commandInput = null;
		
		// Test parsing of "add" and "task name" parameter
		commandInput = parseUserInputHelper("add Hello");
		assertNotNull(commandInput);
		assertEquals(commandInput.getCommandType(), CommandType.ADD);
		assertEquals(commandInput.getName(), "Hello");
		
		// Test parsing of "add" and "task name", "due" parameter
		commandInput = parseUserInputHelper("add Hello due 15 August 2015");
		assertNotNull(commandInput);
		assertEquals(commandInput.getCommandType(), CommandType.ADD);
		assertEquals(commandInput.getName(), "Hello");
		assertEquals(commandInput.getEndDate().dayOfMonth().get(), 15);
		assertEquals(commandInput.getEndDate().monthOfYear().get(), 8);
		assertEquals(commandInput.getEndDate().year().get(), 2015);
		
		// Test parsing of "add" and "task name", "due" (relative date) parameter
		commandInput = parseUserInputHelper("add Hello due tomorrow");
		assertNotNull(commandInput);
		assertEquals(commandInput.getCommandType(), CommandType.ADD);
		assertEquals(commandInput.getName(), "Hello");
		assertEquals(commandInput.getEndDate().dayOfYear().get(), DateTime.now().plusDays(1).dayOfYear().get());
	}
	
	private CommandInput parseUserInputHelper(String input) {
		Parser parser = new ParserMainImpl();
		return parser.parseUserInput(input);
	}
}
