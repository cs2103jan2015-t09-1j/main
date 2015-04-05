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
import sg.edu.nus.cs2103t.omnitasks.command.CommandSearchImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandUndoImpl;
import sg.edu.nus.cs2103t.omnitasks.command.Utils;

import com.joestelmach.natty.DateGroup;

public class ParserMainImpl extends Parser {

	private static final String[] DATE_INDICATORS = new String[] { "from",
			"by", "due", "to", "on" };
	private static final String[] PRIORITY_INDICATORS = new String[] { "^h",
			"^m", "^l", "^n" };

	@Override
	public Command parseUserInput(String input) {
		// TODO: Fix prototype implementation, need to think of the proper way
		// to parse text modularly
		String[] inputSplit = input.split(" ");

		// Get commandName from the first word in user input
		// TODO: Need SLAP?
		String commandName = inputSplit[0].toLowerCase();

		if (Utils.getCommandTypeFromString(commandName) == CommandType.DISPLAY) {
			CommandInput commandInput = new CommandInput(CommandType.DISPLAY);
			commandInput.setCommandType(CommandType.DISPLAY);

			return new CommandDisplayImpl(commandInput);
		}

		if (Utils.getCommandTypeFromString(commandName) == CommandType.UNDO) {
			CommandInput commandInput = new CommandInput(CommandType.UNDO);
			commandInput.setCommandType(CommandType.UNDO);

			return new CommandUndoImpl(commandInput);
		}

		if (Utils.getCommandTypeFromString(commandName) == CommandType.REDO) {
			CommandInput commandInput = new CommandInput(CommandType.REDO);
			commandInput.setCommandType(CommandType.REDO);

			return new CommandRedoImpl(commandInput);
		}

		if (Utils.getCommandTypeFromString(commandName) == CommandType.EXIT) {
			CommandInput commandInput = new CommandInput(CommandType.EXIT);
			commandInput.setCommandType(CommandType.EXIT);

			return new CommandExitImpl(commandInput);
		}

		if (Utils.getCommandTypeFromString(commandName) == CommandType.DELETE) {
			CommandInput commandInput = new CommandInput(CommandType.DELETE);
			commandInput.setCommandType(CommandType.DELETE);

			long deleteId;
			deleteId = Long.parseLong(inputSplit[1]);
			commandInput.setId(deleteId);

			return new CommandDeleteImpl(commandInput);
		}

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
		
		if (Utils.getCommandTypeFromString(commandName) == CommandType.NEXT) {
			CommandInput commandInput = new CommandInput(CommandType.NEXT);
			commandInput.setCommandType(CommandType.NEXT);

			return new CommandNextImpl(commandInput);
		}
		
		if (Utils.getCommandTypeFromString(commandName) == CommandType.PREV) {
			CommandInput commandInput = new CommandInput(CommandType.PREV);
			commandInput.setCommandType(CommandType.PREV);

			return new CommandPrevImpl(commandInput);
		}

		// TODO: Not sure if the parsing should be done in Command class itself.
		// Hmm...
		if (Utils.getCommandTypeFromString(commandName) == CommandType.ADD) {
			CommandInput commandInput = new CommandInput(CommandType.ADD);
			

			detectPrio(inputSplit, commandInput, false);

			extractDatesAndTaskNameFromCommand(1, inputSplit, commandInput);

			return new CommandAddImpl(commandInput);
		}

		// search task command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.SEARCH) {
			CommandInput commandInput = new CommandInput(CommandType.SEARCH);
			commandInput.setCommandType(CommandType.SEARCH);

			if (inputSplit.length > 1)
				commandInput.setName(inputSplit[1]);

			return new CommandSearchImpl(commandInput);
		}

		// help command
		if (Utils.getCommandTypeFromString(commandName) == CommandType.HELP) {
			CommandInput commandInput = new CommandInput(CommandType.HELP);
			commandInput.setCommandType(CommandType.HELP);

			if (inputSplit.length > 1)
				commandInput.setName(inputSplit[1]);

			return new CommandHelpImpl(commandInput);
		}

		if (Utils.getCommandTypeFromString(commandName) == CommandType.EDIT) {
			CommandInput commandInput = new CommandInput(CommandType.EDIT);
			long updateId;
			updateId = Long.parseLong(inputSplit[1]);
			commandInput.setId(updateId);

			detectPrio(inputSplit, commandInput, true);

			extractDatesAndTaskNameFromCommand(2, inputSplit, commandInput);

			return new CommandEditImpl(commandInput);

		}

		// base case, return null
		return null;
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

	// Join a string array with a space
	private String joinStringArray(String[] strArray, int start, int end) {
		String str = "";
		for (int i = start; i < end; i++) {
			str += strArray[i] + " ";
		}

		return str.trim();
	}
	
	private void extractDatesAndTaskNameFromCommand(int startIndex, String[] inputSplit, CommandInput commandInput) {
		String taskName = "";
		
		for (int i = 1; i < inputSplit.length; i++) {
			if (inArray(DATE_INDICATORS, inputSplit[i])) {
				taskName = joinStringArray(inputSplit, startIndex, i);

				// Parse date using Natty
				com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser();
				List<DateGroup> groups = parser.parse(joinStringArray(
						inputSplit, i, inputSplit.length));
				for (DateGroup group : groups) {
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
				}

				break;
			}
		}

		if (taskName.equals("")) {
			taskName = joinStringArray(inputSplit, startIndex, inputSplit.length);
		}

		commandInput.setName(taskName.trim());
	}

	private void detectPrio(String[] inputSplit, CommandInput commandInput, boolean isEditing) {
		int priorityIndex = 0;
		boolean prioritySent = false;

		// detect for priority
		for (int j = 1; j < inputSplit.length; j++) {
			if (inArray(PRIORITY_INDICATORS, inputSplit[j])) {
				priorityIndex = j;
				if (inputSplit[j].equals(PRIORITY_INDICATORS[0])) {
					commandInput.setPriority(Priority.HIGH);
				} else if (inputSplit[j].equals(PRIORITY_INDICATORS[1])) {
					commandInput.setPriority(Priority.MEDIUM);
				} else if (inputSplit[j].equals(PRIORITY_INDICATORS[2])) {
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
}
