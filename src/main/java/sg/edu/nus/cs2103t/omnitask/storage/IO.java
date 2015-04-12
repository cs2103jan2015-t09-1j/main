package sg.edu.nus.cs2103t.omnitask.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.cs2103t.omnitask.item.Task;

public abstract class IO {
	public static boolean CheckIfFileExistAndCreateIfDoesNot(File file)
			throws IOException {
		if (!file.exists()) {
			Files.createFile(file.toPath());
		}

		return true;
	}

	public static String readFromConfFile() throws IOException {
		String storageDir = "";

		BufferedReader bufferReader = new BufferedReader(new FileReader("omnitask.conf"));
		storageDir = bufferReader.readLine();
		bufferReader.close();
		return storageDir;
	}

	public abstract ArrayList<Task> readFromFile() throws IOException;

	public abstract String readFromHelpFile(String helpType, boolean miniMenu)
			throws IOException;

	public abstract void redoFile();

	public abstract void saveToFile(List<Task> tasks) throws IOException;

	public abstract void undoFile();

	public abstract boolean changeStorageFileDirectory(String newDir)
			throws IOException;

	// public abstract String readFromConfFile() throws FileNotFoundException,
	// IOException;

}
