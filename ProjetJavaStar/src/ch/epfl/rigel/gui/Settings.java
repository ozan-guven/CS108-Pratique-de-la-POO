package ch.epfl.rigel.gui;

import javafx.beans.property.BooleanProperty;

import java.io.*;
import java.util.StringJoiner;

/**
 * Class used to read and write the parameter choices of the user
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class Settings {

    private Settings() {
    }

    public static void readSettings() throws IOException {
        try(BufferedReader stream = new BufferedReader(new InputStreamReader(Settings.class.getResourceAsStream("settings.txt")))) {
            stream.lines()
                    .close();
        }
    }

    public static void writeSettings(BooleanProperty drawAsterisms, BooleanProperty dayNight) {
        try (Writer w = new OutputStreamWriter(new FileOutputStream("settings.txt"))) {
            w.write("# drawAsterisms, dayNight\n");
            StringJoiner joiner = new StringJoiner(", ", "", "");
            joiner.add(Boolean.toString(drawAsterisms.get()));
            joiner.add(Boolean.toString(dayNight.get()));
            w.write(joiner.toString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
