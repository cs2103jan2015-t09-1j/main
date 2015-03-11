package sg.edu.nus.cs2103t.omnitask.parser;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;

import com.joestelmach.natty.DateGroup;

import java.lang.Object;
import java.util.List;

import org.antlr.runtime.tree.Tree;
import org.joda.time.DateTime;

public class ParserMainImpl extends Parser {

	private static final String[] DATE_INDICATORS = new String[]{"from", "by", "due", "to", "on"};

	@Override
	public CommandInput parseUserInput(String input) {
		// TODO: Fix prototype implementation, need to think of the proper way
		// to parse text modularly
		String[] inputSplit = input.split(" ");

		// need to add !inputSplit[0].equals(Commands.COMMAND_DELETE) for
		// subsequent commands to validate if user input a valid command
		if (!inputSplit[0].toLowerCase().equals(CommandInput.COMMAND_ADD)
				&& !inputSplit[0].toLowerCase().equals(
						CommandInput.COMMAND_DISPLAY)
				&& !inputSplit[0].toLowerCase().equals(
						CommandInput.COMMAND_DELETE)
				&& !inputSplit[0].toLowerCase().equals(
						CommandInput.COMMAND_EDIT)
				&& !inputSplit[0].toLowerCase().equals(
						CommandInput.COMMAND_EXIT)) {
			return null;
		}
		
		CommandInput commandInput = new CommandInput(inputSplit[0]);
		
		if (commandInput.getCommandName().equals(CommandInput.COMMAND_ADD)) {
			String taskName = "";
			
			for (int i = 1; i < inputSplit.length; i++) {
				if (inArray(DATE_INDICATORS, inputSplit[i])) {
					taskName = joinStringArray(inputSplit, 1, i);
					
					// Parse date using Natty
					com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser();
					List<DateGroup> groups = parser.parse(input);
					for (DateGroup group : groups) {
						// If there are 2 dates, means it's to and from
						// If no specific time is specified by user, set the time to 00:00:00, retaining the dates
						if (group.getDates().size() == 2) {
							commandInput.setStartDate(new DateTime(group.getDates().get(0).getTime()));
							if (!isTimeSpecifiedByUser(group.getSyntaxTree().getChild(0))) {
								commandInput.setStartDate(commandInput.getStartDate().withMillisOfDay(0));
							}
							
							commandInput.setEndDate(new DateTime(group.getDates().get(1).getTime()));
							if (!isTimeSpecifiedByUser(group.getSyntaxTree().getChild(1))) {
								commandInput.setEndDate(commandInput.getEndDate().withMillisOfDay(0));
							}
						} else {
							commandInput.setEndDate(new DateTime(group.getDates().get(0).getTime()));
							if (!isTimeSpecifiedByUser(group.getSyntaxTree().getChild(0))) {
								commandInput.setEndDate(commandInput.getEndDate().withMillisOfDay(0));
							}
						}
					}
					
					break;
				}
			}
			
			if (taskName.equals("")) {
				taskName = joinStringArray(inputSplit, 1, inputSplit.length);
			}
			
			commandInput.setName(taskName.trim());
		}

		// parse for delete command

		if (inputSplit[0].toLowerCase().equals(CommandInput.COMMAND_DELETE)) {
			long deleteId;
			deleteId = Long.parseLong(inputSplit[1]);
			commandInput.setId(deleteId);
		}

		// parse for update command

		if (inputSplit[0].toLowerCase().equals(CommandInput.COMMAND_EDIT)) {
			long updateId;
			updateId = Long.parseLong(inputSplit[1]);
			commandInput.setId(updateId);
			String taskName = "";
//			String name = "";
//			for (int i = 2; i < inputSplit.length; i++) {
//				taskName += inputSplit[i] + " ";
//			}
			
			
			for (int i = 2; i < inputSplit.length; i++) {
				if (inArray(DATE_INDICATORS, inputSplit[i])) {
					taskName = joinStringArray(inputSplit, 2, i);
					
					// Parse date using Natty
					com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser();
					List<DateGroup> groups = parser.parse(input);
					for (DateGroup group : groups) {
						// If there are 2 dates, means it's to and from
						// If no specific time is specified by user, set the time to 00:00:00, retaining the dates
						if (group.getDates().size() == 2) {
							commandInput.setStartDate(new DateTime(group.getDates().get(0).getTime()));
							if (!isTimeSpecifiedByUser(group.getSyntaxTree().getChild(0))) {
								commandInput.setStartDate(commandInput.getStartDate().withMillisOfDay(0));
							}
							
							commandInput.setEndDate(new DateTime(group.getDates().get(1).getTime()));
							if (!isTimeSpecifiedByUser(group.getSyntaxTree().getChild(1))) {
								commandInput.setEndDate(commandInput.getEndDate().withMillisOfDay(0));
							}
						} else {
							commandInput.setEndDate(new DateTime(group.getDates().get(0).getTime()));
							if (!isTimeSpecifiedByUser(group.getSyntaxTree().getChild(0))) {
								commandInput.setEndDate(commandInput.getEndDate().withMillisOfDay(0));
							}
						}
					}
					
					break;
				}
			}
			
//			if (taskName.equals("")) {
//				taskName = joinStringArray(inputSplit, 1, inputSplit.length);
//			}
			commandInput.setName(taskName.trim());
		}

		if (inputSplit[0].toLowerCase().equals(CommandInput.COMMAND_EXIT)) {
			commandInput.setName("exit");
		}

		return commandInput;
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
