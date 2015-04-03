package sg.edu.nus.cs2103t.omnitask.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.cs2103t.omnitask.model.Task;

public abstract class IO {
	public abstract ArrayList<Task> readFromFile() throws IOException;
	
	public abstract void saveToFile(List<Task> tasks, boolean undoable) throws IOException;
	
	public abstract boolean undoFile() throws IOException;
	
	public abstract boolean redoFile() throws IOException;
	
	public abstract String readFromHelpFile(String helpType) throws IOException; 
	
	public static boolean CheckIfFileExistAndCreateIfDoesNot(File file) throws IOException {
		if (!file.exists()) {
			Files.createFile(file.toPath());
		}
		
		return true;
	}

	
	
}
