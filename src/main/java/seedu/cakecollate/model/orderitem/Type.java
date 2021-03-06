package seedu.cakecollate.model.orderitem;

import static java.util.Objects.requireNonNull;
import static seedu.cakecollate.commons.util.AppUtil.checkArgument;

/**
 * Represents the type of cake of an order item.
 * Guarantees: immutable; is valid as declared in {@link #isValidType(String)}
 */
public class Type {

    public static final String SHARED_CONSTRAINTS_MESSAGE =
            "%s should only contain alphabets, and it should not be blank.";

    public static final String MESSAGE_CONSTRAINTS = String.format(SHARED_CONSTRAINTS_MESSAGE, "Order Type");

    public static final String VALIDATION_REGEX = "^([\\p{Alpha}]|([\\p{Alpha}][\\p{Alpha} ]*))$";

    public final String value;

    /**
     * Constructs a {@code Type}.
     *
     * @param type A valid type.
     */
    public Type(String type) {
        requireNonNull(type);
        checkArgument(isValidType(type), MESSAGE_CONSTRAINTS);
        value = type;
    }

    /**
     * Returns true if a given string is a valid Type.
     */
    public static boolean isValidType(String test) {
        if (test.length() > 0) {
            assert (test.charAt(test.length() - 1) != ' ');
        }
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns true if the value of the type is the same regardless of case.
     *
     * @param other Object to compare with
     * @return whether the two are equal (case insensitive)
     */
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Type // instanceof handles nulls
                && value.equalsIgnoreCase(((Type) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public String getValue() {
        return value;
    }
}
