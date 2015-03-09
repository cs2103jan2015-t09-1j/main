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

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.Task;

import com.google.gson.Gson;

public class IOImpl extends IO{
	public File storageFile;
	public Gson gson;
	public ArrayList<Task> tasks;

	public IOImpl() {
	}
	
	@Override
	public String readFromFile(File storageFile) throws IOException {
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
	
	@Override
	public void saveToFile(Task task) throws IOException {
		String json = gson.toJson(tasks);
		
		OutputStream out = new BufferedOutputStream(Files.newOutputStream(storageFile.toPath(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING));
		out.write((json).getBytes());
		out.flush();
		out.close();
	}
	
	
	public boolean CheckIfFileExistAndCreateIfDoesNot(File file) throws IOException {
		if (!file.exists()) {
			Files.createFile(file.toPath());
		}
		
		return true;
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