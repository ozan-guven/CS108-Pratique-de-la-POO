package ch.epfl.rigel.math;

/**
 * A mathematical interval
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public abstract class Interval {
    private final double LOW_BOUND;
    private final double UP_BOUND;

    /**
     * Constructor of the interval
     *
     * @param lowBound the lower bound
     * @param upBound  the upper bound
     */
    protected Interval(double lowBound, double upBound) {
        LOW_BOUND = lowBound;
        UP_BOUND = upBound;
    }

    /**
     * @return the lower bound
     */
    public double low() {
        return LOW_BOUND;
    }

    /**
     * @return the upper bound
     */
    public double high() {
        return UP_BOUND;
    }

    /**
     * @return the size of the interval
     */
    public double size() {
        return UP_BOUND - LOW_BOUND;
    }

    /**
     * Checks if a given value is contained in the interval
     *
     * @param v the value that needs to be checked
     * @return true if v is in the interval and false otherwise
     */
    public abstract boolean contains(double v);

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}
