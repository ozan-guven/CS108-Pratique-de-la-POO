package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import javafx.scene.paint.Color;

/**
 * Class that represents planets
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class Planet extends CelestialObject {
    public final Color color;


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
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude, Color color) {
        super(name, equatorialPos, angularSize, magnitude);

        this.color = color;
    }
}
