package seedu.multitasky.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.multitasky.commons.exceptions.DataConversionException;
import seedu.multitasky.commons.util.FileUtil;
import seedu.multitasky.model.ReadOnlyEntryBook;

public class XmlEntryBookStorageTest {
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlEntryBookStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readAddressBook_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readAddressBook(null);
    }

    private java.util.Optional<ReadOnlyEntryBook> readAddressBook(String filePath) throws Exception {
        return new XmlEntryBookStorage(filePath).readEntryBook(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null ? TEST_DATA_FOLDER + prefsFileInTestDataFolder : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readAddressBook("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readAddressBook("NotXmlFormatAddressBook.xml");

        /*
         * IMPORTANT: Any code below an exception-throwing line (like the one
         * above) will be ignored. That means you should not have more than one
         * exception test in one method
         */
    }

    @Test
    public void readAndSaveAddressBook_allInOrder_success() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempAddressBook.xml";
        TypicalPersons td = new TypicalPersons();
        AddressBook original = td.getTypicalAddressBook();
        XmlEntryBookStorage xmlAddressBookStorage = new XmlEntryBookStorage(filePath);

        // Save in new file and read back
        xmlAddressBookStorage.saveEntryBook(original, filePath);
        ReadOnlyEntryBook readBack = xmlAddressBookStorage.readEntryBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addPerson(new Person(td.hoon));
        original.removePerson(new Person(td.alice));
        xmlAddressBookStorage.saveEntryBook(original, filePath);
        readBack = xmlAddressBookStorage.readEntryBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        // Save and read without specifying file path
        original.addPerson(new Person(td.ida));
        xmlAddressBookStorage.saveEntryBook(original); // file path not
                                                       // specified
        readBack = xmlAddressBookStorage.readEntryBook().get(); // file path not
                                                                // specified
        assertEquals(original, new AddressBook(readBack));

    }

    @Test
    public void saveAddressBook_nullAddressBook_throwsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        saveAddressBook(null, "SomeFile.xml");
    }

    private void saveAddressBook(ReadOnlyEntryBook addressBook, String filePath) throws IOException {
        new XmlEntryBookStorage(filePath).saveEntryBook(addressBook, addToTestDataPathIfNotNull(filePath));
    }

    @Test
    public void saveAddressBook_nullFilePath_throwsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        saveAddressBook(new AddressBook(), null);
    }

}
