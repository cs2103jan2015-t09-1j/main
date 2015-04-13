package sg.edu.nus.cs2103t.omnitask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.joda.time.DateTime;
import org.junit.Test;

import sg.edu.nus.cs2103t.omnitask.item.CommandInput.CommandType;
import sg.edu.nus.cs2103t.omnitask.item.Task.Priority;
import sg.edu.nus.cs2103t.omnitask.parser.Parser;
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
		
		//Test parsing priority
		command = parseUserInputHelper("new Hello ^h");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.ADD);
		assertEquals(command.getCommandInput().getName(), "Hello");
		assertEquals(command.getCommandInput().getPriority(), Priority.HIGH);
		
		//Test parsing priority with dates
		command = parseUserInputHelper("add Hello ^l due 31 August 2015");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.ADD);
		assertEquals(command.getCommandInput().getName(), "Hello");
		assertEquals(command.getCommandInput().getEndDate().dayOfMonth().get(), 31);
		assertEquals(command.getCommandInput().getEndDate().monthOfYear().get(), 8);
		assertEquals(command.getCommandInput().getEndDate().year().get(), 2015);
		assertEquals(command.getCommandInput().getPriority(), Priority.LOW);
		
		// Test parsing of "add" and "task name", "due" parameter
		//This is also a test combining multiple inputs
		//commandType indicator + task name + dateTime indicator + dateTime
		command = parseUserInputHelper("add Hello due 31 August 2015");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.ADD);
		assertEquals(command.getCommandInput().getName(), "Hello");
		assertEquals(command.getCommandInput().getEndDate().dayOfMonth().get(), 31);
		assertEquals(command.getCommandInput().getEndDate().monthOfYear().get(), 8);
		assertEquals(command.getCommandInput().getEndDate().year().get(), 2015);
		
		//Test parsing of "add" and "task name", "by" parameter
		//day of month is expected to be 1-31
		//32 is a boundary value for testing
		command = parseUserInputHelper("add Hello by 29 August 2015");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.ADD);
		assertEquals(command.getCommandInput().getName(), "Hello");
		assertEquals(command.getCommandInput().getEndDate().dayOfMonth().get(), 29);
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
		
		// Test parsing of "add" and "task name", "from" (but not a date) and "on" parameter
		command = parseUserInputHelper("add Get something from Joe on 15 August 2015");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.ADD);
		assertEquals(command.getCommandInput().getName(), "Get something from Joe");
		assertEquals(command.getCommandInput().getEndDate().dayOfMonth().get(), 15);
		assertEquals(command.getCommandInput().getEndDate().monthOfYear().get(), 8);
		assertEquals(command.getCommandInput().getEndDate().year().get(), 2015);
		
		// Test parsing of "add" and "task name", with quotes (") to escape parsing of date
		command = parseUserInputHelper("add \"Get something from Joe on 15 August 2015\"");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.ADD);
		assertEquals(command.getCommandInput().getName(), "Get something from Joe on 15 August 2015");
		assertNull(command.getCommandInput().getEndDate());
		
		// Variation of above
		command = parseUserInputHelper("add Get something \"from Joe on 15 August 2015\"");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.ADD);
		assertEquals(command.getCommandInput().getName(), "Get something from Joe on 15 August 2015");
		assertNull(command.getCommandInput().getEndDate());
		
		// Another variation of above
		command = parseUserInputHelper("add Get something \"from Joe\" on 15 August 2015");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.ADD);
		assertEquals(command.getCommandInput().getName(), "Get something from Joe");
		assertEquals(command.getCommandInput().getEndDate().dayOfMonth().get(), 15);
		assertEquals(command.getCommandInput().getEndDate().monthOfYear().get(), 8);
		assertEquals(command.getCommandInput().getEndDate().year().get(), 2015);
		
		// Another variation of above
		command = parseUserInputHelper("add Get something from Joe on \"15 August 2015\"");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.ADD);
		assertEquals(command.getCommandInput().getName(), "Get something from Joe on 15 August 2015");
		assertNull(command.getCommandInput().getEndDate());
		
		// Test parsing of "add" and "task name", with quotes (") to escape parsing of date in between keyword in task name and actual date
		command = parseUserInputHelper("add Get something from \"Joe\" on 15 August 2015");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.ADD);
		assertEquals(command.getCommandInput().getName(), "Get something from Joe");
		assertEquals(command.getCommandInput().getEndDate().dayOfMonth().get(), 15);
		assertEquals(command.getCommandInput().getEndDate().monthOfYear().get(), 8);
		assertEquals(command.getCommandInput().getEndDate().year().get(), 2015);
	
	    //Test parsing of "delete" and "task index" parameter
		command = parseUserInputHelper("delete 1");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.DELETE);
		assertEquals(command.getCommandInput().getId(), 1);
		
		//Test parsing of "display" parameter
		command = parseUserInputHelper("display");
		assertNotNull(command);
		assertEquals(command.getCommandInput().getCommandType(), CommandType.SHOW);
		
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
		//Equivalent partition [add],[delete],[display],[edit],[exit] and [search]
	}
	
	private Command parseUserInputHelper(String input) {
		Parser parser = new Parser();
		return parser.parseUserInput(input);
	}
}
