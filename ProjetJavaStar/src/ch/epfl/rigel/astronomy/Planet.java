package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Class that represents planets
 *
 * @author Ozan Güven (297076)
 */
public final class Planet extends CelestialObject {

    /**
     * Constructor of a planet
     *
     * @param name          the name of the planet
     * @param equatorialPos the equatorial position of the planet
     * @param angularSize   the angular size of the planet
     * @param magnitude     the magnitude of the planet
     * @throws IllegalArgumentException if the angularSize is negative
     * @throws NullPointerException     if the name is null
     * @throws NullPointerException     if the equatorialPos is null
     */
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        super(name, equatorialPos, angularSize, magnitude);
    }
}
