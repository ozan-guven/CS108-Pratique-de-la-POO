package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Class that represents planets
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class Planet extends CelestialObject {

    private final String color;

    /**
     * Constructor of a planet
     *
     * @param name          the name of the planet
     * @param equatorialPos the equatorial position of the planet
     * @param angularSize   the angular size of the planet
     * @param magnitude     the magnitude of the planet
     * @param color         the hexadecimal RGB color of the planet
     * @throws IllegalArgumentException if the angularSize is negative
     * @throws NullPointerException     if the name is null
     * @throws NullPointerException     if the equatorialPos is null
     */
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude, String color) {
        super(name, equatorialPos, angularSize, magnitude);

        this.color = color;
    }

    /**
     * Gets the RGB string color of the planet
     *
     * @return the rgb string color
     */
    public String color() {
        return color;
    }
}
