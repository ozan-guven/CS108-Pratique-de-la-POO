package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that offers a method to get the color of
 * a black body given its temperature
 *
 * @author Ozan Güven (297076)
 */
public final class BlackBodyColor {

    private final static String BBR_COLOR_CATALOGUE_NAME = "/bbr_color.txt";
    private final static Map<Integer, Color> kelvinRGB = initializeMap();

    private BlackBodyColor() {
    }

    /**
     * Initializes the map
     *
     * @return the map
     */
    private static Map<Integer, Color> initializeMap() {
        HashMap<Integer, Color> kelvinRGB = new HashMap<>();

        try (BufferedReader stream = new BufferedReader(
                new InputStreamReader(BlackBodyColor.class.getResourceAsStream(BBR_COLOR_CATALOGUE_NAME)))) {
            String s = stream.readLine();

            while (s.charAt(0) == '#') {
                s = stream.readLine();
            }

            String[] strings;
            while (s.charAt(0) != '#') {
                strings = s.trim().split("\\s+"); //trims the string and "\\s+" takes into account multiple spaces such as "   "
                if (!strings[2].equals("2deg")) {
                    kelvinRGB.put(Integer.parseInt(strings[0]), Color.web(strings[strings.length - 1]));
                }
                s = stream.readLine();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return kelvinRGB;
    }

    /**
     * Gets the color temperature of a black body
     * given its temperature in Kelvin
     *
     * @param temperatureKelvin the temperature in Kelvin
     * @return the color of the black body
     * @throws IllegalArgumentException if the temperature is not in [1000 K, 40000 K]
     */
    public static Color colorForTemperature(double temperatureKelvin) {
        ClosedInterval interval = ClosedInterval.of(1000, 40000);
        Preconditions.checkInInterval(interval, temperatureKelvin);

        String rgb = "";
        int closestTemp = closetHundredMultiple(temperatureKelvin);

        return kelvinRGB.get(closestTemp);
    }

    /**
     * Gets the closet multiple of 100 for the given number
     *
     * @param number the number to be rounded
     * @return the closest multiple of 100
     */
    private static int closetHundredMultiple(double number) {
        int numberRound = (int) Math.floor(number);
        int numberModulo = numberRound % 100;

        if (numberModulo == 0) {
            return numberRound;
        } else if (numberModulo >= 50) {
            return numberRound + (100 - numberModulo);
        } else {
            return numberRound - numberModulo;
        }
    }
}
