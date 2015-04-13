package sg.edu.nus.cs2103t.omnitask;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.joda.time.DateTime;

//@author A0111795A
/**
 * Simple logger to log to file. Largely unused.
 *
 */
public class Logger {
	private static Path logFile = new File("log.txt").toPath();

	public static void writeDebug(String msg) {
		writeToFile(logFile, DateTime.now() + " [D]: " + msg + "\r\n");
	}

	public static void writeError(String msg) {
		writeToFile(logFile, DateTime.now() + " [E]: " + msg + "\r\n");
	}

	private static void writeToFile(Path file, String string) {
		try {
			if (!file.toFile().exists()) {
				Files.createFile(file);
			}

			OutputStream out = new BufferedOutputStream(Files.newOutputStream(
					file, StandardOpenOption.WRITE, StandardOpenOption.APPEND));
			out.write((string).getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
