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
            throw new IllegalArgumentException();
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
        checkArgument(interval.contains(value));
        return value;
    }

}
