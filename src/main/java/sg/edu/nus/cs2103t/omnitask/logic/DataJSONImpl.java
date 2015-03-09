package sg.edu.nus.cs2103t.omnitask.logic;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.storage.*;

public class DataJSONImpl extends Data {

	private File storageFile;
	
	private Gson gson;
	
	private ArrayList<Task> tasks;
	
	public DataJSONImpl(File storageFile,String jsonData) {
		this.storageFile = storageFile;
		this.gson = new Gson();
		
		//try {
			// read storage file
			String json = jsonData;
			
		    // convert json to ArrayList
			this.tasks = gson.fromJson(json, new TypeToken<ArrayList<Task>>(){}.getType());
		//} catch (IOException ex) {
			// TODO: Handle error
			//ex.printStackTrace();
		//}
		
		if (this.tasks == null) {
			this.tasks = new ArrayList<Task>();
		}
	}
	

	@Override
	public ArrayList<Task> getTasks() {
		return tasks;
	}

	@Override
	public boolean addTask(Task task) {
		// TODO: Perform sanitization and error checking
		tasks.add(task);
		
		// Save changes to file (move to commandsMainImpl)
		/*try {
			//saveToFile();
		} catch (IOException ex) {
			// TODO: Handle error
			ex.printStackTrace();
			// Reverse change
			tasks.remove(tasks.size()-1);
			return false;
		}*/
		
		return true;
	}
	
}
