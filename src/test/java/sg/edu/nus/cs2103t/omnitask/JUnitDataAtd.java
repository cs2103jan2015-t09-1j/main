package sg.edu.nus.cs2103t.omnitask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Test;

import sg.edu.nus.cs2103t.omnitask.logic.Data.TaskNoNameException;
import sg.edu.nus.cs2103t.omnitask.logic.DataImpl;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.storage.IOStubImpl;

public class JUnitDataAtd {
	
	@Test
	public void TestAdd() throws TaskNoNameException, IOException {
		DataImpl data = DataImpl.GetSingleton();
		
		// Needed because of method signature
		// Technically exception would never ever be throw if using IOStubImpl
		try {
			data.init(new IOStubImpl());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Craft tests below
		addOneTask(data, "Testing");
		
	}
	private void addOneTask(final DataImpl data, final String name) throws TaskNoNameException, IOException {
		Task task = new Task();
		task.setName(name);
		
		data.addTask(task);
		assertEquals(data.getTasks().get(0).getId(), task.getId());
	}
}
