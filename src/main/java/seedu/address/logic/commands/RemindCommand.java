package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.ReminderDatePredicate;

/**
 * Finds and lists all orders in CakeCollate database whose delivery date is within 3 days of the current date.
 *
 */

public class RemindCommand extends Command {

    public static final String COMMAND_WORD = "remind";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all orders whose delivery dates "
            + "are within X days from the current date, as a list with index numbers.\n"
            + "Parameters: DAYS\n"
            + "Example: " + COMMAND_WORD + " 3";

    private final ReminderDatePredicate predicate;

    public RemindCommand(ReminderDatePredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_REMINDER_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RemindCommand // instanceof handles nulls
                && predicate.equals(((RemindCommand) other).predicate)); // state check
    }
}
