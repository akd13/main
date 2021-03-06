package seedu.multitasky.logic.parser;

import static seedu.multitasky.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.multitasky.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.logic.EditCommandHistory;
import seedu.multitasky.logic.commands.AddCommand;
import seedu.multitasky.logic.commands.ClearCommand;
import seedu.multitasky.logic.commands.Command;
import seedu.multitasky.logic.commands.CompleteCommand;
import seedu.multitasky.logic.commands.DeleteCommand;
import seedu.multitasky.logic.commands.EditCommand;
import seedu.multitasky.logic.commands.ExitCommand;
import seedu.multitasky.logic.commands.FindCommand;
import seedu.multitasky.logic.commands.HelpCommand;
import seedu.multitasky.logic.commands.HistoryCommand;
import seedu.multitasky.logic.commands.ListCommand;
import seedu.multitasky.logic.commands.OpenCommand;
import seedu.multitasky.logic.commands.RedoCommand;
import seedu.multitasky.logic.commands.RestoreCommand;
import seedu.multitasky.logic.commands.SaveCommand;
import seedu.multitasky.logic.commands.UndoCommand;
import seedu.multitasky.logic.parser.exceptions.ParseException;
import seedu.multitasky.model.LogicUserPrefs;

/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)"
                                                                        + "(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput, LogicUserPrefs userprefs,
                                CommandHistory history) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                   HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord.toLowerCase()) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments, userprefs);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments, (EditCommandHistory) history);

        case CompleteCommand.COMMAND_WORD:
            return new CompleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommandParser().parse(arguments);

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case OpenCommand.COMMAND_WORD:
            return new OpenCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommandParser().parse(arguments);

        case SaveCommand.COMMAND_WORD:
            return new SaveCommandParser().parse(arguments);

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        case RestoreCommand.COMMAND_WORD:
            return new RestoreCommandParser().parse(arguments);

        case HistoryCommand.COMMAND_WORD:
            return new HistoryCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
