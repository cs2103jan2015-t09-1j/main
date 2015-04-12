package sg.edu.nus.cs2103t.omnitask.storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.cs2103t.omnitask.item.Task;

//@author A0111795A
/**
 * IOStubImpl is to be used for testing only. It simulates normal IO by caching
 * the data in memory instead of writing it to file.
 */
public class StubStorage extends Storage {

	private ArrayList<Task> tasks;

	public StubStorage() {
		this.tasks = new ArrayList<Task>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Task> readFromFile() {
		return (ArrayList<Task>) tasks.clone();
	}

	@Override
	public String readFromHelpFile(String helpType, boolean miniMenu)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void redoFile() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveToFile(List<Task> tasks) {
		this.tasks.clear();
		this.tasks.addAll(tasks);
	}

	@Override
	public void undoFile() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean changeStorageFileDirectory(String newDir) {
		// TODO Auto-generated method stub
		return false;
	}

}
