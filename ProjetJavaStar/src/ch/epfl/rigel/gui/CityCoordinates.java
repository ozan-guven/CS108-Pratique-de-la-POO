package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

public final class CityCoordinates {
    private static final String CITY_CATALOGUE_NAME = "/worldcities.csv";
    public static final Map<String, GeographicCoordinates> CITY_MAP = initializeMap();

    private static Map<String, GeographicCoordinates> initializeMap() {
        Map<String, GeographicCoordinates> map = new HashMap<>();

        try (BufferedReader stream = new BufferedReader(
                new InputStreamReader(CityCoordinates.class.getResourceAsStream(CITY_CATALOGUE_NAME)))) {
            stream.lines()
                    .skip(1)
                    .map(lines -> lines.split(";"))
                    .forEachOrdered(l -> map.put(l[Column.city.ordinal()],
                            GeographicCoordinates.ofDeg(Double.parseDouble(l[Column.lng.ordinal()]),
                                    Double.parseDouble(l[Column.lat.ordinal()]))));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return map;
    }

    private enum Column {
        city, city_ascii, lat, lng, country, iso2, iso3, admin_name, capital, population, id
    }
}
