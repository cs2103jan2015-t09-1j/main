package sg.edu.nus.cs2103t.omnitask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.joda.time.DateTime;
import org.junit.Test;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.logic.DataImpl;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput.CommandType;
import sg.edu.nus.cs2103t.omnitask.parser.Parser;
import sg.edu.nus.cs2103t.omnitask.parser.ParserMainImpl;
import sg.edu.nus.cs2103t.omnitask.storage.IOStubImpl;
import sg.edu.nus.cs2103t.omnitasks.command.Command;

public class JUnitDataAtd {
	
	@Test
	public void TestAdd() {
		DataImpl data = DataImpl.GetSingleton();
		
		// Needed because of method signature
		// Technically exception would never ever be throw if using IOStubImpl
		try {
			data.init(new IOStubImpl());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Craft tests below
	}
	
}
