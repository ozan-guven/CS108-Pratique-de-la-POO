package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Class that represent a star
 *
 * @author Robin Goumaz (301420)
 */
public final class Star extends CelestialObject{
    private final int hipparcosId;
    private final double colorTemperature;

    /**
     * Constructor of a star
     *
     * @param hipparcosId   the number hipparcos of the star
     * @param name          the name of the star
     * @param equatorialPos the equatorial position of the star
     * @param magnitude     the magnitude of the star
     * @throws IllegalArgumentException if the angularSize is negative
     * @throws NullPointerException     if the name is null
     * @throws NullPointerException     if the equatorialPos is null
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex) {
        super(name, equatorialPos, 0, magnitude);

        this.hipparcosId = hipparcosId;

        colorTemperature = 4600*(1 / (0.92 * colorIndex + 1.7) + 1 / (0.92 * colorIndex + 0.62));
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
        return (int)colorTemperature;
    }
}
