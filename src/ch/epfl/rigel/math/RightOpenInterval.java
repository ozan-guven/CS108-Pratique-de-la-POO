package ch.epfl.rigel.math;

import java.util.Locale;

import static ch.epfl.rigel.Preconditions.checkArgument;

/**
 * An interval that contains its left bound but not its right bound
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class RightOpenInterval extends Interval {

    private RightOpenInterval(double lowBound, double upBound) {
        super(lowBound, upBound);
    }

    /**
     * Creates a right open interval from low to high
     *
     * @param low  the lower bound
     * @param high the upper bound
     * @return a right open interval
     * @throws IllegalArgumentException if low >= high
     */
    public static RightOpenInterval of(double low, double high) {
        checkArgument(low < high);
        return new RightOpenInterval(low, high);
    }

    /**
     * Creates a right open interval given its size centered around zero (symmetric interval)
     *
     * @param size the size of the interval
     * @return a symmetric interval
     * @throws IllegalArgumentException if the size is not strictly positive
     */
    public static RightOpenInterval symmetric(double size) {
        checkArgument(size > 0);
        double halfSize = size / 2;
        return new RightOpenInterval(-halfSize, halfSize);
    }

    /**
     * Checks if a value v is in the interval
     *
     * @param v value that needs to be checked
     * @return true if v is in the interval and false otherwise
     */
    @Override
    public boolean contains(double v) {
        return v >= low() && v < high();
    }

    /**
     * Reduces the value v within the interval
     *
     * @param v the value that needs to be reduced
     * @return the reduced value
     */
    public double reduce(double v) {
        if (this.contains(v))
            return v;
        double x = v - low();
        return low() + x - size() * Math.floor(x / size());
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "[%.2f, %.2f[",
                low(),
                high());
    }
}
