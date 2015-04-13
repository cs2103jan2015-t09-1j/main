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
		processInput(ui, parser, data, "add Hello World", CommandType.ADD, 0, "Hello World", null, null, Priority.NONE, false);
		assertTaskAttributes(data.getTasks().get(0), 1, "Hello World", null, null, Priority.NONE, false);
		
		// Test Add Task with due date
		processInput(ui, parser, data, "add Hello World due tomorrow", CommandType.ADD, 0, "Hello World", null, new DateTime().withMillisOfDay(0).plusDays(1), Priority.NONE, false);
		// ...the order in the data.getTasks() has changed due to sorting and hence item 0 is the item which we just added
		assertTaskAttributes(data.getTasks().get(0), 1, "Hello World", null, new DateTime().withMillisOfDay(0).plusDays(1), Priority.NONE, false);
		
		// Test Delete Task
		processInput(ui, parser, data, "delete 2", CommandType.DELETE, 2, null, null, null, null, false);
		assertEquals(data.getTasks().size(), 1);
		assertTaskAttributes(data.getTasks().get(0), 1, "Hello World", null, new DateTime().withMillisOfDay(0).plusDays(1), Priority.NONE, false);
		
		// Test Edit Task
		processInput(ui, parser, data, "edit 1 Hello World Edited", CommandType.EDIT, 1, "Hello World Edited", null, null, null, false);
		assertTaskAttributes(data.getTasks().get(0), 1, "Hello World Edited", null, new DateTime().withMillisOfDay(0).plusDays(1), Priority.NONE, false);
		
		// Test Edit Task Due
		processInput(ui, parser, data, "edit 1 due 5pm", CommandType.EDIT, 1, null, null, new DateTime().withMillisOfDay(0).plusHours(17), null, false);
		assertTaskAttributes(data.getTasks().get(0), 1, "Hello World Edited", null, new DateTime().withMillisOfDay(0).plusHours(17), Priority.NONE, false);
		
		// Test Add Timed Task
		processInput(ui, parser, data, "add Timed Task from today to tomorrow", CommandType.ADD, 0, "Timed Task", new DateTime().withMillisOfDay(0), new DateTime().withMillisOfDay(0).plusDays(1), Priority.NONE, false);
		// ...the order in the data.getTasks() has changed due to sorting and hence item 0 is the item which we just added
		assertTaskAttributes(data.getTasks().get(0), 1, "Timed Task", new DateTime().withMillisOfDay(0), new DateTime().withMillisOfDay(0).plusDays(1), Priority.NONE, false);
		
		// Test Edit Timed Task
		processInput(ui, parser, data, "edit 1 from tomorrow 1pm to tomorrow 3pm", CommandType.EDIT, 1, null, new DateTime().withMillisOfDay(0).plusDays(1).plusHours(13), new DateTime().withMillisOfDay(0).plusDays(1).plusHours(15), null, false);
		// ...the order in the data.getTasks() has changed due to sorting and hence item 1 (id 2) is the item which we just edited
		assertTaskAttributes(data.getTasks().get(1), 2, "Timed Task", new DateTime().withMillisOfDay(0).plusDays(1).plusHours(13), new DateTime().withMillisOfDay(0).plusDays(1).plusHours(15), Priority.NONE, false);
		
		// Test Add with Priority
		processInput(ui, parser, data, "add High Priority Task ^h", CommandType.ADD, 0, "High Priority Task", null, null, Priority.HIGH, false);
		assertTaskAttributes(data.getTasks().get(2), 3, "High Priority Task", null, null, Priority.HIGH, false);
		
		// Test Edit Priority
		processInput(ui, parser, data, "edit 3 Low Priority Task ^l", CommandType.EDIT, 3, "Low Priority Task", null, null, Priority.LOW, false);
		assertTaskAttributes(data.getTasks().get(2), 3, "Low Priority Task", null, null, Priority.LOW, false);
		
		// Test Mark Done
		processInput(ui, parser, data, "mark 3 done", CommandType.MARK, 3, null, null, null, null, true);
		assertTaskAttributes(data.getTasks().get(2), 3, "Low Priority Task", null, null, Priority.LOW, true);
		
		// Test Mark Undone
		processInput(ui, parser, data, "mark 3 undone", CommandType.MARK, 3, null, null, null, null, false);
		assertTaskAttributes(data.getTasks().get(2), 3, "Low Priority Task", null, null, Priority.LOW, false);
		
		// Test remove-date
		processInput(ui, parser, data, "remove-date 2", CommandType.REMOVEDATE, 2, null, null, null, null, false);
		// ...the order in the data.getTasks() has changed due to sorting and hence item 2 (id 3) is the item which we just edited
		assertTaskAttributes(data.getTasks().get(2), 3, "Timed Task", null, null, Priority.NONE, false);
	}
	
	private void processInput(Ui ui, Parser parser, Data data, String input, CommandType expectedCommandType, long expectedId, String expectedTaskName, DateTime expectedStartDate, DateTime expectedEndDate, Priority expectedPriority, boolean expectedIsCompleted) {
		Command command = parser.parseUserInput(input);

		assertNotNull(command);
		assertEquals(expectedCommandType, command.getCommandInput().getCommandType());
		assertEquals(expectedId, command.getCommandInput().getId());
		assertEquals(expectedTaskName, command.getCommandInput().getName() == null || command.getCommandInput().getName().equals("") ? null : command.getCommandInput().getName());
		assertEquals(expectedStartDate, command.getCommandInput().getStartDate());
		assertEquals(expectedEndDate, command.getCommandInput().getEndDate());
		assertEquals(expectedPriority, command.getCommandInput().getPriority());
		assertEquals(expectedIsCompleted, command.getCommandInput().isCompleted());
		
		boolean success = command.processCommand(data, ui);
		assertTrue(success);
	}
	
	private void assertTaskAttributes(Task task, long expectedId, String expectedTaskName, DateTime expectedStartDate, DateTime expectedEndDate, Priority expectedPriority, boolean expectedIsCompleted) {
		assertEquals(expectedId, task.getId());
		assertEquals(expectedTaskName, task.getName());
		assertEquals(expectedStartDate, task.getStartDate());
		assertEquals(expectedEndDate, task.getEndDate());
		assertEquals(expectedPriority, task.getPriority());
		assertEquals(expectedIsCompleted, task.isCompleted());
	}
}
