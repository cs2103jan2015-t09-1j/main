package sg.edu.nus.cs2103t.omnitask.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.cs2103t.omnitask.item.Task;

public abstract class Storage {
	//@author A0111795A
	public static boolean CheckIfFileExistAndCreateIfDoesNot(File file)
			throws IOException {
		if (!file.exists()) {
			Files.createFile(file.toPath());
		}

		return true;
	}

	//@author A0119643A
	public static String readFromConfFile() throws IOException {
		String storageDir = "";

		BufferedReader bufferReader = new BufferedReader(new FileReader("omnitask.conf"));
		storageDir = bufferReader.readLine();
		bufferReader.close();
		return storageDir;
	}

	//@author A0111795A
	public abstract ArrayList<Task> readFromFile() throws IOException;

	//@author A0119643A
	/**
	 * Retrieves content from the help file. There is 2 type of help content
	 * aside from the main help there is a mini help menu displayed along when
	 * user types in a code.
	 * <p>
	 */
	public abstract String readFromHelpFile(String helpType, boolean miniMenu)
			throws IOException;

	public abstract void redoFile();

	//@author A0111795A
	public abstract void saveToFile(List<Task> tasks) throws IOException;

	public abstract void undoFile();

	//@author A0119643A
	/**
	 * This method will test if the user specified location exist if it does
	 * create a new storage.txt in the new directory and copy the contents of
	 * the old storage.txt to it. Lastly create a configuration file which
	 * stores the new directory, it will be loaded everytime omnitask starts.
	 * <p>
	 * 
	 */
	public abstract boolean changeStorageFileDirectory(String newDir)
			throws IOException;

	// public abstract String readFromConfFile() throws FileNotFoundException,
	// IOException;

}
