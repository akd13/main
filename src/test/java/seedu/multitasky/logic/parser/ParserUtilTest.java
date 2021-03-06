package seedu.multitasky.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.multitasky.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.multitasky.testutil.SampleEntries.INDEX_FIRST_ENTRY;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.tag.Tag;

// @@author A0140633R
/**
 * Contains unit tests for ParserUtil methods used by the parser classes.
 */
// @@author
public class ParserUtilTest {
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = "Walk the dog";
    private static final String VALID_TAG_1 = "with_friends";
    private static final String VALID_TAG_2 = "priority";
    private static final String VALID_DATE = "12/1/17 18:30:00";
    private static final String VALID_DATE_2 = "12/1/17 19:00:00";
    private static final Prefix VALID_PREFIX_TAG = CliSyntax.PREFIX_TAG;
    private static final Prefix VALID_PREFIX_FLOAT = CliSyntax.PREFIX_FLOATINGTASK;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseIndex_invalidInput_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        ParserUtil.parseIndex("10 a");
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(MESSAGE_INVALID_INDEX);
        ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_ENTRY, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_ENTRY, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseName(null);
    }

    @Test
    public void parseName_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseName(Optional.empty()).isPresent());
    }

    @Test
    public void parseName_validValue_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        Optional<Name> actualName = ParserUtil.parseName(Optional.of(VALID_NAME));

        assertEquals(expectedName, actualName.get());
    }

    @Test
    public void parseTags_null_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseTags(null);
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    // @@author A0140633R
    @Test
    public void arePrefixesPresent_emptyArgMap_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ArgumentMultimap argMultimap = null;
        ParserUtil.arePrefixesPresent(argMultimap, VALID_PREFIX_FLOAT, VALID_PREFIX_TAG);
    }

    @Test
    public void arePrefixesPresent_noPrefixFound_returnFalse() {
        String argString = " typical argument string without flags";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argString, VALID_PREFIX_TAG,
                                                                  VALID_PREFIX_FLOAT);
        assertFalse(ParserUtil.arePrefixesPresent(argMultimap, VALID_PREFIX_TAG, VALID_PREFIX_FLOAT));
    }

    @Test
    public void arePrefixesPresent_prefixFound_returnTrue() {
        String argString = " typical argument string with flags float 1 tag flagged tagged";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argString, VALID_PREFIX_TAG,
                                                                  VALID_PREFIX_FLOAT);
        assertTrue(ParserUtil.arePrefixesPresent(argMultimap, VALID_PREFIX_TAG, VALID_PREFIX_FLOAT));
    }

    public void areAllPrefixesPresent_emptyArgMap_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ArgumentMultimap argMultimap = null;
        ParserUtil.areAllPrefixesPresent(argMultimap, VALID_PREFIX_FLOAT, VALID_PREFIX_TAG);
    }

    @Test
    public void areAllPrefixesPresent_allPrefixFound_returnTrue() {
        String argString = " typical argument string with flags float 1 tag flagged tagged";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argString, VALID_PREFIX_TAG,
                                                                  VALID_PREFIX_FLOAT);
        assertTrue(ParserUtil.areAllPrefixesPresent(argMultimap, VALID_PREFIX_TAG, VALID_PREFIX_FLOAT));
    }

    @Test
    public void areAllPrefixesPresent_notAllPrefixFound_returnFalse() {
        // only 1 prefix in arg
        String argString = " typical argument string with flags tag flagged tagged";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argString, VALID_PREFIX_TAG,
                                                                  VALID_PREFIX_FLOAT);
        assertFalse(ParserUtil.areAllPrefixesPresent(argMultimap, VALID_PREFIX_TAG, VALID_PREFIX_FLOAT));

        // no prefixes in arg
        argString = " typical argument string without flags";
        argMultimap = ArgumentTokenizer.tokenize(argString, VALID_PREFIX_TAG, VALID_PREFIX_FLOAT);
        assertFalse(ParserUtil.areAllPrefixesPresent(argMultimap, VALID_PREFIX_TAG, VALID_PREFIX_FLOAT));
    }

    @Test
    public void parseDate_emptyString_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        ParserUtil.parseDate("");
    }

    @Test
    public void parseDate_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseDate(Optional.empty()).isPresent());
    }

    @Test
    public void parseDate_validValue_returnsCalendar() throws Exception {
        Calendar expectedCalendar = new GregorianCalendar();
        // following MM/dd/yy format of prettyTime dependency.
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        sdf.setLenient(false);
        Date expectedDate = sdf.parse(VALID_DATE);
        expectedCalendar.setTime(expectedDate);
        Optional<Calendar> actualCalendar = ParserUtil.parseDate(Optional.of(VALID_DATE));

        assertTrue(expectedCalendar.compareTo(actualCalendar.get()) == 0);
    }

    @Test
    public void parseExtendedDate_validValue_returnsCalendar() throws Exception {
        Calendar expectedCalendar = new GregorianCalendar();
        // following MM/dd/yy format of prettyTime dependency.
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        sdf.setLenient(false);
        Date expectedDate = sdf.parse(VALID_DATE_2);
        expectedCalendar.setTime(expectedDate);
        Optional<Calendar> actualCalendar = ParserUtil.parseExtendedDate(Optional.of(VALID_DATE),
                                                                         Optional.of(VALID_DATE_2));

        assertTrue(expectedCalendar.compareTo(actualCalendar.get()) == 0);
    }

    @Test
    public void getMainPrefix_preconditionFailed_throwsAssertionError() throws Exception {
        String argString = " typical argument string with flags flags float 1 tag flagged tagged";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argString, VALID_PREFIX_TAG,
                                                                  VALID_PREFIX_FLOAT);
        thrown.expect(AssertionError.class);
        ParserUtil.getMainPrefix(argMultimap, VALID_PREFIX_TAG, VALID_PREFIX_FLOAT);
    }

    @Test
    public void getMainPrefix_validInput_success() throws Exception {
        String argString = " typical argument string with flag tag flagged tagged";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argString, VALID_PREFIX_TAG,
                                                                  VALID_PREFIX_FLOAT);
        Prefix expectedPrefix = VALID_PREFIX_TAG;
        Prefix actualPrefix = ParserUtil.getMainPrefix(argMultimap, VALID_PREFIX_TAG, VALID_PREFIX_FLOAT);
        assertEquals(expectedPrefix, actualPrefix);
    }

}
