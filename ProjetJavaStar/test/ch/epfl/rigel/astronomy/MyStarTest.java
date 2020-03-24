package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyStarTest {

    @Test
    void constructorThrowsExceptionsWhenNeeded() {
        assertThrows(NullPointerException.class, () -> {
            Star star = new Star(-5, null,
                    EquatorialCoordinates.of(0, 0), -10, (float) 1);
        });

        assertThrows(NullPointerException.class, () -> {
            Star star = new Star(-5, "Beluga",
                    null, -10, (float) 1);
        });
    }

    @Test
    void hipparcosIdThrowsExceptions() {
        //hippardcosId negative
        assertThrows(IllegalArgumentException.class, () -> {
            Star star = new Star(-5, "Beluga",
                    EquatorialCoordinates.of(0, 0), -10, (float) -0.1);
        });

        //colorIndex pas dans [-0.5, 5.5]
        assertThrows(IllegalArgumentException.class, () -> {
            Star star = new Star(0, "Beluga",
                    EquatorialCoordinates.of(0, 0), -10, (float) -0.5001);
        });

        //colorIndex pas dans [-0.5, 5.5]
        assertThrows(IllegalArgumentException.class, () -> {
            Star star = new Star(1, "Beluga",
                    EquatorialCoordinates.of(0, 0), -10, (float) 5.50001);
        });

        //Checks if no exceptions are thrown
        assertDoesNotThrow(() -> {
            Star star = new Star(0, "Beluga",
                    EquatorialCoordinates.of(0, 0), -10, (float) -0.500);
        });

        //Checks if no exceptions are thrown
        assertDoesNotThrow(() -> {
            Star star = new Star(1, "Beluga",
                    EquatorialCoordinates.of(0, 0), -10, (float) 5.500);
        });

        //Checks if no exceptions are thrown
        assertDoesNotThrow(() -> {
            Star star = new Star(1, "Beluga",
                    EquatorialCoordinates.of(0, 0), -10, (float) 5.25);
        });
    }

    //*************************** TEST DE LA CLASSE ****************************************

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