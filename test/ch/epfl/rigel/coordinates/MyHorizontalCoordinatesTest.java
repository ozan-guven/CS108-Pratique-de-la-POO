package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MyHorizontalCoordinatesTest {

    @Test
    void ofWorksOnNonTrivialCoords() {
        HorizontalCoordinates coord1 = HorizontalCoordinates.of(0, 0);
        assertEquals(0, coord1.lon());
        assertEquals(0, coord1.lat());

        HorizontalCoordinates coord2 = HorizontalCoordinates.of(4.13452, 1.08210);
        assertEquals(4.13452, coord2.lon(), 1e-8);
        assertEquals(1.08210, coord2.lat(), 1e-8);
    }

    @Test
    void ofFailsOnOutOfIntervalValues() {
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates coord1 = HorizontalCoordinates.of(Angle.TAU, 0);
            HorizontalCoordinates coord2 = HorizontalCoordinates.of(0, Angle.TAU / 4 + 0.0001);
        });
    }

    @Test
    void ofDegWorksOnNonTrivialCoords() {
        HorizontalCoordinates coord1 = HorizontalCoordinates.ofDeg(0, 0);
        assertEquals(0, coord1.lonDeg());
        assertEquals(0, coord1.latDeg());

        HorizontalCoordinates coord2 = HorizontalCoordinates.ofDeg(250.234, 78.327);
        assertEquals(250.234, coord2.azDeg(), 1e-8);
        assertEquals(78.327, coord2.altDeg(), 1e-8);
    }

    @Test
    void ofDegFailsOnOutOfIntervalValues() {
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates coord1 = HorizontalCoordinates.of(360, 0);
            HorizontalCoordinates coord2 = HorizontalCoordinates.of(0, 90.0001);
        });
    }

    @Test
    void azWorks() {
        HorizontalCoordinates coord1 = HorizontalCoordinates.of(0.12392, 1.0291);
        HorizontalCoordinates coord2 = HorizontalCoordinates.of(2.34926, 0);

        assertEquals(0.12392, coord1.az(), 1e-8);
        assertEquals(2.34926, coord2.az(), 1e-8);
    }

    @Test
    void azDeg() {
        HorizontalCoordinates coord1 = HorizontalCoordinates.of(0.12392, 1.0291);
        HorizontalCoordinates coord2 = HorizontalCoordinates.of(2.34926, 0);

        assertEquals(7.10009299, coord1.azDeg(), 1e-8);
        assertEquals(134.60268297, coord2.azDeg(), 1e-8);
    }

    @Test
    void azOctantName() {
        HorizontalCoordinates coord1 = HorizontalCoordinates.of(0.12392, 1.0291);
        HorizontalCoordinates coord2 = HorizontalCoordinates.of(2.34926, 0);
        HorizontalCoordinates coord3 = HorizontalCoordinates.of(3.9619, 1.4421);
        HorizontalCoordinates coord4 = HorizontalCoordinates.ofDeg(247.5, 20);
        HorizontalCoordinates coord5 = HorizontalCoordinates.ofDeg(157.5, 20);
        HorizontalCoordinates coord6 = HorizontalCoordinates.ofDeg(359.9999, 20);
        HorizontalCoordinates coord7 = HorizontalCoordinates.ofDeg(45, 20);
        HorizontalCoordinates coord8 = HorizontalCoordinates.ofDeg(317, 20);

        assertEquals("N", coord1.azOctantName("N", "E", "S", "W"));
        assertEquals("SE", coord2.azOctantName("N", "E", "S", "W"));
        assertEquals("SW", coord3.azOctantName("N", "E", "S", "W"));
        assertEquals("W", coord4.azOctantName("N", "E", "S", "W"));
        assertEquals("S", coord5.azOctantName("N", "E", "S", "W"));
        assertEquals("N", coord6.azOctantName("N", "E", "S", "W"));
        assertEquals("NE", coord7.azOctantName("N", "E", "S", "W"));
        assertEquals("NW", coord8.azOctantName("N", "E", "S", "W"));
    }

    @Test
    void alt() {
        HorizontalCoordinates coord1 = HorizontalCoordinates.of(0.12392, 1.02913);
        HorizontalCoordinates coord2 = HorizontalCoordinates.of(2.34926, 1.55345);

        assertEquals(1.02913, coord1.alt(), 1e-8);
        assertEquals(1.55345, coord2.alt(), 1e-8);
    }

    @Test
    void altDeg() {
        HorizontalCoordinates coord1 = HorizontalCoordinates.of(0.12392, 1.02913);
        HorizontalCoordinates coord2 = HorizontalCoordinates.of(2.34926, 1.55345);

        assertEquals(58.96480557, coord1.altDeg(), 1e-8);
        assertEquals(89.00612868, coord2.altDeg(), 1e-8);
    }

    @Test
    void angularDistanceTo() {
        HorizontalCoordinates coord1 = HorizontalCoordinates.of(0.12392, 1.02913);
        HorizontalCoordinates coord2 = HorizontalCoordinates.of(2.34926, 1.55345);

        assertEquals(0.55238103, coord1.angularDistanceTo(coord2), 1e-8);
    }

    @Test
    void testToString() {
        HorizontalCoordinates coord1 = HorizontalCoordinates.of(0.12392, 1.02913);
        HorizontalCoordinates coord2 = HorizontalCoordinates.of(2.34926, 1.55345);

        assertEquals("(az=7.1001°, alt=58.9648°)", coord1.toString());
        assertEquals("(az=134.6027°, alt=89.0061°)", coord2.toString());
    }
}