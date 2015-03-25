package sg.edu.nus.cs2103t.omnitask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.joda.time.DateTime;
import org.junit.Test;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput.CommandType;
import sg.edu.nus.cs2103t.omnitask.parser.Parser;
import sg.edu.nus.cs2103t.omnitask.parser.ParserMainImpl;
import sg.edu.nus.cs2103t.omnitasks.command.Command;

public class JUnitParserAtd {
	@Test
	public void TestParseUserInput() {
		Command command = null;
		
		// Test parsing of "add" and "task name" parameter
		command = parseUserInputHelper("add Hello");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.ADD);
		assertEquals(command.getCommandInput().getName(), "Hello");
		
		//Test parsing of "new" and "task name"
		command = parseUserInputHelper("new Hello");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.ADD);
		assertEquals(command.getCommandInput().getName(), "Hello");
		
		
		// Test parsing of "add" and "task name", "due" parameter
		command = parseUserInputHelper("add Hello due 15 August 2015");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.ADD);
		assertEquals(command.getCommandInput().getName(), "Hello");
		assertEquals(command.getCommandInput().getEndDate().dayOfMonth().get(), 15);
		assertEquals(command.getCommandInput().getEndDate().monthOfYear().get(), 8);
		assertEquals(command.getCommandInput().getEndDate().year().get(), 2015);
		
		//Test parsing of "add" and "task name", "by" parameter
		command = parseUserInputHelper("add Hello by 15 August 2015");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.ADD);
		assertEquals(command.getCommandInput().getName(), "Hello");
		assertEquals(command.getCommandInput().getEndDate().dayOfMonth().get(), 15);
		assertEquals(command.getCommandInput().getEndDate().monthOfYear().get(), 8);
		assertEquals(command.getCommandInput().getEndDate().year().get(), 2015);
		
		//Test parsing of "add", "task name" and "on" parameter
		command = parseUserInputHelper("add Hello on 15 August 2015");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.ADD);
		assertEquals(command.getCommandInput().getName(), "Hello");
		assertEquals(command.getCommandInput().getEndDate().dayOfMonth().get(), 15);
		assertEquals(command.getCommandInput().getEndDate().monthOfYear().get(), 8);
		assertEquals(command.getCommandInput().getEndDate().year().get(), 2015);
		
		// Test parsing of "add" and "task name", "due" (relative date) parameter
		command = parseUserInputHelper("add Hello due tomorrow");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.ADD);
		assertEquals(command.getCommandInput().getName(), "Hello");
		assertEquals(command.getCommandInput().getEndDate().dayOfYear().get(), DateTime.now().plusDays(1).dayOfYear().get());
	
	    //Test parsing of "delete" and "task index" parameter
		command = parseUserInputHelper("delete 1");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.DELETE);
		assertEquals(command.getCommandInput().getId(), 1);
		
		//Test parsing of "display" parameter
		command = parseUserInputHelper("display");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.DISPLAY);
		
		//Test parsing of "edit" and "task index" and "task name" parameter
		command = parseUserInputHelper("edit 1 Hello");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.EDIT);
		assertEquals(command.getCommandInput().getId(), 1);
		assertEquals(command.getCommandInput().getName(), "Hello");
		
		//Test parsing of "edit", "task index" and "due" parameter
		command = parseUserInputHelper("edit 1 due 15 August 2015");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.EDIT);
		assertEquals(command.getCommandInput().getId(), 1);
		assertEquals(command.getCommandInput().getEndDate().dayOfMonth().get(), 15);
		assertEquals(command.getCommandInput().getEndDate().monthOfYear().get(), 8);
		assertEquals(command.getCommandInput().getEndDate().year().get(), 2015);
		
		//Test parsing of "exit" parameter
		command = parseUserInputHelper("exit");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.EXIT);
		
		//Test parsing of "search" and "search content"
		command = parseUserInputHelper("search Hello");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.SEARCH);
		assertEquals(command.getCommandInput().getName(), "Hello");
	}
	
	private Command parseUserInputHelper(String input) {
		Parser parser = new ParserMainImpl();
		return parser.parseUserInput(input);
	}
}
