package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * Class that represent a star
 *
 * @author Ozan Güven (297076)
 * @author Robin Goumaz (301420)
 */
public final class Star extends CelestialObject {

    private static final ClosedInterval COLOR_INDEX_INTERVAL = ClosedInterval.of(-0.5, 5.5);

    private final int hipparcosId;
    private final double colorTemperature;

    /**
     * Constructor of a star
     *
     * @param hipparcosId   the number hipparcos of the star
     * @param name          the name of the star
     * @param equatorialPos the equatorial position of the star
     * @param magnitude     the magnitude of the star
     * @param colorIndex    the color index of the star
     * @throws IllegalArgumentException if the colorIndex is not in [-0.5, 5.5]
     * @throws IllegalArgumentException if the hipparcosId is < 0
     * @throws NullPointerException     if the name is null
     * @throws NullPointerException     if the equatorialPos is null
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex) {
        super(name, equatorialPos, 0, magnitude);

        Preconditions.checkInInterval(COLOR_INDEX_INTERVAL, colorIndex);
        Preconditions.checkArgument(hipparcosId >= 0);

        this.hipparcosId = hipparcosId;

        colorTemperature = 4600 * (1 / (0.92 * colorIndex + 1.7) + 1 / (0.92 * colorIndex + 0.62));
    }

    /**
     * Getter for the hipparcos ID
     *
     * @return the hipparcos ID of the star
     */
    public int hipparcosId() {
        return hipparcosId;
    }

    /**
     * Getter for the color temperature
     *
     * @return the color temperature of the star
     */
    public int colorTemperature() {
        return (int) colorTemperature;
    }
}
