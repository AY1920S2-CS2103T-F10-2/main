package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.showInternshipApplicationAtIndex;
import static seedu.address.testutil.InternshipApplicationUtil.createArchivedInternshipApplication;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_INTERNSHIP_APPLICATION;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_INTERNSHIP_APPLICATION;
import static seedu.address.testutil.TypicalInternshipApplications.GOOGLE;
import static seedu.address.testutil.TypicalInternshipApplications.getTypicalInternshipDiary;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.InternshipDiary;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.internship.InternshipApplication;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code UnarchiveCommand}.
 */
public class UnarchiveCommandTest {

    private Model model = new ModelManager(getTypicalInternshipDiary(), new UserPrefs());
    private ModelManager expectedModel = new ModelManager(model.getInternshipDiary(), new UserPrefs());

    @Test
    public void execute_unarchiveOneInternshipApplication_archivalViewSuccess() {
        InternshipApplication archivedInternship = createArchivedInternshipApplication(GOOGLE);
        model.setInternshipDiary(new InternshipDiary());
        model.addInternshipApplication(archivedInternship);
        model.updateFilteredInternshipApplicationList(Model.PREDICATE_SHOW_ARCHIVED_INTERNSHIPS);
        UnarchiveCommand unarchiveCommand = new UnarchiveCommand(INDEX_FIRST_INTERNSHIP_APPLICATION);
        try {
            unarchiveCommand.execute(model);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
        expectedModel.setInternshipDiary(new InternshipDiary());
        expectedModel.addInternshipApplication(GOOGLE);
        expectedModel.updateFilteredInternshipApplicationList(Model.PREDICATE_SHOW_ARCHIVED_INTERNSHIPS);

        assertEquals(model, expectedModel);
    }

    @Test
    public void execute_unarchiveOneInternshipApplication_listViewSuccess() {
        model.setInternshipDiary(new InternshipDiary());
        model.addInternshipApplication(GOOGLE);
        model.archiveInternshipApplication(GOOGLE);
        model.updateFilteredInternshipApplicationList(Model.PREDICATE_SHOW_ARCHIVED_INTERNSHIPS);
        UnarchiveCommand unarchiveCommand = new UnarchiveCommand(INDEX_FIRST_INTERNSHIP_APPLICATION);
        try {
            unarchiveCommand.execute(model);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
        model.updateFilteredInternshipApplicationList(Model.PREDICATE_SHOW_NOT_ARCHIVED_INTERNSHIPS);

        expectedModel.setInternshipDiary(new InternshipDiary());
        expectedModel.addInternshipApplication(GOOGLE);

        assertEquals(model, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredInternshipApplicationList().size() + 1);
        UnarchiveCommand unarchiveCommand = new UnarchiveCommand(outOfBoundIndex);

        assertCommandFailure(unarchiveCommand, model, Messages.MESSAGE_INVALID_INTERNSHIP_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showInternshipApplicationAtIndex(model, INDEX_FIRST_INTERNSHIP_APPLICATION);

        Index outOfBoundIndex = INDEX_SECOND_INTERNSHIP_APPLICATION;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getInternshipDiary().getInternshipList().size());

        UnarchiveCommand unarchiveCommand = new UnarchiveCommand(outOfBoundIndex);

        assertCommandFailure(unarchiveCommand, model, Messages.MESSAGE_INVALID_INTERNSHIP_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        UnarchiveCommand unarchiveFirstCommand = new UnarchiveCommand(INDEX_FIRST_INTERNSHIP_APPLICATION);
        UnarchiveCommand unarchiveSecondCommand = new UnarchiveCommand(INDEX_SECOND_INTERNSHIP_APPLICATION);

        // same object -> returns true
        assertTrue(unarchiveFirstCommand.equals(unarchiveFirstCommand));

        // same values -> returns true
        UnarchiveCommand unarchiveFirstCommandCopy = new UnarchiveCommand(INDEX_FIRST_INTERNSHIP_APPLICATION);
        assertTrue(unarchiveFirstCommand.equals(unarchiveFirstCommandCopy));

        // different types -> returns false
        assertFalse(unarchiveFirstCommand.equals(1));

        // null -> returns false
        assertFalse(unarchiveFirstCommand.equals(null));

        // different internship application index -> returns false
        assertFalse(unarchiveFirstCommand.equals(unarchiveSecondCommand));
    }

}