package sg.edu.nus.cs2103t.omnitask.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;

public class DataStubImpl extends Data {
	
	private static DataStubImpl data;

	private ArrayList<Task> tasks;
	
	private boolean inited;

	public static DataStubImpl GetSingleton() {
		if (data == null) {
			data = new DataStubImpl();
		}
		
		return data;
	}
	
	private DataStubImpl() {
		super();
	}
	
	public DataStubImpl init() throws IOException {
		if (inited) {
			return this;
		}
		
		tasks = new ArrayList<Task>();
		
		return this;
	}
	
	private void assertInited() {
		assert inited;
	}
	
	private long getNewId() {
		assertInited();
		
		long taskId = 1;
		ArrayList<Task> tasks = getTasks();
		if (tasks.size() > 0) {
			taskId = tasks.get(tasks.size() - 1).getId() + 1;
		}

		return taskId;
	}
	
	@Override
	public void notifyDataChanged() {
		assertInited();
		
		for (DataUpdatedListener listener : dataUpdatedListeners) {
			listener.dataUpdated(tasks, null);
		}
	}

	@Override
	public ArrayList<Task> getTasks() {
		assertInited();
		
		return tasks;
	}

	@Override
	public boolean addTask(Task task) throws TaskNoNameException, IOException {
		assertInited();
		
		// Create new task object
		if (task.getName().trim().isEmpty()) {
			throw new TaskNoNameException();
		}
		
		// When adding task, always get new id
		task.setId(getNewId());
		
		// Assign randomUUID to task if it is null
		if (task.getUuid() == null) {
			task.setUuid(UUID.randomUUID());
		}

		tasks.add(task);
		
		notifyDataChanged();
		
		return true;
	}

	@Override
	public boolean deleteTask(Task task) {
		assertInited();
		
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getUuid().equals(task.getUuid())) {
				tasks.remove(i);
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean editTask(Task task) {
		assertInited();
		
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getUuid().equals(task.getUuid())) {
				tasks.set(i, task);
				return true;
			}
		}
		
		return false;
	}

	@Override
	public ArrayList<Task> searchTask() {
		assertInited();
		
		return null;
	}

	@Override

	public String getHelpDescriptors(String helpType) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean undo() {
		return true;
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean redo() {
		// TODO Auto-generated method stub
		return false;
	}

}
