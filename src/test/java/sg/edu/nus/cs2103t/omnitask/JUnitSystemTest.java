package sg.edu.nus.cs2103t.omnitask;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.joda.time.DateTime;
import org.junit.Test;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.logic.DataImpl;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput.CommandType;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.model.Task.Priority;
import sg.edu.nus.cs2103t.omnitask.parser.Parser;
import sg.edu.nus.cs2103t.omnitask.parser.ParserMainImpl;
import sg.edu.nus.cs2103t.omnitask.storage.IOJSONImpl;
import sg.edu.nus.cs2103t.omnitask.ui.UI;
import sg.edu.nus.cs2103t.omnitask.ui.UIStubImpl;
import sg.edu.nus.cs2103t.omnitasks.command.Command;

public class JUnitSystemTest {
	
	// Test Whole System And All Known Commands
	@Test
	public void TestSystem() throws IOException {
		// Initialize stub ui
		UI ui = new UIStubImpl();
		
		// Initialize other components
		Parser parser = new ParserMainImpl();
		File file = new File("SystemTestingStorage.txt");
		file.delete();
		file.deleteOnExit();
		Data data = DataImpl.GetSingleton().init(new IOJSONImpl(file));

		// General Test Flow:
		// 1. Check if user input is parsed properly to CommandInput
		// 2. Check if command succeed
		// 3. Check if Data committed the changes proper
		
		// Test Add Task
		processInput(ui, parser, data, "add Hello World", CommandType.ADD, 0, "Hello World", null, null, Priority.NONE);
		assertTaskAttributes(data.getTasks().get(0), 1, "Hello World", null, null, Priority.NONE);
		
		// Test Add Task with due date
		processInput(ui, parser, data, "add Hello World Tmr due tomorrow", CommandType.ADD, 0, "Hello World Tmr", null, new DateTime().withMillisOfDay(0).plusDays(1), Priority.NONE);
		assertTaskAttributes(data.getTasks().get(1), 2, "Hello World Tmr", null, new DateTime().withMillisOfDay(0).plusDays(1), Priority.NONE);
		
		// Test Delete Task
		processInput(ui, parser, data, "delete 1", CommandType.DELETE, 1, null, null, null, Priority.NONE);
		assertEquals(data.getTasks().size(), 1);
		assertTaskAttributes(data.getTasks().get(0), 1, "Hello World Tmr", null, new DateTime().withMillisOfDay(0).plusDays(1), Priority.NONE);
		
		// Test Edit Task
		processInput(ui, parser, data, "edit 1 Hello World Edited", CommandType.EDIT, 1, "Hello World Edited", null, null, Priority.NONE);
		assertTaskAttributes(data.getTasks().get(0), 1, "Hello World Edited", null, new DateTime().withMillisOfDay(0).plusDays(1), Priority.NONE);
		
		// TODO: Test Edit Due Task
		// TODO: Test Add Timed Task
		// TODO: Test Edit Timed Task
		// TODO: Test Add Priority
		// TODO: Test Edit Priority
	}
	
	private void processInput(UI ui, Parser parser, Data data, String input, CommandType expectedCommandType, long expectedId, String expectedTaskName, DateTime expectedStartDate, DateTime expectedEndDate, Priority expectedPriority) {
		Command command = parser.parseUserInput(input);

		assertNotNull(command);
		assertEquals(expectedCommandType, command.getCommandInput().getCommandType());
		assertEquals(expectedId, command.getCommandInput().getId());
		assertEquals(expectedTaskName, command.getCommandInput().getName());
		assertEquals(expectedStartDate, command.getCommandInput().getStartDate());
		assertEquals(expectedEndDate, command.getCommandInput().getEndDate());
		assertEquals(expectedPriority, command.getCommandInput().getPriority());
		
		boolean success = command.processCommand(data, ui);
		assertTrue(success);
	}
	
	private void assertTaskAttributes(Task task, long expectedId, String expectedTaskName, DateTime expectedStartDate, DateTime expectedEndDate, Priority expectedPriority) {
		assertEquals(expectedId, task.getId());
		assertEquals(expectedTaskName, task.getName());
		assertEquals(expectedStartDate, task.getStartDate());
		assertEquals(expectedEndDate, task.getEndDate());
		assertEquals(expectedPriority, task.getPriority());
	}
}
