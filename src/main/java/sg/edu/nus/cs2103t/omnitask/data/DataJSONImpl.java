package sg.edu.nus.cs2103t.omnitask.data;

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

public class DataJSONImpl extends Data {

	private File storageFile;
	
	private Gson gson;
	
	private ArrayList<Task> tasks;
	
	public DataJSONImpl(File storageFile) {
		this.storageFile = storageFile;
		this.gson = new Gson();
		
		try {
			// read storage file
			String json = readFromFile();
			
		    // convert json to ArrayList
			this.tasks = gson.fromJson(json, new TypeToken<ArrayList<Task>>(){}.getType());
		} catch (IOException ex) {
			// TODO: Handle error
			ex.printStackTrace();
		}
		
		if (this.tasks == null) {
			this.tasks = new ArrayList<Task>();
		}
	}

	@Override
	public ArrayList<Task> getTasks() {
		//#tlx read file everytime get task is called?
		try {
			// read storage file
			String json = readFromFile();
			
		    // convert json to ArrayList
			this.tasks = gson.fromJson(json, new TypeToken<ArrayList<Task>>(){}.getType());
		} catch (IOException ex) {
			// TODO: Handle error
			ex.printStackTrace();
		}
		return tasks;
	}

	@Override
	public boolean addTask(Task task) {
		// TODO: Perform sanitization and error checking
		tasks.add(task);
		
		// Save changes to file
		try {
			saveToFile();
		} catch (IOException ex) {
			// TODO: Handle error
			ex.printStackTrace();
			// Reverse change
			tasks.remove(tasks.size()-1);
			return false;
		}
		
		return true;
	}
	
	public boolean deleteTask(int id){
	//#tlx############	

		for(int i=0;i < tasks.size(); i++){
			if(tasks.get(i).getId()==id){
				tasks.remove(i);
			}
		}
		
		//if(this.tasks!=null){
			//this.tasks.remove(id-1);
		//}
	//####################
		
		
		try {
			saveToFile();
		} catch (IOException ex) {
			// TODO: Handle error
			ex.printStackTrace();
			return false;
		} 
		
		return true;
	}
	
	private String readFromFile() throws IOException {
		String lines = "";
		
		InputStream in = Files.newInputStream(storageFile.toPath());
	    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	        lines += line + "\n";
	    }
	    in.close();	    
	    
	    return lines;
	}
	
	private void saveToFile() throws IOException {
		String json = gson.toJson(tasks);
		
		OutputStream out = new BufferedOutputStream(Files.newOutputStream(storageFile.toPath(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING));
		out.write((json).getBytes());
		out.flush();
		out.close();
	}
	
	public static boolean CheckIfFileExistAndCreateIfDoesNot(File file) throws IOException {
		if (!file.exists()) {
			Files.createFile(file.toPath());
		}
		
		return true;
	}
}
