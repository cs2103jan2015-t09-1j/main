package sg.edu.nus.cs2103t.omnitask.parser;

import java.util.List;

import org.antlr.runtime.tree.Tree;
import org.joda.time.DateTime;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput.CommandType;
import sg.edu.nus.cs2103t.omnitask.model.Task.Priority;
import sg.edu.nus.cs2103t.omnitasks.command.Command;
import sg.edu.nus.cs2103t.omnitasks.command.CommandAddImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandDeleteImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandDisplayImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandEditImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandExitImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandHelpImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandMarkImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandNextImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandPrevImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandRedoImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandRemoveDateImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandSearchImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandStorageImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandUndoImpl;
import sg.edu.nus.cs2103t.omnitasks.command.Utils;

import com.joestelmach.natty.DateGroup;

public class ParserMainImpl extends Parser {

	private static final String[] DATE_INDICATORS = new String[] { "from",
			"by", "due", "to", "on" };
	public static final String[] PRIORITY_INDICATORS = new String[] { "^n", "^l", "^m", "^h"};

	@Override
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
			
			extractDatesAndTaskNameFromCommand(1, inputSplit, commandInput, false);

			return new CommandDisplayImpl(commandInput);
		}

		// Undo command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.UNDO) {
			CommandInput commandInput = new CommandInput(CommandType.UNDO);
			commandInput.setCommandType(CommandType.UNDO);

			return new CommandUndoImpl(commandInput);
		}

		// Re-do command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.REDO) {
			CommandInput commandInput = new CommandInput(CommandType.REDO);
			commandInput.setCommandType(CommandType.REDO);

			return new CommandRedoImpl(commandInput);
		}

		// Next command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.NEXT) {
			CommandInput commandInput = new CommandInput(CommandType.NEXT);
			commandInput.setCommandType(CommandType.NEXT);

			return new CommandNextImpl(commandInput);
		}

		// Prev command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.PREV) {
			CommandInput commandInput = new CommandInput(CommandType.PREV);
			commandInput.setCommandType(CommandType.PREV);

			return new CommandPrevImpl(commandInput);
		}

		// Exit command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.EXIT) {
			CommandInput commandInput = new CommandInput(CommandType.EXIT);
			commandInput.setCommandType(CommandType.EXIT);

			return new CommandExitImpl(commandInput);
		}

		// Delete command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.DELETE) {
			CommandInput commandInput = new CommandInput(CommandType.DELETE);
			commandInput.setCommandType(CommandType.DELETE);

			long deleteId;
			deleteId = Long.parseLong(inputSplit[1]);
			commandInput.setId(deleteId);

			return new CommandDeleteImpl(commandInput);
		}

		// Mark command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.MARK) {
			CommandInput commandInput = new CommandInput(CommandType.MARK);
			commandInput.setCommandType(CommandType.MARK);

			long markId;
			String inputOfIsCompleted;
			markId = Long.parseLong(inputSplit[1]);
			inputOfIsCompleted = (inputSplit[2]);

			if (inputOfIsCompleted.equalsIgnoreCase("done")) {
				commandInput.setCompleted(true);
			} else if (inputOfIsCompleted.equalsIgnoreCase("not done")) {
				commandInput.setCompleted(false);
			}
			commandInput.setId(markId);

			return new CommandMarkImpl(commandInput);
		}
		
		// RemoveDate Command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.REMOVEDATE) {
			CommandInput commandInput = new CommandInput(CommandType.REMOVEDATE);
			commandInput.setCommandType(CommandType.REMOVEDATE);

			long removeDateId;
			removeDateId = Long.parseLong(inputSplit[1]);

			commandInput.setId(removeDateId);

			return new CommandRemoveDateImpl(commandInput);
		}

		// Add Command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.ADD) {
			CommandInput commandInput = new CommandInput(CommandType.ADD);

			// detect if priority is specified by user
			detectPrio(inputSplit, commandInput, false);

			// parse dateTime within input string
			extractDatesAndTaskNameFromCommand(1, inputSplit, commandInput,
					false);

			return new CommandAddImpl(commandInput);
		}

		// Search Task Command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.SEARCH) {
			CommandInput commandInput = new CommandInput(CommandType.SEARCH);
			commandInput.setCommandType(CommandType.SEARCH);

			if (inputSplit.length > 1) {
				commandInput.setName(joinStringArray(inputSplit, 1, inputSplit.length));
			}

			return new CommandSearchImpl(commandInput);
		}
		
		// Storage Command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.STORAGE) {
			CommandInput commandInput = new CommandInput(CommandType.STORAGE);
			commandInput.setCommandType(CommandType.STORAGE);

			if (inputSplit.length > 1)
				commandInput.setName(inputSplit[1]);

			return new CommandStorageImpl(commandInput);
		}

		// Help Command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.HELP) {
			CommandInput commandInput = new CommandInput(CommandType.HELP);
			commandInput.setCommandType(CommandType.HELP);

			if (inputSplit.length > 1)
				commandInput.setName(inputSplit[1]);

			return new CommandHelpImpl(commandInput);
		}

		// Edit Command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.EDIT) {
			CommandInput commandInput = new CommandInput(CommandType.EDIT);
			long updateId;
			updateId = Long.parseLong(inputSplit[1]);
			commandInput.setId(updateId);

			// edit priority of task if detected
			detectPrio(inputSplit, commandInput, true);

			// edit time and name of task if detected within input string
			if (!extractDatesAndTaskNameFromCommand(2, inputSplit,
					commandInput, true)) {
				commandInput.setName(joinStringArray(inputSplit, 2,
						inputSplit.length).trim());
			}

			return new CommandEditImpl(commandInput);

		}

		// base case, return null
		return null;
	}

	// Method to detect priority indicator within string input and remove it
	// from taskName
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
	private boolean extractDatesAndTaskNameFromCommand(int startIndex,
			String[] inputSplit, CommandInput commandInput, boolean isEditing) {
		String taskName = "";
		boolean editted = false;

		boolean quoteDetected = false;
		
		for (int i = startIndex; i < inputSplit.length; i++) {
			// This first block of if conditions check for " symbol and if found, don't use it in natty
			if (inputSplit[i].startsWith("\"") && inputSplit[i].endsWith("\"")) {
				quoteDetected = true;
			} else if (inputSplit[i].startsWith("\"")) {
				quoteDetected = true;
				inputSplit[i] = inputSplit[i].substring(1, inputSplit[i].length());
			} else if (quoteDetected && inputSplit[i].endsWith("\"")) {
				quoteDetected = false;
				inputSplit[i] = inputSplit[i].substring(0, inputSplit[i].length() - 1);
			}
			
			if (!quoteDetected && inArray(DATE_INDICATORS, inputSplit[i])) {
				String dateString = "";
				String ignored = "";
				
				// Similar to the outer if conditions block which checks for " symbol
				// Repeated here because of possibility of having " symbol after detecting the DATE_INDICATORS keywords
				// Any words to be ignored are placed in the ignored variable, Any words to be passed to Natty into dateString variable
				for (int j = i; j < inputSplit.length; j++) {
					if (inputSplit[j].startsWith("\"") && inputSplit[j].endsWith("\"")) {
						quoteDetected = true;
					} else if (inputSplit[j].startsWith("\"")) {
						quoteDetected = true;
						ignored += inputSplit[j].substring(1, inputSplit[j].length()) + " ";
					} else if (quoteDetected && inputSplit[j].endsWith("\"")) {
						quoteDetected = false;
						ignored += inputSplit[j].substring(0, inputSplit[j].length() - 1) + " ";
					}
					
					if (quoteDetected && inputSplit[j].startsWith("\"") && inputSplit[j].endsWith("\"")) {
						ignored += inputSplit[j].substring(1, inputSplit[j].length() - 1) + " ";
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
					
					// If there is nothing parsed by Natty, it is probably part of task name
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
							commandInput.setStartDate(new DateTime(group.getDates()
									.get(0).getTime()));
							if (!isTimeSpecifiedByUser(group.getSyntaxTree()
									.getChild(0))) {
								commandInput.setStartDate(commandInput
										.getStartDate().withMillisOfDay(0));
							}
	
							commandInput.setEndDate(new DateTime(group.getDates()
									.get(1).getTime()));
							if (!isTimeSpecifiedByUser(group.getSyntaxTree()
									.getChild(1))) {
								commandInput.setEndDate(commandInput.getEndDate()
										.withMillisOfDay(0));
							}
						} else {
							commandInput.setEndDate(new DateTime(group.getDates()
									.get(0).getTime()));
							if (!isTimeSpecifiedByUser(group.getSyntaxTree()
									.getChild(0))) {
								commandInput.setEndDate(commandInput.getEndDate()
										.withMillisOfDay(0));
							}
						}
						
						// Put the string which is unused for parsing dates before the ignored variable string
						String unusedDateString = dateString.substring(0, group.getPosition());
						String[] unusedDateStringSplit = unusedDateString.split(" ");
						ignored = joinStringArray(unusedDateStringSplit, 0, unusedDateStringSplit.length - 1) + " " + ignored;
					}
				}
				
				// Insert anything ignored as task name
				taskName += ignored + " ";
				
				break;
			}
			
			// If a single word was quoted, unquote it
			if (quoteDetected && inputSplit[i].startsWith("\"") && inputSplit[i].endsWith("\"")) {
				inputSplit[i] = inputSplit[i].substring(1, inputSplit[i].length()-1);
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
