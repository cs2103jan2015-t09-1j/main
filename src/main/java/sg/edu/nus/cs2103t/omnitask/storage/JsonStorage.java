package sg.edu.nus.cs2103t.omnitask.storage;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import sg.edu.nus.cs2103t.omnitask.DateTimeConverter;
import sg.edu.nus.cs2103t.omnitask.item.Task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JsonStorage extends Storage {
	public Gson gson;

	public File storageFile;

	//@author A0111795A
	public JsonStorage(File storageFile) throws IOException {
		this.storageFile = storageFile;
		this.gson = new GsonBuilder().registerTypeAdapter(
				new TypeToken<DateTime>() {
				}.getType(), new DateTimeConverter()).create();

		Storage.CheckIfFileExistAndCreateIfDoesNot(storageFile);
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

		// return empty arraylist if file has zero items
		ArrayList<Task> tasks = new ArrayList<Task>();

		// convert json to ArrayList
		try {
			ArrayList<Task> tasksFromFile = gson.fromJson(lines,
					new TypeToken<ArrayList<Task>>() {
					}.getType());
			if (tasksFromFile != null) {
				tasks = tasksFromFile;
			}
		} catch (Exception e) {
			// TODO: This is unacceptable, warn user first!
			// File is most likely corrupted, start over
			saveToFile(new ArrayList<Task>());
		}

		return tasks;
	}

	//@author A0119643
	@Override
	public String readFromHelpFile(String helpType, boolean miniMenu)
			throws IOException {
		String commandDescription = "";
		InputStream in;
		if (miniMenu) {
			in = getClass().getResourceAsStream("/omnitext mini help file");
		} else {
			in = getClass().getResourceAsStream("/omnitext help file");
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (line.equals("<" + helpType + ">")) {
				while ((line = reader.readLine()) != null
						&& !line.equals("</" + helpType + ">")) {
					commandDescription += line;
				}
				break;
			}
		}
		in.close();
		return commandDescription;
	}

	@Override
	public void redoFile() {
		// TODO Auto-generated method stub

	}

	//@author A0111795A
	@Override
	public void saveToFile(List<Task> tasks) throws IOException {
		String json = gson.toJson(tasks);

		OutputStream out = new BufferedOutputStream(Files.newOutputStream(
				storageFile.toPath(), StandardOpenOption.WRITE,
				StandardOpenOption.TRUNCATE_EXISTING));
		out.write((json).getBytes());
		out.flush();
		out.close();
	}

	@Override
	public void undoFile() {
		// TODO Auto-generated method stub

	}

	//@author A0119643
	@Override
	public boolean changeStorageFileDirectory(String newDir) throws IOException {
		boolean status=false;
		List<Task> currentTasks;
		try{
		//check if the path exists
		Path path = Paths.get(newDir);
		if (Files.exists(path)) {
			// copy contents
			currentTasks = readFromFile();
			storageFile = new File(newDir+"\\storage.txt");
			Storage.CheckIfFileExistAndCreateIfDoesNot(storageFile);
			saveToFile(currentTasks);
			// create configuration file to store new storage location
			PrintWriter confFile = new PrintWriter("omnitask.conf");
			confFile.println(newDir);
			confFile.close();
			status = true;
			
		}
		}catch(InvalidPathException ip){
			//the path specified by user is invalid
			
		}

		return status;
	}

}
