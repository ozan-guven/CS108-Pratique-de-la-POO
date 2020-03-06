package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * Main class that represents celestial objects
 *
 * @author Ozan Güven (297076)
 */
public abstract class CelestialObject {

    //TODO Je mets final à tout mais je sais pas si c'est utile
    private final String name;
    private final EquatorialCoordinates equatorialPos;
    private final float angularSize;
    private final float magnitude;

    /**
     * Constructor of a celestial object
     *
     * @param name          the name of the object
     * @param equatorialPos the equatorial position of the object
     * @param angularSize   the angular size of the object
     * @param magnitude     the magnitude of the object
     * @throws IllegalArgumentException if the angularSize is negative
     * @throws NullPointerException     if the name is null
     * @throws NullPointerException     if the equatorialPos is null
     */
    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        Preconditions.checkArgument(angularSize >= 0);

        //TODO : Doit-on faire des copies défesives ?
        this.name = Objects.requireNonNull(name);
        this.equatorialPos = Objects.requireNonNull(equatorialPos);
        this.angularSize = angularSize;
        this.magnitude = magnitude;
    }

    /**
     * Gets the name of the celestial object
     *
     * @return the name of the object
     */
    public String name() {
        return name;
    }

    //TODO : On doit faire un typpage de float a double ?

    /**
     * Gets the angular size of the celestial object
     *
     * @return the angular size
     */
    public double angularSize() {
        return angularSize;
    }

    /**
     * Gets the magnitude of the celestial object
     *
     * @return the magnitude of the object
     */
    public double magnitude() {
        return magnitude;
    }

    /**
     * Gets the equatorial coordinates of the celestial object
     *
     * @return the equatorial coordinates of the object
     */
    public EquatorialCoordinates equatorialPos() {
        return equatorialPos; //TODO : Copie défensive ?
    }

    /**
     * Short informative text about the celestial object
     *
     * @return a short informative text about the object
     */
    public String info() {
        return name();
    }

    @Override
    public String toString() {
        return info();
    }
}
