package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.*;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

class RemarkCommandTest {

    @Test
    void execute_addRemarkUnfilteredList_success() {
        //        Model model = new ModelManager(); // todo are these stubs?
        //        Model expectedModel = new ModelManager();

        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        String remark = "test remark";
        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(remark));

        Person firstPerson = model.getFilteredPersonList().get(0); // gets first person from starting model
        Person remarkedPerson = new PersonBuilder(firstPerson).withRemark(remark).build(); // changes the remark
        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS, remarkedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), remarkedPerson); // changes the person in th model

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
        // this method actually runs the command to be tested
    }
}