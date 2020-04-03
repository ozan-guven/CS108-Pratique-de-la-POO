package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;

import java.time.ZonedDateTime;
import java.util.ArrayList;

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

    private Sun sun;
    private Moon moon;
    private ArrayList<Planet> planets;
    private ArrayList<Star> stars;

    public ObservedSky(ZonedDateTime time, GeographicCoordinates coordinates,
                       StereographicProjection projection, StarCatalogue catalogue) {
        this.time = time;
        this.coordinates = coordinates;
        this.projection = projection;
        this.catalogue = catalogue;

        double daysUntilJ2010 = Epoch.J2010.daysUntil(time);
        EclipticToEquatorialConversion conversion = new EclipticToEquatorialConversion(time);

        sun = SunModel.SUN.at(daysUntilJ2010, conversion);
        moon = MoonModel.MOON.at(daysUntilJ2010, conversion);

        planets = new ArrayList<>();
        for (PlanetModel planet : PlanetModel.values()) {
            if (planet != PlanetModel.EARTH) {
                planets.add(planet.at(daysUntilJ2010, conversion));
            }
        }
        //TODO : Et comment on sait la position des étoiles ?
    }
}
