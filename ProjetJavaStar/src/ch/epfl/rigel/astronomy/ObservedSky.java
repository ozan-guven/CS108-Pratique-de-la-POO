package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.ClosedInterval;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * Class representing a set of celestial objects projected
 * onto a plan with a stereographic projection
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class ObservedSky {

    //private final ZonedDateTime time;
    //private final GeographicCoordinates coordinates;
    //private final StereographicProjection projection;
    private final StarCatalogue catalogue;

    private final Sun sun;
    private final Moon moon;
    private final ArrayList<Planet> planets;
    private final List<Star> stars;

    private final CartesianCoordinates sunCoordinates;
    private final CartesianCoordinates moonCoordinates;
    private final double[] planetPositions = new double[14];
    private final double[] starsPositions;

    private Map<CelestialObject, CartesianCoordinates> mapOfAll;

    /**
     * Constructor of an observable sky at a given time and place
     *
     * @param time the zoned date/time to observe the sky
     * @param coordinates the position to observe the sky
     * @param projection the stereographic projection to use
     * @param catalogue the catalogue containing all the stars and asterisms to show
     */
    public ObservedSky(ZonedDateTime time, GeographicCoordinates coordinates,
                       StereographicProjection projection, StarCatalogue catalogue) {
        //this.time = time;
        //this.coordinates = coordinates;
        //this.projection = projection;
        this.catalogue = catalogue;
        this.stars = catalogue.stars();

        double daysUntilJ2010 = Epoch.J2010.daysUntil(time);
        EclipticToEquatorialConversion conversionToEqu = new EclipticToEquatorialConversion(time);
        EquatorialToHorizontalConversion conversionToHor = new EquatorialToHorizontalConversion(time, coordinates);

        mapOfAll = new HashMap<>();

        sun = SunModel.SUN.at(daysUntilJ2010, conversionToEqu);
        moon = MoonModel.MOON.at(daysUntilJ2010, conversionToEqu);

        sunCoordinates = projection.apply(conversionToHor.apply(sun.equatorialPos()));
        moonCoordinates = projection.apply(conversionToHor.apply(moon.equatorialPos()));

        mapOfAll.put(sun, sunCoordinates);
        mapOfAll.put(moon, moonCoordinates);

        planets = new ArrayList<>();
        int i = 0;
        for (PlanetModel planet : PlanetModel.ALL) {
            if (planet != PlanetModel.EARTH) {
                Planet newPlanet = planet.at(daysUntilJ2010, conversionToEqu);
                planets.add(newPlanet);

                CartesianCoordinates planetProjection = projection.apply(conversionToHor.apply(newPlanet.equatorialPos()));
                planetPositions[i++] = planetProjection.x();
                planetPositions[i++] = planetProjection.y();

                mapOfAll.put(newPlanet, planetProjection);
            }
        }

        starsPositions = new double[stars.size()*2];
        for (int j = 0; j < stars.size(); j++) {
            CartesianCoordinates starProjection = projection.apply(conversionToHor.apply(stars.get(j).equatorialPos()));
            starsPositions[j*2] = starProjection.x();
            starsPositions[j*2+1] = starProjection.y();

            mapOfAll.put(stars.get(j), starProjection);
        }

        i = 0;
        for (Star star : stars) {
            CartesianCoordinates starProjection = projection.apply(conversionToHor.apply(star.equatorialPos()));
            starsPositions[i++] = starProjection.x();
            starsPositions[i++] = starProjection.y();

            mapOfAll.put(star, starProjection);
        }
    }

    /**
     * Gets the sun computed at that time and seen from that position
     *
     * @return the sun
     */
    public Sun sun() {
        return sun;
    }

    /**
     * Gets the cartesian coordinates of the projection of the sun onto the plane
     *
     * @return the coordinates of the sun in the sky
     */
    public CartesianCoordinates sunPosition() {
        return sunCoordinates;
    }

    /**
     * Gets the moon computed at that time and seen from that position
     *
     * @return the moon
     */
    public Moon moon() {
        return moon;
    }

    /**
     * Gets the cartesian coordinates of the projection of the moon onto the plane
     *
     * @return the coordinates of the moon in the sky
     */
    public CartesianCoordinates moonPosition() {
        return moonCoordinates;
    }

    /**
     * Gets a list of all the planets (excepted the Earth)
     * computed at that time and seen from that position
     *
     * @return a list of the planets (excepted the Earth)
     */
    public List<Planet> planets() {
        return List.copyOf(planets);
    }

    /**
     * Gets an array containing the cartesian coordinate components of each planet (excepted the Earth)
     * in order beginning from Mercury and the x coordinate
     * ([Mercury.x(), Mercury.y(), Venus.x(), Venus.y(), Mars.x(), ...])
     *
     * @return an array containing the coordinate components of all the planets (excepted Earth)
     */
    public double[] planetPositions() {
        return planetPositions;
    }

    /**
     * Gets a list of all the stars contained in the observed sky
     * (Gets all the stars of the catalogue)
     *
     * @return a list of all the stars in the observed sky
     */
    public List<Star> stars() {
        return List.copyOf(stars);
    }

    /**
     * Gets an array containing the cartesian coordinate components of each star
     * contained in the catalogue. Starting with the first star of the catalogue
     * <p>(For example [Star0.x(), Star0.y(), Star1.x(), ...])</p>
     *
     * @return an array containing the coordinate components
     * of all the stars in the catalogue
     */
    public double[] starPositions() {
          return starsPositions;
    }

    /**
     * Gets a set containing all the asterisms of the catalogue
     *
     * @return a set containing all the asterisms of the catalogue
     * @see StarCatalogue#asterisms()
     */
    public Set<Asterism> asterisms() {
        return Set.copyOf(catalogue.asterisms());
    }

    /**
     * Gets the list of the star indexes contained in the catalogue
     * for the given asterism
     *
     * @param asterism the asterism to have its star indexes
     * @return a list containing the indexes of the stars
     * @see StarCatalogue#asterismIndices(Asterism)
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        return List.copyOf(catalogue.asterismIndices(asterism));
    }

    /**
     * Gets the closest Celestial object on the projection for the given
     * cartesian coordinates and given a maximum distance
     *
     * @param coordinates the coordinates to search the closest object from
     * @param maxDistance the maximum distance to search the object from the coordinate
     * @return the closest object to the given point
     */
    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates coordinates, double maxDistance) {
        double currentDistance = coordinates.distanceToSquared(sunCoordinates); //Initialises a first distance
        double nextDistance;
        CelestialObject nearestObject = null;
        CartesianCoordinates coordOfObject;
        ClosedInterval intervalX = ClosedInterval.of(coordinates.x() - maxDistance, coordinates.x() + maxDistance);
        ClosedInterval intervalY = ClosedInterval.of(coordinates.y() - maxDistance, coordinates.y() + maxDistance);

        for (CelestialObject object : mapOfAll.keySet()) {
            coordOfObject = mapOfAll.get(object);
            /*In order
            Checks if the coordinates are in the given square with the two intervals (not to compute the distance all the times)
            Computes and checks if the distance is in the given maxDistance
            Checks if the new distance is smaller or equal to the current one*/
            if (coordOfObject.inPartOfPlane(intervalX, intervalY)
                    && (nextDistance = coordOfObject.distanceToSquared(coordinates)) <= maxDistance * maxDistance
                    && (currentDistance >= nextDistance)) {
                currentDistance = nextDistance;
                nearestObject = object;
            }
        }
        return nearestObject == null ? Optional.empty() : Optional.of(nearestObject);
    }
}
