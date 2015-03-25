package sg.edu.nus.cs2103t.omnitask;

import java.io.IOException;

import org.junit.Test;

import sg.edu.nus.cs2103t.omnitask.logic.DataImpl;
import sg.edu.nus.cs2103t.omnitask.storage.IOStubImpl;

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
