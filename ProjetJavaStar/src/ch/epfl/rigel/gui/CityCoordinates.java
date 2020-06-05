package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Non instantiatable class containing a map of major cities with their geographic coordinates
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class CityCoordinates {

    /**
     * Map containing city names with their corresponding geographic coordinates
     */
    private static final Map<String, GeographicCoordinates> CITY_MAP = initializeMap();

    private static final String CITY_CATALOGUE_NAME = "/worldcities.csv";
    private static final int MIN_POPULATION = 120_000; //2821 such cities

    private CityCoordinates() {
    }

    /**
     * Initiates the map
     *
     * @return the map
     */
    private static Map<String, GeographicCoordinates> initializeMap() {
        Map<String, GeographicCoordinates> map = new HashMap<>();
        try (BufferedReader stream = new BufferedReader(
                new InputStreamReader(CityCoordinates.class.getResourceAsStream(CITY_CATALOGUE_NAME)))) {
            stream.lines()
                    .skip(1)
                    .map(lines -> lines.split(";"))
                    .forEachOrdered(l -> {
                        if (!l[Column.population.ordinal()].isBlank() //About 11_000 cities have no population information
                                && toDouble(l[Column.population.ordinal()]) >= MIN_POPULATION)
                            map.put(l[Column.city.ordinal()],
                                    GeographicCoordinates.ofDeg(
                                            toDouble(l[Column.lng.ordinal()]),
                                            toDouble(l[Column.lat.ordinal()])));
                    });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        map.put("EPFL", GeographicCoordinates.ofDeg(6.57, 46.52));

        return map;
    }

    /**
     * Function used to convert a string with coma format double to point format double
     *
     * @param number the string to convert
     * @return the converted string to double
     */
    private static double toDouble(String number) {
        return Double.parseDouble(number.replace(",", "."));
    }

    /**
     * Gets the map containing city names with their corresponding geographic coordinates
     *
     * @return map of coordinates
     */
    public static Map<String, GeographicCoordinates> getCityMap() {
        return Collections.unmodifiableMap(CITY_MAP);
    }

    private enum Column {
        city, city_ascii, lat, lng, country, iso2, iso3, admin_name, capital, population, id
    }
}
