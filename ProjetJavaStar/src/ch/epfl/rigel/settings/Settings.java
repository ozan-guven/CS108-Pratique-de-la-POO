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

    private static final String SETTINGS_TEXT = "# drawAsterisms;showGrid;dayNight;currentCity;currentAccelerator\n";
    private static final int NBR_OF_PARAMETERS = 5;

    private final boolean wasRead;
    private final boolean drawAsterisms;
    private final boolean showGrid;
    private final boolean allowDayNightCycle;
    private final String selectedCity;
    private final NamedTimeAccelerator selectedAccelerator;


    private Settings(boolean wasRead, boolean drawAsterisms, boolean showGrid, boolean allowDayNightCycle, String selectedCity, NamedTimeAccelerator selectedAccelerator) {
        this.wasRead = wasRead;
        this.drawAsterisms = drawAsterisms;
        this.showGrid = showGrid;
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
        boolean showGrid = false;
        boolean allowDayNightCycle = false;
        String selectedCity = "EPFL";
        NamedTimeAccelerator selectedAccelerator = NamedTimeAccelerator.TIMES_300;
        String[] settings = new String[NBR_OF_PARAMETERS];
        try (BufferedReader stream = new BufferedReader(new InputStreamReader(new FileInputStream("settings.txt")))) {
            stream.lines()
                    .skip(1)
                    .map(l -> l.split(";"))
                    .forEachOrdered(l -> System.arraycopy(l, 0, settings, 0, NBR_OF_PARAMETERS));
            wasRead = true;
            drawAsterisms = Boolean.parseBoolean(settings[0]);
            showGrid = Boolean.parseBoolean(settings[1]);
            allowDayNightCycle = Boolean.parseBoolean(settings[2]);
            selectedCity = settings[3];
            selectedAccelerator = NamedTimeAccelerator.valueFromName(settings[4]);
        } catch (Exception e) {
            //If the file wasn't read, the default settings will be used
            wasRead = false;
        }

        return new Settings(wasRead, drawAsterisms, showGrid, allowDayNightCycle, selectedCity, selectedAccelerator);
    }

    /**
     * Writes the current setting in the settings.txt file
     * @param drawAsterisms (BooleanProperty)
     * @param showGrid (BooleanProperty)
     * @param dayNight (BooleanProperty)
     * @param currentCity (ObjectProperty<String>)
     * @param currentAccelerator (ObjectProperty<NamedTimeAccelerator>)
     */
    public static void writeSettings(BooleanProperty drawAsterisms, BooleanProperty showGrid, BooleanProperty dayNight, ObjectProperty<String> currentCity, ObjectProperty<NamedTimeAccelerator> currentAccelerator) {
        try (Writer w = new OutputStreamWriter(new FileOutputStream("settings.txt"))) {
            w.write(SETTINGS_TEXT);
            StringJoiner joiner = new StringJoiner(";", "", "");
            joiner.add(Boolean.toString(drawAsterisms.get()));
            joiner.add(Boolean.toString(showGrid.get()));
            joiner.add(Boolean.toString(dayNight.get()));
            joiner.add(currentCity.get());
            joiner.add(currentAccelerator.get().getName());
            w.write(joiner.toString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Gets the boolean telling if reading the file was successful
     *
     * @return true if the file was read successfully
     */
    public boolean wasRead() {
        return wasRead;
    }

    /**
     * The value to use for drawing asterisms when opening the program
     *
     * @return the value to use for drawing the asterisms
     */
    public boolean drawAsterisms() {
        return drawAsterisms;
    }

    /**
     * The value to use for drawing the grid when opening the program
     *
     * @return the value to use for drawing the grid
     */
    public boolean showGrid() {
        return showGrid;
    }

    /**
     * The value to use for enabling day/night cycle when opening the program
     *
     * @return the value to use for enabling day/night cycle
     */
    public boolean allowDayNightCycle() {
        return allowDayNightCycle;
    }

    /**
     * The value to use for the first city when opening the program
     *
     * @return the value to use for the first city
     */
    public String selectedCity() {
        return selectedCity;
    }

    /**
     * The value to use for the first time accelerator when opening the program
     *
     * @return the value to use for the first time accelerator
     */
    public NamedTimeAccelerator selectedAccelerator() {
        return selectedAccelerator;
    }
}
