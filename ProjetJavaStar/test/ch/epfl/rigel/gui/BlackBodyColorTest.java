package ch.epfl.rigel.gui;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BlackBodyColorTest {

    @Test
    void colorForTemperature() {

        //**************************** TESTS DE LA CLASSE ****************************
        assertEquals(Color.web("#ffcc99"), BlackBodyColor.colorForTemperature(3798.1409));
        assertEquals(Color.web("#c8d9ff"), BlackBodyColor.colorForTemperature(10500d));

        assertThrows(IllegalArgumentException.class, () -> BlackBodyColor.colorForTemperature(40_000.00001d));

        assertEquals(Color.web("#ff3800"), BlackBodyColor.colorForTemperature(1000));
        assertEquals(Color.web("#ff8912"), BlackBodyColor.colorForTemperature(2000));
        assertEquals(Color.web("#ffdbba"), BlackBodyColor.colorForTemperature(4500));
        assertEquals(Color.web("#ccdbff"), BlackBodyColor.colorForTemperature(10000));
        assertEquals(Color.web("#9bbcff"), BlackBodyColor.colorForTemperature(40000));

        assertEquals(Color.web("#ffcc99"), BlackBodyColor.colorForTemperature(3798));
        assertEquals(Color.web("#ffcc99"), BlackBodyColor.colorForTemperature(3802));

        assertEquals(Color.web("#ff3800"), BlackBodyColor.colorForTemperature(1049));
        assertEquals(Color.web("#ff8912"), BlackBodyColor.colorForTemperature(1951));

        assertThrows(IllegalArgumentException.class, () -> BlackBodyColor.colorForTemperature(999));
        assertThrows(IllegalArgumentException.class, () -> BlackBodyColor.colorForTemperature(40001));
    }
}