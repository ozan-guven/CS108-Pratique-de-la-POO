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
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class BlackBodyColor {

    private static final String BBR_COLOR_CATALOGUE_NAME = "/bbr_color.txt";
    private static final Map<Integer, Color> KELVIN_RGB = initializeMap();

    private static final ClosedInterval INTERVAL = ClosedInterval.of(1000, 40000);

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
            stream.lines()
                    .filter(l -> l.charAt(0) != '#') //filters all lines with
                    .map(l -> l.trim().split("\\s+")) //trims the string and "\\s+" takes into account multiple spaces such as "   "
                    .filter(s -> !s[2].equals("2deg")) //filters all lines with 2deg
                    .forEachOrdered(s -> kelvinRGB.put(Integer.parseInt(s[0]), Color.web(s[s.length -1])));
            /*
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
            }*/
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
        int closestTemp = closestHundredMultiple(Preconditions.checkInInterval(INTERVAL, temperatureKelvin));

        return KELVIN_RGB.get(closestTemp);
    }

    /**
     * Gets the closet multiple of 100 for the given number
     *
     * @param number the number to be rounded
     * @return the closest multiple of 100
     */
    private static int closestHundredMultiple(double number) {
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

    //TODO : On pourrait créer une classe apart avec cette méthode
    public int closestIntMultipleTo(int multiple, double number) {
        double numberModulo = number % multiple;

        if (numberModulo == 0) {
            return (int) number;
        } else if (numberModulo >= multiple / 2d) {
            return ((int) (number + (multiple - numberModulo)));
        } else {
            return ((int) (number - numberModulo));
        }
    }
}
