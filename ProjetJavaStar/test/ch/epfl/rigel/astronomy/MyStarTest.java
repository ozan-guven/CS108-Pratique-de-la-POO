package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyStarTest {

    @Test
    void hipparcosId() {

    }

    @Test
    void colorTemperature() {
        assertEquals(10515,
                new Star(24436,
                        "Rigel",
                        EquatorialCoordinates.of(0, 0),
                        0,
                        -0.03f)
                        .colorTemperature());

        assertEquals(3793,
                new Star(27989,
                        "Betelgeuse",
                        EquatorialCoordinates.of(0, 0),
                        0,
                        1.50f)
                        .colorTemperature());
    }
}