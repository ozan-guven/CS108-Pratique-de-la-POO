package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

/**
 * Interface representing a celestial object model,
 * tools to compute characteristics of an object at a given time.
 *
 * @param <O> type of the object modeled by the model
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public interface CelestialObjectModel<O> {

    /**
     * Returns the modeled object for the given number of days after epoch
     * J2010 (may be negative) and, using the given coordinate conversion
     * system, computes its equatorial coordinates given its ecliptic
     * coordinates.
     *
     * @param daysSinceJ2010                 the number of days since J2010
     * @param eclipticToEquatorialConversion the conversion that will be used
     * @return the modeled object
     */
    public abstract O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);

}
