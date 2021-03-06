package seedu.cakecollate.model.order;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.cakecollate.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class OrderDescriptionTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new OrderDescription(null));
    }


    @Test
    public void constructor_invalidOrderDescription_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new OrderDescription(" "));
        assertThrows(IllegalArgumentException.class, () -> new OrderDescription(""));
    }

    @Test
    public void isValidOrderDescription() {
        // null order descriptions
        assertThrows(NullPointerException.class, () -> OrderDescription.isValidOrderDescription(null));

        // invalid order descriptions
        assertFalse(OrderDescription.isValidOrderDescription("")); // empty string
        assertFalse(OrderDescription.isValidOrderDescription(" ")); // spaces only
        assertFalse(OrderDescription.isValidOrderDescription("          ")); // spaces only
        assertFalse(OrderDescription.isValidOrderDescription("Cake##")); // with special characters excluding "
        assertFalse(OrderDescription.isValidOrderDescription("12345")); // numbers
        assertFalse(OrderDescription.isValidOrderDescription("2 x Chocolate cake")); // numbers

        // valid order descriptions
        assertTrue(OrderDescription.isValidOrderDescription("chocolate cake")); // alphabets only
    }

}
