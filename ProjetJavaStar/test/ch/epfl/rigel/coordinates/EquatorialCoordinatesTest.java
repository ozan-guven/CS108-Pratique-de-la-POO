package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquatorialCoordinatesTest {

    @Test
    void ofThrowsExceptionOnNonValidAngles() {

        //ATTENTION : All the angles (if not specified otherwise) must be given in angles

        assertThrows(IllegalArgumentException.class, () -> {
            SphericalCoordinates coord = EquatorialCoordinates.of(Angle.TAU + 1, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            SphericalCoordinates coord = EquatorialCoordinates.of(0, Math.PI + 0.0001);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates coord = EquatorialCoordinates.of(-0.0002, -(Math.PI + 1.144));
        });
    }

    @Test
    void raReturnsCorrect() {
        EquatorialCoordinates coord = EquatorialCoordinates.of(0.785398, 0);
        assertEquals(0.785398, coord.ra());
    }

    @Test
    void raDegReturnsCorrect() {
        EquatorialCoordinates coord = EquatorialCoordinates.of(0.523599, 0.112);
        assertEquals(30.000012857, coord.raDeg(), 1e-8);
    }

    @Test
    void raHrReturnsCorrect() {
        EquatorialCoordinates coord = EquatorialCoordinates.of(6.02138591938, 0.112);
        assertEquals(22.999999999998, coord.raHr(), 1e-8);
    }

    @Test
    void decReturnsCorrect() {
        EquatorialCoordinates coord = EquatorialCoordinates.of(0.523599, -0.436332);
        assertEquals(-0.436332, coord.dec());
    }

    @Test
    void decDegReturnsCorrect() {
        EquatorialCoordinates coord = EquatorialCoordinates.of(0.785398, 1.48353);
        assertEquals(85.00000778, coord.decDeg(), 1e-8);
    }

    @Test
    void testToString() {
        EquatorialCoordinates coord = EquatorialCoordinates.of(0.392699081699, Math.PI/4);
        assertEquals("(ra=1.5000h, dec=45.0000°)", coord.toString());
    }
}