package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyMoonTest {

    @Test
    void info() {
        Moon a = new Moon(EquatorialCoordinates.of(0,0), 1, 1, 0.3f);
        Moon b = new Moon(EquatorialCoordinates.of(0,0), 1, 1, 0.5f);
        Moon c = new Moon(EquatorialCoordinates.of(0,0), 1, 1, 1f);
        Moon d = new Moon(EquatorialCoordinates.of(0,0), 1, 1, 0.003f);
        Moon e = new Moon(EquatorialCoordinates.of(0,0), 1, 1, 0.125f);

        assertEquals("Lune (30.0%)", a.info());
        assertEquals("Lune (50.0%)", b.info());
        assertEquals("Lune (100.0%)", c.info());
        assertEquals("Lune (0.3%)", d.info());
        assertEquals("Lune (12.5%)", e.info());
    }

    @Test
    void phase(){
        assertThrows(IllegalArgumentException.class, () -> {
            Moon a = new Moon(EquatorialCoordinates.of(0,0), 1, 1, -0.3f);
            Moon b = new Moon(EquatorialCoordinates.of(0,0), 1, 1, 1.3f);
        });
    }

    @Test
    void name(){
        Moon a = new Moon(EquatorialCoordinates.of(0,0), 1, 1, 0.3f);

        assertEquals("Lune", a.name());
    }

    @Test
    void angularSize(){
        Moon a = new Moon(EquatorialCoordinates.of(0,0), 0, 1, 0.3f);
        Moon b = new Moon(EquatorialCoordinates.of(0,0), 1, 1, 0.5f);
        Moon c = new Moon(EquatorialCoordinates.of(0,0), 10, 1, 1f);
        Moon d = new Moon(EquatorialCoordinates.of(0,0), 235.345f, 1, 0.003f);
        Moon e = new Moon(EquatorialCoordinates.of(0,0), 0.0000012f, 1, 0.125f);

        assertEquals(0, a.angularSize());
        assertEquals(1, b.angularSize());
        assertEquals(10, c.angularSize());
        assertEquals(235.345f, d.angularSize());
        assertEquals(0.0000012f, e.angularSize());
    }

    @Test
    void magnitude(){
        Moon a = new Moon(EquatorialCoordinates.of(0,0), 0.4f, 0, 0.3f);
        Moon b = new Moon(EquatorialCoordinates.of(0,0), 1, -121, 0.5f);
        Moon c = new Moon(EquatorialCoordinates.of(0,0), 10, 11.23409f, 1f);
        Moon d = new Moon(EquatorialCoordinates.of(0,0), 235.345f, -12.845f, 0.003f);
        Moon e = new Moon(EquatorialCoordinates.of(0,0), 0.0000012f, 0.23847289f, 0.125f);

        assertEquals(0, a.magnitude());
        assertEquals(-121, b.magnitude());
        assertEquals(11.23409f, c.magnitude());
        assertEquals(-12.845f, d.magnitude());
        assertEquals(0.23847289f, e.magnitude());
    }

    @Test
    void error(){
        assertThrows(IllegalArgumentException.class, () -> {
            Moon a = new Moon(EquatorialCoordinates.of(0,0), -0.00000000123f, 0, 0.3f);
        });
    }
}