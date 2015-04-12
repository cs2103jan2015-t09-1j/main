package sg.edu.nus.cs2103t.omnitask.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.cs2103t.omnitask.item.Task;

//@author A0111795A
public abstract class Storage {
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
	public abstract String readFromHelpFile(String helpType, boolean miniMenu)
			throws IOException;

	public abstract void redoFile();

	//@author A0111795A
	public abstract void saveToFile(List<Task> tasks) throws IOException;

	public abstract void undoFile();

	//@author A0119643A
	public abstract boolean changeStorageFileDirectory(String newDir)
			throws IOException;

	// public abstract String readFromConfFile() throws FileNotFoundException,
	// IOException;

}
