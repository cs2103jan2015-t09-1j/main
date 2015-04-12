package sg.edu.nus.cs2103t.omnitask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import jdk.nashorn.internal.parser.AbstractParser;

import org.joda.time.DateTime;
import org.junit.Test;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.data.StorageBackedData;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput.CommandType;
import sg.edu.nus.cs2103t.omnitask.item.Task;
import sg.edu.nus.cs2103t.omnitask.item.Task.Priority;
import sg.edu.nus.cs2103t.omnitask.parser.Parser;
import sg.edu.nus.cs2103t.omnitask.storage.JsonStorage;
import sg.edu.nus.cs2103t.omnitask.ui.StubUi;
import sg.edu.nus.cs2103t.omnitask.ui.Ui;
import sg.edu.nus.cs2103t.omnitasks.command.Command;

//@author A0111795A
public class JUnitSystemTest {
	
	// Test Whole System And All Known Commands
	@Test
	public void TestSystem() throws IOException {
		// Initialize stub ui
		Ui ui = new StubUi();
		
		// Initialize other components
		Parser parser = new Parser();
		File file = new File("SystemTestingStorage.txt");
		file.delete();
		file.deleteOnExit();
		Data data = StorageBackedData.GetSingleton().init(new JsonStorage(file));

		// General Test Flow:
		// 1. Check if user input is parsed properly to CommandInput
		// 2. Check if command succeed
		// 3. Check if Data committed the changes proper
		// NOTE: Due to using a sortedlist in Data, do note the index of items when retrieving, it won't be in order of it was added
		
		// Test Add Task
		processInput(ui, parser, data, "add Hello World", CommandType.ADD, 0, "Hello World", null, null, Priority.NONE);
		assertTaskAttributes(data.getTasks().get(0), 1, "Hello World", null, null, Priority.NONE);
		
		// Test Add Task with due date
		processInput(ui, parser, data, "add Hello World due tomorrow", CommandType.ADD, 0, "Hello World", null, new DateTime().withMillisOfDay(0).plusDays(1), Priority.NONE);
		// ...the order in the data.getTasks() has changed due to sorting and hence item 0 is the item which we just added
		assertTaskAttributes(data.getTasks().get(0), 1, "Hello World", null, new DateTime().withMillisOfDay(0).plusDays(1), Priority.NONE);
		
		// Test Delete Task
		processInput(ui, parser, data, "delete 2", CommandType.DELETE, 2, null, null, null, null);
		assertEquals(data.getTasks().size(), 1);
		assertTaskAttributes(data.getTasks().get(0), 1, "Hello World", null, new DateTime().withMillisOfDay(0).plusDays(1), Priority.NONE);
		
		// Test Edit Task
		processInput(ui, parser, data, "edit 1 Hello World Edited", CommandType.EDIT, 1, "Hello World Edited", null, null, null);
		assertTaskAttributes(data.getTasks().get(0), 1, "Hello World Edited", null, new DateTime().withMillisOfDay(0).plusDays(1), Priority.NONE);
		
		// TODO: Test Edit Due Task
		// TODO: Test Add Timed Task
		// TODO: Test Edit Timed Task
		// TODO: Test Add Priority
		// TODO: Test Edit Priority
	}
	
	private void processInput(Ui ui, Parser parser, Data data, String input, CommandType expectedCommandType, long expectedId, String expectedTaskName, DateTime expectedStartDate, DateTime expectedEndDate, Priority expectedPriority) {
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
