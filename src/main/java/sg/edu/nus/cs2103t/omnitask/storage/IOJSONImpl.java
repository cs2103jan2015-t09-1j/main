package sg.edu.nus.cs2103t.omnitask.storage;

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

import sg.edu.nus.cs2103t.omnitask.model.Task;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class IOJSONImpl extends IO {
	public File storageFile;
	
	public Gson gson;

	public IOJSONImpl(File storageFile) throws IOException {
		this.storageFile = storageFile;
		this.gson = new Gson();
		
		IO.CheckIfFileExistAndCreateIfDoesNot(storageFile);
	}
	
	@Override
	public ArrayList<Task> readFromFile() throws IOException {
		String lines = "";
		
		InputStream in = Files.newInputStream(storageFile.toPath());
	    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	        lines += line + "\n";
	    }
	    in.close();
	    
<<<<<<< HEAD
=======
	    // return empty arraylist if file has zero items
	    if (lines.equals(""))
	    	return new ArrayList<Task>();
	    
>>>>>>> 291d7182368c9abe7e250cac11a32eebeb85e1b3
		// convert json to ArrayList
		return gson.fromJson(lines, new TypeToken<ArrayList<Task>>(){}.getType());
	}
	
	@Override
	public void saveToFile(ArrayList<Task> tasks) throws IOException {
		String json = gson.toJson(tasks);
		
		OutputStream out = new BufferedOutputStream(Files.newOutputStream(storageFile.toPath(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING));
		out.write((json).getBytes());
		out.flush();
		out.close();
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