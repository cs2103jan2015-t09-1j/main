package sg.edu.nus.cs2103t.omnitask.parser;

import java.util.List;

import org.antlr.runtime.tree.Tree;
import org.joda.time.DateTime;

import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput.CommandType;
import sg.edu.nus.cs2103t.omnitask.item.Task.Priority;
import sg.edu.nus.cs2103t.omnitasks.command.Command;
import sg.edu.nus.cs2103t.omnitasks.command.CommandAdd;
import sg.edu.nus.cs2103t.omnitasks.command.CommandArchive;
import sg.edu.nus.cs2103t.omnitasks.command.CommandDelete;
import sg.edu.nus.cs2103t.omnitasks.command.CommandDisplay;
import sg.edu.nus.cs2103t.omnitasks.command.CommandEdit;
import sg.edu.nus.cs2103t.omnitasks.command.CommandExit;
import sg.edu.nus.cs2103t.omnitasks.command.CommandHelp;
import sg.edu.nus.cs2103t.omnitasks.command.CommandMark;
import sg.edu.nus.cs2103t.omnitasks.command.CommandNext;
import sg.edu.nus.cs2103t.omnitasks.command.CommandPrev;
import sg.edu.nus.cs2103t.omnitasks.command.CommandRedo;
import sg.edu.nus.cs2103t.omnitasks.command.CommandRemoveDate;
import sg.edu.nus.cs2103t.omnitasks.command.CommandSearch;
import sg.edu.nus.cs2103t.omnitasks.command.CommandStorage;
import sg.edu.nus.cs2103t.omnitasks.command.CommandUnarchive;
import sg.edu.nus.cs2103t.omnitasks.command.CommandUndo;
import sg.edu.nus.cs2103t.omnitasks.command.Utils;

import com.joestelmach.natty.DateGroup;

//@A0116347H
public class Parser {

	private static final String[] DATE_INDICATORS = new String[] { "from",
			"by", "due", "to", "on" };
	public static final String[] PRIORITY_INDICATORS = new String[] { "^n",
			"^l", "^m", "^h" };
/**
 * This function reads from user input, then parses the type of commands entered by user
 * and passes attributes contained within the input to CommandInput class for further
 * processing.
 * 
 * @param input 
 *             Input text from user
 * @return Command
 *             commandType and attributes to be processed
 */
	public Command parseUserInput(String input) {
		// TODO: Fix prototype implementation, need to think of the proper way
		// to parse text modularly
		String[] inputSplit = input.split(" ");

		// Get commandName from the first word in user input
		// TODO: Need SLAP?
		String commandName = inputSplit[0].toLowerCase();

		// Display command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.DISPLAY) {
			CommandInput commandInput = new CommandInput(CommandType.DISPLAY);
			commandInput.setCommandType(CommandType.DISPLAY);

			extractDatesAndTaskNameFromCommand(1, inputSplit, commandInput,
					false);

			return new CommandDisplay(commandInput);
		}

		// Undo command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.UNDO) {
			CommandInput commandInput = new CommandInput(CommandType.UNDO);
			commandInput.setCommandType(CommandType.UNDO);

			return new CommandUndo(commandInput);
		}

		// Re-do command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.REDO) {
			CommandInput commandInput = new CommandInput(CommandType.REDO);
			commandInput.setCommandType(CommandType.REDO);

			return new CommandRedo(commandInput);
		}

		// Next command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.NEXT) {
			CommandInput commandInput = new CommandInput(CommandType.NEXT);
			commandInput.setCommandType(CommandType.NEXT);

			return new CommandNext(commandInput);
		}

		// Prev command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.PREV) {
			CommandInput commandInput = new CommandInput(CommandType.PREV);
			commandInput.setCommandType(CommandType.PREV);

			return new CommandPrev(commandInput);
		}

		// Exit command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.EXIT) {
			CommandInput commandInput = new CommandInput(CommandType.EXIT);
			commandInput.setCommandType(CommandType.EXIT);

			return new CommandExit(commandInput);
		}

		// Delete command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.DELETE) {
			CommandInput commandInput = new CommandInput(CommandType.DELETE);
			commandInput.setCommandType(CommandType.DELETE);

			long deleteId;
			try {
				deleteId = Long.parseLong(inputSplit[1]);
			} catch (NumberFormatException e) {
				return null;
			}
			commandInput.setId(deleteId);

			return new CommandDelete(commandInput);
		}

		// Mark command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.MARK) {
			CommandInput commandInput = new CommandInput(CommandType.MARK);
			commandInput.setCommandType(CommandType.MARK);

			long markId;
			try {
				markId = Long.parseLong(inputSplit[1]);
			} catch (NumberFormatException e) {
				return null;
			}
			//invalid command if not specified done or undone
            if (inputSplit.length==2) {
            	return null;
            } else if (inputSplit[2].equalsIgnoreCase("done")) {
				commandInput.setCompleted(true);
			} else if (inputSplit[2].equalsIgnoreCase("undone")) {
				commandInput.setCompleted(false);
			} else {
				return null;
			}
			commandInput.setId(markId);

			return new CommandMark(commandInput);
		}

		// RemoveDate Command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.REMOVEDATE) {
			CommandInput commandInput = new CommandInput(CommandType.REMOVEDATE);
			commandInput.setCommandType(CommandType.REMOVEDATE);

			long removeDateId;
			try {
				removeDateId = Long.parseLong(inputSplit[1]);
			} catch (NumberFormatException e) {
				return null;
			}

			commandInput.setId(removeDateId);

			return new CommandRemoveDate(commandInput);
		}

		// Add Command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.ADD) {
			CommandInput commandInput = new CommandInput(CommandType.ADD);

			// detect if priority is specified by user
			detectPrio(inputSplit, commandInput, false);

			// parse dateTime within input string
			extractDatesAndTaskNameFromCommand(1, inputSplit, commandInput,
					false);

			return new CommandAdd(commandInput);
		}

		// Search Task Command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.SEARCH) {
			CommandInput commandInput = new CommandInput(CommandType.SEARCH);
			commandInput.setCommandType(CommandType.SEARCH);

			if (inputSplit.length > 1) {
				commandInput.setName(joinStringArray(inputSplit, 1,
						inputSplit.length));
			}

			return new CommandSearch(commandInput);
		}

		// Storage Command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.STORAGE) {
			CommandInput commandInput = new CommandInput(CommandType.STORAGE);
			commandInput.setCommandType(CommandType.STORAGE);

			if (inputSplit.length > 1)
				commandInput.setName(inputSplit[1]);

			return new CommandStorage(commandInput);
		}

		// Help Command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.HELP) {
			CommandInput commandInput = new CommandInput(CommandType.HELP);
			commandInput.setCommandType(CommandType.HELP);

			if (inputSplit.length > 1)
				commandInput.setName(inputSplit[1]);

			return new CommandHelp(commandInput);
		}

		// Archive Command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.ARCHIVE) {
			CommandInput commandInput = new CommandInput(CommandType.ARCHIVE);
			long updateId = -1;
			try {
				updateId = Long.parseLong(inputSplit[1]);
				commandInput.setId(updateId);
			} catch (NumberFormatException e) {
			}

			if (updateId == -1 && inputSplit.length > 1)
				commandInput.setName(inputSplit[1]);

			return new CommandArchive(commandInput);
		}

		// Unarchive Command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.UNARCHIVE) {
			CommandInput commandInput = new CommandInput(CommandType.UNARCHIVE);
			long updateId = -1;
			try {
				updateId = Long.parseLong(inputSplit[1]);
				commandInput.setId(updateId);
			} catch (NumberFormatException e) {
				return null;
			}

			return new CommandUnarchive(commandInput);
		}

		// Edit Command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.EDIT) {
			CommandInput commandInput = new CommandInput(CommandType.EDIT);
			long updateId;
			try {
				updateId = Long.parseLong(inputSplit[1]);
			} catch (NumberFormatException e) {
				return null;
			}
			commandInput.setId(updateId);

			// edit priority of task if detected
			detectPrio(inputSplit, commandInput, true);

			// edit time and name of task if detected within input string
			if (!extractDatesAndTaskNameFromCommand(2, inputSplit,
					commandInput, true)) {
				commandInput.setName(joinStringArray(inputSplit, 2,
						inputSplit.length).trim());
			}

			return new CommandEdit(commandInput);

		}

		// base case, return null
		return null;
	}

	// Method to detect priority indicator within string input and remove it
	// from taskName
	/**
	 * This method checks though the user input for priority indicator, it will pass the 
	 * priority found to date class for editAttributes, if no priority is detected, it leaves 
	 * the field unchanged for editing and set it to none when adding
	 * 
	 * @param inputSplit  
	 *                  user input in String array
	 * @param commandInput
	 *                  commandInput 
	 * @param isEditing
	 *                  to check if the method is called by adding or editing algorithm
	 */
	private void detectPrio(String[] inputSplit, CommandInput commandInput,
			boolean isEditing) {
		int priorityIndex = 0;
		boolean prioritySent = false;

		// Detect for priority
		for (int j = 1; j < inputSplit.length; j++) {
			if (inArray(PRIORITY_INDICATORS, inputSplit[j])) {
				priorityIndex = j;
				if (inputSplit[j].equals(PRIORITY_INDICATORS[3])) {
					commandInput.setPriority(Priority.HIGH);
				} else if (inputSplit[j].equals(PRIORITY_INDICATORS[2])) {
					commandInput.setPriority(Priority.MEDIUM);
				} else if (inputSplit[j].equals(PRIORITY_INDICATORS[1])) {
					commandInput.setPriority(Priority.LOW);
				} else {
					commandInput.setPriority(Priority.NONE);
				}

				prioritySent = true;
				// remove priority after setting
				if (priorityIndex >= inputSplit.length - 1) {
					inputSplit[inputSplit.length - 1] = "";
				} else {
					for (int k = priorityIndex; k < inputSplit.length - 1; k++) {
						inputSplit[k] = inputSplit[k + 1];
					}
					inputSplit[inputSplit.length - 1] = "";
				}
				break;
			}
		}

		// set priority to none if not detected
		if (!isEditing && prioritySent == false) {
			commandInput.setPriority(Priority.NONE);
		}
	}

	// Parse the dateTime within the string input using natty
	/**
	 * This method checks the input from user for time indicator, when time indicator is found
	 * it will check the item after it if it is a date/time. Then it will pass the String 
	 * contains the date/time to natty and passes the returned date/time to editAttribute.
	 * And also set the taskName excluding the date/time String.
	 * 
	 * @param startIndex
	 *              index of the time indicator
	 * @param inputSplit
	 *              input String array
	 * @param commandInput
	 *              commandInput
	 * @param isEditing
	 *              to check if the method is called by adding or editing algorithm
	 * @return
	 */
	private boolean extractDatesAndTaskNameFromCommand(int startIndex,
			String[] inputSplit, CommandInput commandInput, boolean isEditing) {
		String taskName = "";
		boolean editted = false;

		boolean quoteDetected = false;

		for (int i = startIndex; i < inputSplit.length; i++) {
			// This first block of if conditions check for " symbol and if
			// found, don't use it in natty
			if (inputSplit[i].startsWith("\"") && inputSplit[i].endsWith("\"")) {
				quoteDetected = true;
			} else if (inputSplit[i].startsWith("\"")) {
				quoteDetected = true;
				inputSplit[i] = inputSplit[i].substring(1,
						inputSplit[i].length());
			} else if (quoteDetected && inputSplit[i].endsWith("\"")) {
				quoteDetected = false;
				inputSplit[i] = inputSplit[i].substring(0,
						inputSplit[i].length() - 1);
			}

			if (!quoteDetected && inArray(DATE_INDICATORS, inputSplit[i])) {
				String dateString = "";
				String ignored = "";

				// Similar to the outer if conditions block which checks for "
				// symbol
				// Repeated here because of possibility of having " symbol after
				// detecting the DATE_INDICATORS keywords
				// Any words to be ignored are placed in the ignored variable,
				// Any words to be passed to Natty into dateString variable
				for (int j = i; j < inputSplit.length; j++) {
					if (inputSplit[j].startsWith("\"")
							&& inputSplit[j].endsWith("\"")) {
						quoteDetected = true;
					} else if (inputSplit[j].startsWith("\"")) {
						quoteDetected = true;
						ignored += inputSplit[j].substring(1,
								inputSplit[j].length())
								+ " ";
					} else if (quoteDetected && inputSplit[j].endsWith("\"")) {
						quoteDetected = false;
						ignored += inputSplit[j].substring(0,
								inputSplit[j].length() - 1)
								+ " ";
					}

					if (quoteDetected && inputSplit[j].startsWith("\"")
							&& inputSplit[j].endsWith("\"")) {
						ignored += inputSplit[j].substring(1,
								inputSplit[j].length() - 1)
								+ " ";
						quoteDetected = false;
					} else if (!quoteDetected) {
						dateString += inputSplit[j] + " ";
					}
				}

				// reset the variable back
				quoteDetected = false;

				// if there is something to parse, parse it with natty
				dateString = dateString.trim();

				if (!dateString.isEmpty()) {
					// Parse date using Natty
					com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser();
					List<DateGroup> groups = parser.parse(dateString.trim());

					// If there is nothing parsed by Natty, it is probably part
					// of task name
					if (groups.size() == 0) {
						taskName += inputSplit[i] + " ";
						continue;
					}

					editted = true;

					if (groups.size() > 0) {
						DateGroup group = groups.get(groups.size() - 1);
						// If there are 2 dates, means it's to and from
						// If no specific time is specified by user, set the
						// time to 00:00:00, retaining the dates
						if (group.getDates().size() == 2) {
							commandInput.setStartDate(new DateTime(group
									.getDates().get(0).getTime()));
							if (!isTimeSpecifiedByUser(group.getSyntaxTree()
									.getChild(0))) {
								commandInput.setStartDate(commandInput
										.getStartDate().withMillisOfDay(0));
							}

							commandInput.setEndDate(new DateTime(group
									.getDates().get(1).getTime()));
							if (!isTimeSpecifiedByUser(group.getSyntaxTree()
									.getChild(1))) {
								commandInput.setEndDate(commandInput
										.getEndDate().withMillisOfDay(0));
							}
						} else {
							commandInput.setEndDate(new DateTime(group
									.getDates().get(0).getTime()));
							if (!isTimeSpecifiedByUser(group.getSyntaxTree()
									.getChild(0))) {
								commandInput.setEndDate(commandInput
										.getEndDate().withMillisOfDay(0));
							}
						}

						// Put the string which is unused for parsing dates
						// before the ignored variable string
						String unusedDateString = dateString.substring(0,
								group.getPosition());
						String[] unusedDateStringSplit = unusedDateString
								.split(" ");
						ignored = joinStringArray(unusedDateStringSplit, 0,
								unusedDateStringSplit.length - 1)
								+ " "
								+ ignored;
					}
				}

				// Insert anything ignored as task name
				taskName += ignored + " ";

				break;
			}

			// If a single word was quoted, unquote it
			if (quoteDetected && inputSplit[i].startsWith("\"")
					&& inputSplit[i].endsWith("\"")) {
				inputSplit[i] = inputSplit[i].substring(1,
						inputSplit[i].length() - 1);
				quoteDetected = false;
			}

			// Insert anything not to be parsed by Natty as task name
			taskName += inputSplit[i] + " ";
		}

		// to indicate if taskName is edited
		commandInput.setName(taskName.trim());
		return editted;
	}

	// Do case-insensitive search of word in array
	private boolean inArray(String[] haystack, String needle) {
		for (int i = 0; i < haystack.length; i++) {
			if (haystack[i].toLowerCase().equals(needle.toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	// method to indicate if time is specified by the user in the input string
	private boolean isTimeSpecifiedByUser(Tree tree) {
		boolean timeSpecified = false;
		for (int j = 0; j < tree.getChildCount(); j++) {
			if (tree.getChild(j).getText().equals("EXPLICIT_TIME")) {
				timeSpecified = true;
				break;
			}
		}

		return timeSpecified;
	}

	// Join a string array with a space
	private String joinStringArray(String[] strArray, int start, int end) {
		String str = "";
		for (int i = start; i < end; i++) {
			str += strArray[i] + " ";
		}

		return str.trim();
	}
}
//@A0116347H