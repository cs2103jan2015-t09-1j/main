package sg.edu.nus.cs2103t.omnitask;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import sg.edu.nus.cs2103t.omnitask.data.DataStubImpl;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput.CommandType;
import sg.edu.nus.cs2103t.omnitask.ui.UI;
import sg.edu.nus.cs2103t.omnitask.ui.UIStubImpl;
import sg.edu.nus.cs2103t.omnitasks.command.Command;
import sg.edu.nus.cs2103t.omnitasks.command.CommandAddImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandEditImpl;

public class JUnitCommandsAtd {
	
	// Test add command, make sure it is added successfully
	@Test
	public void TestCommandAdd() {
		UI ui = new UIStubImpl();
		DataStubImpl data = initData();
		addOneTaskShouldPass(data, ui, "Hello Add");
	}
	
	// Test add command, make sure empty string will not add successfully
	@Test
	public void TestCommandAddFail1() {
		UI ui = new UIStubImpl();
		DataStubImpl data = initData();
		addOneTaskShouldFail(data, ui, "");
	}
	
	// Test add command, make sure empty (untrimmed) string will not add successfully
	@Test
	public void TestCommandAddFail2() {
		UI ui = new UIStubImpl();
		DataStubImpl data = initData();
		addOneTaskShouldFail(data, ui, " ");
	}
	
	// Test edit command, make sure edited tasks are saved properly
	@Test
	public void TestCommandEdit() {
		UI ui = new UIStubImpl();
		DataStubImpl data = initData();
		
		addOneTaskShouldPass(data, ui, "Hello Edit");
		
		CommandInput commandInput = new CommandInput(CommandType.EDIT);
		commandInput.setId(1);
		commandInput.setName("Hello Edited");
		
		Command command = new CommandEditImpl(commandInput);
		boolean success = command.processCommand(data, ui);
		
		assertTrue(success);
		assertNotNull(data.getTasks().get(0));
		assertEquals(data.getTasks().get(0).getId(), 1);
		assertEquals(data.getTasks().get(0).getName(), "Hello Edited");
	}
	
	private DataStubImpl initData() {
		DataStubImpl data = DataStubImpl.GetSingleton();
		
		// Needed because of method signature
		// Technically exception would never ever be thrown
		try {
			data.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	// Assert if fail
	private void addOneTaskShouldPass(DataStubImpl data, UI ui, String name) {
		CommandInput commandInput = new CommandInput(CommandType.ADD);
		commandInput.setName(name);
		
		Command command = new CommandAddImpl(commandInput);
		boolean success = command.processCommand(data, ui);
		
		assertTrue(success);
		assertNotNull(data.getTasks().get(0));
		assertEquals(data.getTasks().get(0).getId(), 1);
		assertEquals(data.getTasks().get(0).getName(), name);
	}
	
	// Assert if pass
	private void addOneTaskShouldFail(DataStubImpl data, UI ui, String name) {
		CommandInput commandInput = new CommandInput(CommandType.ADD);
		commandInput.setName(name);
		
		Command command = new CommandAddImpl(commandInput);
		boolean success = command.processCommand(data, ui);
		
		assertFalse(success);
	}
	
}
