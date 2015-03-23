package sg.edu.nus.cs2103t.omnitask.parser;

import java.util.List;

import org.antlr.runtime.tree.Tree;
import org.joda.time.DateTime;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput.CommandType;
import sg.edu.nus.cs2103t.omnitasks.command.Command;
import sg.edu.nus.cs2103t.omnitasks.command.CommandAddImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandDeleteImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandDisplayImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandEditImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandExitImpl;
import sg.edu.nus.cs2103t.omnitasks.command.CommandSearchImpl;

import com.joestelmach.natty.DateGroup;

public class ParserMainImpl extends Parser {

	private static final String[] DATE_INDICATORS = new String[]{"from", "by", "due", "to", "on"};
	
	@Override
	public Command parseUserInput(String input) {
		// TODO: Fix prototype implementation, need to think of the proper way
		// to parse text modularly
		String[] inputSplit = input.split(" ");
		
		// Get commandName from the first word in user input
		// TODO: Need SLAP?
		String commandName = inputSplit[0].toLowerCase();
		
		if (CommandDisplayImpl.GetCommandTypeFromString(commandName) == CommandType.DISPLAY) {
			CommandInput commandInput = new CommandInput(CommandType.DISPLAY);
			commandInput.setCommandType(CommandType.DISPLAY);
			
			return new CommandDisplayImpl(commandInput);
		}
		
		if(CommandExitImpl.GetCommandTypeFromString(commandName) == CommandType.EXIT){
			CommandInput commandInput = new CommandInput(CommandType.EXIT);
			commandInput.setCommandType(CommandType.EXIT);
			
			return new CommandExitImpl(commandInput);
		}
		
		if(CommandDeleteImpl.GetCommandTypeFromString(commandName) == CommandType.DELETE){
			CommandInput commandInput = new CommandInput(CommandType.DELETE);
			commandInput.setCommandType(CommandType.DELETE);

			long deleteId;
			deleteId = Long.parseLong(inputSplit[1]);
			commandInput.setId(deleteId);
			
			return new CommandDeleteImpl(commandInput);
		}
		
		
		// TODO: Not sure if the parsing should be done in Command class itself. Hmm...
		if (CommandAddImpl.GetCommandTypeFromString(commandName) == CommandType.ADD) {
			CommandInput commandInput = new CommandInput(CommandType.ADD);
			String taskName = "";
			
			for (int i = 1; i < inputSplit.length; i++) {
				if (inArray(DATE_INDICATORS, inputSplit[i])) {
					taskName = joinStringArray(inputSplit, 1, i);
					
					// Parse date using Natty
					com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser();
					List<DateGroup> groups = parser.parse(joinStringArray(inputSplit, i, inputSplit.length));
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
			
			return new CommandAddImpl(commandInput);
		}
		

		//search task command
		if (CommandSearchImpl.GetCommandTypeFromString(commandName) == CommandType.SEARCH) {
			CommandInput commandInput = new CommandInput(CommandType.SEARCH);
			commandInput.setCommandType(CommandType.SEARCH);
			
			
			if(inputSplit.length>1)
			commandInput.setName(inputSplit[1]);
			
				
			return new CommandSearchImpl(commandInput);
		}

		if (CommandEditImpl.GetCommandTypeFromString(commandName) == CommandType.EDIT) {
			CommandInput commandInput = new CommandInput(CommandType.EDIT);
			String taskName = "";
			long updateId;
			updateId = Long.parseLong(inputSplit[1]);
			commandInput.setId(updateId);
						
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
//				taskName = joinStringArray(inputSplit, 2, inputSplit.length);
//			}
			
			commandInput.setName(taskName.trim());
			
			return new CommandEditImpl(commandInput);

		}

		// TODO: Port to new architecture to make it work
		/*
		// parse for delete command
												//CommandInput.COMMAND_DELETE
		if (inputSplit[0].toLowerCase().equals(commandTypes.DELETE.toString())) {
			commandInput.setCommandType(CommandType.DELETE);
			long deleteId;
			deleteId = Long.parseLong(inputSplit[1]);
			commandInput.setId(deleteId);
		}

		// parse for update command
												//CommandInput.COMMAND_EDIT
		if (inputSplit[0].toLowerCase().equals(commandTypes.EDIT.toString())) {
			commandInput.setCommandType(CommandType.EDIT);
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
			
			if (taskName.equals("")) {
				taskName = joinStringArray(inputSplit, 2, inputSplit.length);
			}
			commandInput.setName(taskName.trim());
			//commandInput.setCommandName(commandName);
		}
		
		
		//parse for search command
		
		if (inputSplit[0].toLowerCase().equals(commandTypes.SEARCH.toString())) {
			commandInput.setCommandType(CommandType.SEARCH);
				commandInput.setName(inputSplit[1]);
		}
		
		
												//CommandInput.COMMAND_EXIT
		if (inputSplit[0].toLowerCase().equals(commandTypes.EXIT.toString())) {
			commandInput.setCommandType(CommandType.EXIT);
			commandInput.setName("exit");
		}
		*/
		
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
