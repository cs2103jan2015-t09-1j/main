package sg.edu.nus.cs2103t.omnitask.storage;

import java.io.File;
import java.io.IOException;

import sg.edu.nus.cs2103t.omnitask.model.Task;

public abstract class IO {
	public abstract String readFromFile(File storageFile) throws IOException;
	
	public abstract void saveToFile(Task task) throws IOException;
	
	public abstract boolean CheckIfFileExistAndCreateIfDoesNot(File file) throws IOException;
	
	public abstract void undoFile();
	
	public abstract void redoFile();
	
}
