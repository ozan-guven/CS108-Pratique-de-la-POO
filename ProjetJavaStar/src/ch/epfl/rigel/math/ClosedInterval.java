package ch.epfl.rigel.math;

import java.util.Locale;

import static ch.epfl.rigel.Preconditions.*;

/**
 * An interval that contains their bounds
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public class ClosedInterval extends Interval {

    private ClosedInterval(double lowBound, double upBound) {
        super(lowBound, upBound);
    }

    /**
     * Creates a closed interval from low to high
     *
     * @param low  the lower bound
     * @param high the upper bound
     * @return a closed interval
     */
    public static ClosedInterval of(double low, double high) {
        checkArgument(low < high);
        return new ClosedInterval(low, high);
    }

    /**
     * Creates a closed interval given its size centered around zero (symmetric interval)
     *
     * @param size the size of the interval
     * @return a symmetric interval
     */
    public static ClosedInterval symmetric(double size) {
        checkArgument(size > 0);
        return new ClosedInterval(-size/2, size/2);
    }

    /**
     * Checks if a value v is in the interval
     *
     * @param v value that needs to be checked
     * @return true if v is in the interval and false otherwise
     */
    @Override
    public boolean contains(double v) {
        return v >= low() && v <= high();
    }

    /**
     * Clips the value of v between the lower and the upper bound
     *
     * @param v value that needs to be clipped
     * @return the value of the clip function evaluated at the value v
     */
    public double clip(double v) {
        if (v <= low()) {
            return low();
        } else {
            return Math.min(v, high());
        }
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "[%.2f, %.2f]",
                low(),
                high());
    }
}
