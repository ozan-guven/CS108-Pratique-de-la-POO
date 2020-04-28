package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

/**
 * Check the conditions
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class Preconditions {
    private Preconditions() {
    }

    /**
     * Throws an exception if the condition is not met
     *
     * @param isTrue boolean condition that needs to be checked
     * @throws IllegalArgumentException if the argument is false
     */
    public static void checkArgument(boolean isTrue) {
        if (!isTrue) {
            throw new IllegalArgumentException("The condition has not been met.");
        }
    }

    /**
     * Throws an exception if the condition is not met and prints
     * the given message to the output console.
     *
     * @param isTrue       boolean condition that needs to be checked
     * @param errorMessage message to be print on the screen if the condition is false
     * @throws IllegalArgumentException if the argument is false
     */
    //TODO : On pourrait aussi utiliser cette nouvelle méthode ?
    public static void checkArgument(boolean isTrue, String errorMessage) {
        if (!isTrue) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Checks if a value is in a given interval
     *
     * @param interval the interval
     * @param value    the value
     * @return the value if it is in the interval
     * @throws IllegalArgumentException if the value is not in the interval
     */
    public static double checkInInterval(Interval interval, double value) {
        //checkArgument(interval.contains(value));
        if (!interval.contains(value))
            throw new IllegalArgumentException("The given value exceeds the bounds of the given interval.");
        return value;
    }

}
