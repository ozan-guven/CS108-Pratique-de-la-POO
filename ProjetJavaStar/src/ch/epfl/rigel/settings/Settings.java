package ch.epfl.rigel.settings;

import ch.epfl.rigel.gui.NamedTimeAccelerator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;

import java.io.*;
import java.util.StringJoiner;

/**
 * Class used to read and write the parameter choices of the user
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class Settings {

    private final boolean wasRead;
    private final boolean drawAsterisms;
    private final boolean allowDayNightCycle;
    private final String selectedCity;
    private final NamedTimeAccelerator selectedAccelerator;


    private Settings(boolean wasRead, boolean drawAsterisms, boolean allowDayNightCycle, String selectedCity, NamedTimeAccelerator selectedAccelerator) {
        this.wasRead = wasRead;
        this.drawAsterisms = drawAsterisms;
        this.allowDayNightCycle = allowDayNightCycle;
        this.selectedCity = selectedCity;
        this.selectedAccelerator = selectedAccelerator;
    }

    /**
     * Initiates all the settings and stores them.<br>
     * If the settings weren't  able to be read, wasRead returns false.
     *
     * @return the current settings stored in the file
     */
    public static Settings readSettings() {
        boolean wasRead;
        boolean drawAsterisms = true;
        boolean allowDayNightCycle = false;
        String selectedCity = "EPFL";
        NamedTimeAccelerator selectedAccelerator = NamedTimeAccelerator.TIMES_300;
        String[] settings = new String[4];
        try(BufferedReader stream = new BufferedReader(new InputStreamReader(new FileInputStream("settings.txt")))) {
            stream.lines()
                    .skip(1)
                    .map(l -> l.split(";"))
                    .forEachOrdered(l -> System.arraycopy(l, 0, settings, 0, 4));
            wasRead = true;
            drawAsterisms = Boolean.parseBoolean(settings[0]);
            allowDayNightCycle = Boolean.parseBoolean(settings[1]);
            selectedCity = settings[2];
            selectedAccelerator = NamedTimeAccelerator.valueFromName(settings[3]);
        } catch (IOException e) {
            wasRead = false;
        }

        return new Settings(wasRead, drawAsterisms, allowDayNightCycle, selectedCity, selectedAccelerator);
    }

    public static void writeSettings(BooleanProperty drawAsterisms, BooleanProperty dayNight, ObjectProperty<String> currentCity, ObjectProperty<NamedTimeAccelerator> currentAccelerator) {
        try (Writer w = new OutputStreamWriter(new FileOutputStream("settings.txt"))) {
            w.write("# drawAsterisms;dayNight;currentCity;currentAccelerator\n");
            StringJoiner joiner = new StringJoiner(";", "", "");
            joiner.add(Boolean.toString(drawAsterisms.get()));
            joiner.add(Boolean.toString(dayNight.get()));
            joiner.add(currentCity.get());
            joiner.add(currentAccelerator.get().getName());
            w.write(joiner.toString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean wasRead() {
        return wasRead;
    }

    public boolean drawAsterisms() {
        return drawAsterisms;
    }

    public boolean allowDayNightCycle() {
        return allowDayNightCycle;
    }

    public String selectedCity() {
        return selectedCity;
    }

    public NamedTimeAccelerator selectedAccelerator() {
        return selectedAccelerator;
    }
}
