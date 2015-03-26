package sg.edu.nus.cs2103t.omnitask;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import sg.edu.nus.cs2103t.omnitask.logic.DataStubImpl;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput.CommandType;
import sg.edu.nus.cs2103t.omnitasks.command.Command;
import sg.edu.nus.cs2103t.omnitasks.command.Command.CommandResultListener;
import sg.edu.nus.cs2103t.omnitasks.command.CommandAddImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandEditImpl;

public class JUnitCommandsAtd {
	
	// Test add command, make sure it is added successfully
	@Test
	public void TestCommandAdd() {
		DataStubImpl data = initData();
		addOneTaskShouldPass(data, "Hello Add");
	}
	
	// Test add command, make sure empty string will not add successfully
	@Test
	public void TestCommandAddFail1() {
		DataStubImpl data = initData();
		addOneTaskShouldFail(data, "");
	}
	
	// Test add command, make sure empty (untrimmed) string will not add successfully
	@Test
	public void TestCommandAddFail2() {
		DataStubImpl data = initData();
		addOneTaskShouldFail(data, " ");
	}
	
	// Test edit command, make sure edited tasks are saved properly
	@Test
	public void TestCommandEdit() {
		final DataStubImpl data = initData();
		addOneTaskShouldPass(data, "Hello Edit");
		
		CommandInput commandInput = new CommandInput(CommandType.EDIT);
		commandInput.setId(1);
		commandInput.setName("Hello Edited");
		
		Command command = new CommandEditImpl(commandInput);
		command.processCommand(data, new CommandResultListener() {

			public void onSuccess(String msg) {
				assertNotNull(data.getTasks().get(0));
				assertEquals(data.getTasks().get(0).getId(), 1);
				assertEquals(data.getTasks().get(0).getName(), "Hello Edited");
			}

			public void onFailure(String msg) {
				throw new AssertionError("Command should not fail at all!");
			}
			
		});
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
	private void addOneTaskShouldPass(final DataStubImpl data, final String name) {
		CommandInput commandInput = new CommandInput(CommandType.ADD);
		commandInput.setName(name);
		
		Command command = new CommandAddImpl(commandInput);
		command.processCommand(data, new CommandResultListener() {

			public void onSuccess(String msg) {
				assertNotNull(data.getTasks().get(0));
				assertEquals(data.getTasks().get(0).getId(), 1);
				assertEquals(data.getTasks().get(0).getName(), name);
			}

			public void onFailure(String msg) {
				throw new AssertionError("Command should not fail at all!");
			}
			
		});
	}
	
	// Assert if pass
	private void addOneTaskShouldFail(final DataStubImpl data, final String name) {
		CommandInput commandInput = new CommandInput(CommandType.ADD);
		commandInput.setName(name);
		
		Command command = new CommandAddImpl(commandInput);
		command.processCommand(data, new CommandResultListener() {

			public void onSuccess(String msg) {
				throw new AssertionError("Command should not pass at all!");
			}

			public void onFailure(String msg) {
				
			}
			
		});
	}
	
}
