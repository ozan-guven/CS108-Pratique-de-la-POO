package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MyEclipticCoordinatesTest {

    @Test
    void ofWorks() {
        EclipticCoordinates coord1 = EclipticCoordinates.of(3.120391, 1.235248);
        EclipticCoordinates coord2 = EclipticCoordinates.of(2.301934, 0.123891);

        assertEquals(3.120391, coord1.lon());
        assertEquals(1.235248, coord1.lat());
        assertEquals(2.301934, coord2.lon());
        assertEquals(0.123891, coord2.lat());
    }

    @Test
    void ofFails() {
        assertThrows(IllegalArgumentException.class, () ->{
            EclipticCoordinates coord1 = EclipticCoordinates.of(10, 1.235248);
            EclipticCoordinates coord2 = EclipticCoordinates.of(2.301934, Angle.TAU);
        });

    }

    @Test
    void lon() {
        EclipticCoordinates coord1 = EclipticCoordinates.of(3.120391, 1.235248);
        EclipticCoordinates coord2 = EclipticCoordinates.of(2.301934, 0.123891);

        assertEquals(3.120391, coord1.lon());
        assertEquals(2.301934, coord2.lon());
    }

    @Test
    void lonDeg() {
        EclipticCoordinates coord1 = EclipticCoordinates.of(3.120391, 1.235248);
        EclipticCoordinates coord2 = EclipticCoordinates.of(2.301934, 0.123891);

        assertEquals(178.78523473, coord1.lonDeg(), 1e-8);
        assertEquals(131.89110292, coord2.lonDeg(), 1e-8);
    }

    @Test
    void lat() {
        EclipticCoordinates coord1 = EclipticCoordinates.of(3.120391, 1.235248);
        EclipticCoordinates coord2 = EclipticCoordinates.of(2.301934, 0.123891);

        assertEquals(1.235248, coord1.lat(), 1e-8);
        assertEquals(0.123891, coord2.lat(), 1e-8);
    }

    @Test
    void latDeg() {
        EclipticCoordinates coord1 = EclipticCoordinates.of(3.120391, 1.235248);
        EclipticCoordinates coord2 = EclipticCoordinates.of(2.301934, 0.123891);

        assertEquals(70.774497052, coord1.latDeg(), 1e-8);
        assertEquals(7.0984314197, coord2.latDeg(), 1e-8);
    }

    @Test
    void testToString() {
        EclipticCoordinates coord1 = EclipticCoordinates.of(3.120391, 1.235248);
        EclipticCoordinates coord2 = EclipticCoordinates.of(2.301934, 0.123891);

        assertEquals("(λ=178.7852°, β=70.7745°)", coord1.toString());
        assertEquals("(λ=131.8911°, β=7.0984°)", coord2.toString());
    }
}