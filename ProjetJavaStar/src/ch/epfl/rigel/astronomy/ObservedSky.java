package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;

import java.time.ZonedDateTime;

/**
 * Class representing a set of celestial objects projected
 * onto a plan with a stereographic projection
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class ObservedSky {

    private final ZonedDateTime time;
    private final GeographicCoordinates coordinates;
    private final StereographicProjection projection;
    private final StarCatalogue catalogue;

    public ObservedSky(ZonedDateTime time, GeographicCoordinates coordinates,
                       StereographicProjection projection, StarCatalogue catalogue) {
        this.time = time;
        this.coordinates = coordinates;
        this.projection = projection;
        this.catalogue = catalogue;
    }
}
