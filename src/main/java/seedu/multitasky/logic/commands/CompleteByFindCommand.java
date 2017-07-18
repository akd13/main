package seedu.multitasky.logic.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;
import seedu.multitasky.model.entry.exceptions.EntryOverdueException;
import seedu.multitasky.model.entry.exceptions.OverlappingAndOverdueEventException;
import seedu.multitasky.model.entry.exceptions.OverlappingEventException;

// @@author A0132788U-reused
/*
 * Finds entries from given keywords and completes the entry if it is the only one found and moves it to archive.
 */
public class CompleteByFindCommand extends CompleteCommand {
    public static final String MESSAGE_NO_ENTRIES = "No entries found! Please try again with different keywords";

    public static final String MESSAGE_MULTIPLE_ENTRIES = "More than one entry found! \n"
                                                          + "Use " + COMMAND_WORD + " ["
                                                          + String.join(" | ",
                                                                  CliSyntax.PREFIX_EVENT.toString(),
                                                                  CliSyntax.PREFIX_DEADLINE.toString(),
                                                                  CliSyntax.PREFIX_FLOATINGTASK.toString())
                                                          + "]"
                                                          + " INDEX to specify which entry to complete.";

    private Set<String> keywords;

    public CompleteByFindCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    @Override
    public CommandResult execute() throws CommandException, DuplicateEntryException {

        // Update all 3 lists with new search parameters until at least 1 result is found.
        model.updateAllFilteredLists(keywords, null, null, Entry.State.ACTIVE, Model.STRICT_SEARCHES);

        // collate a combined list to measure how many entries are found.
        List<ReadOnlyEntry> allList = new ArrayList<>();
        allList.addAll(model.getFilteredDeadlineList());
        allList.addAll(model.getFilteredEventList());
        allList.addAll(model.getFilteredFloatingTaskList());

        if (allList.size() == 1) { // proceed to complete
            entryToComplete = allList.get(0);
            try {
                model.changeEntryState(entryToComplete, Entry.State.ARCHIVED);
            } catch (EntryNotFoundException e) {
                throw new AssertionError("The target entry cannot be missing");
            } catch (OverlappingEventException oee) {
                throw new AssertionError("Overlap should not happen for complete command.");
            } catch (EntryOverdueException e) {
                throw new AssertionError("Overdue should not apply to complete command.");
            } catch (OverlappingAndOverdueEventException e) {
                throw new AssertionError("Overlap and overdue should not apply to complete command.");
            }
            // refresh list view after updating.
            model.updateAllFilteredLists(history.getPrevKeywords(), history.getPrevStartDate(),
                                         history.getPrevEndDate(), history.getPrevState(),
                                         history.getPrevSearches());

            return new CommandResult(String.format(MESSAGE_SUCCESS, entryToComplete));
        } else {
            history.setPrevSearch(keywords, null, null, Entry.State.ACTIVE);
            if (allList.size() >= 2) { // multiple entries found
                throw new CommandException(MESSAGE_MULTIPLE_ENTRIES);
            } else {
                assert (allList.size() == 0); // no entries found
                throw new CommandException(MESSAGE_NO_ENTRIES);
            }
        }
    }

}
