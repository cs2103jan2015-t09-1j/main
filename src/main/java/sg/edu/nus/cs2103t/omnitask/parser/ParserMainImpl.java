package sg.edu.nus.cs2103t.omnitask.parser;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class ParserMainImpl extends Parser {

private static final String STARTDATE_INDICATOR = "from";
private static final String DUEDATE_INDICATOR = "by";
private static final String ENDDATE_INDICATOR = "to";

@Override
	public CommandInput parseUserInput(String input) {
		// TODO: Fix prototype implementation, need to think of the proper way
		// to parse text modularly
		String[] inputSplit = input.split(" ");
		org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("MM dd yyyy");
		String beforeParse = new String();

		// need to add !inputSplit[0].equals(Commands.COMMAND_DELETE) for
		// subsequent commands to validate if user input a valid command
		if (!inputSplit[0].toLowerCase().equals(CommandInput.COMMAND_ADD)
				&& !inputSplit[0].toLowerCase().equals(CommandInput.COMMAND_DISPLAY)
				&& !inputSplit[0].toLowerCase().equals(CommandInput.COMMAND_DELETE)
				&& !inputSplit[0].toLowerCase().equals(CommandInput.COMMAND_EDIT)
				&& !inputSplit[0].toLowerCase().equals(CommandInput.COMMAND_EXIT)) {
			return null;
		}

		CommandInput commandInput = new CommandInput(inputSplit[0].toLowerCase());
		if (inputSplit[0].toLowerCase().equals(CommandInput.COMMAND_ADD)) {
			String name = "";
			for (int i = 1; i < inputSplit.length; i++) {
				if(inputSplit[i]!=STARTDATE_INDICATOR && inputSplit[i]!=DUEDATE_INDICATOR){//recognize date time in string
				name += inputSplit[i] + " ";
				}
				else if(inputSplit[i] == STARTDATE_INDICATOR){
						beforeParse = inputSplit[i+1];
						commandInput.setStartDate(fmt.parseDateTime(beforeParse));
						if(inputSplit[i+2]==ENDDATE_INDICATOR){
							beforeParse = inputSplit[i+3];
							commandInput.setEndDate(fmt.parseDateTime(beforeParse));
						}
						break;
					}
				else{
					beforeParse = inputSplit[i+1];
					commandInput.setEndDate(fmt.parseDateTime(beforeParse));
					break;
				}
			}
			commandInput.setName(name.trim());
		}

		// this is to parse command specific to delete

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
			String name = "";
			for (int i = 2; i < inputSplit.length; i++) {
				name += inputSplit[i] + " ";
			}
			commandInput.setName(name.trim());
		}
		
		if (inputSplit[0].toLowerCase().equals(CommandInput.COMMAND_EXIT)) {
			commandInput.setName("exit");
		}

		return commandInput;
	}

}
