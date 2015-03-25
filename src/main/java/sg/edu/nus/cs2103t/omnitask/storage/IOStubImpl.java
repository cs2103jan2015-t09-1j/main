package sg.edu.nus.cs2103t.omnitask.storage;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.model.Task;


/**
 * IOStubImpl is to be used for testing only.
 * It simulates normal IO by caching the data in memory instead of writing it to file.
 * 
 * @author Faruq
 *
 */
public class IOStubImpl extends IO {
	
	private ArrayList<Task> tasks;

	public IOStubImpl() {
		this.tasks = new ArrayList<Task>();
	}
	
	@Override
	public ArrayList<Task> readFromFile() {
	    return (ArrayList<Task>) tasks.clone();
	}
	
	@Override
	public void saveToFile(ArrayList<Task> tasks) {
		this.tasks.clear();
		this.tasks.addAll(tasks);
	}

	@Override
	public void undoFile() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redoFile() {
		// TODO Auto-generated method stub
		
	}
}