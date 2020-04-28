package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.List;

/**
 * Class representing a asterism (a collection of stars)
 *
 * @author Ozan Güven (297076)
 * @author Robin Goumaz (301420)
 */
public final class Asterism {
    private final List<Star> starList;

    /**
     * Constructor of the asterism
     *
     * @param stars list of the stars contained in the asterism
     */
    public Asterism(List<Star> stars) {
        Preconditions.checkArgument(!stars.isEmpty());

        starList = List.copyOf(stars);
    }

    /**
     * Getter for the star list
     *
     * @return List of the stars
     */
    public List<Star> stars() {
        return starList;
    }
}
