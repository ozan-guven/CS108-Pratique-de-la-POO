package ch.epfl.rigel.coordinates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class GeographicCoordinatesTest {

    @Test
    void ofDegThrowsExceptionOnNonValidAngles() {
        assertThrows(IllegalArgumentException.class, () -> {
            GeographicCoordinates coord = GeographicCoordinates.ofDeg(-181.241, 45);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            GeographicCoordinates coord = GeographicCoordinates.ofDeg(-180, 110);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            GeographicCoordinates coord = GeographicCoordinates.ofDeg(180, -110);
        });
    }

    @Test
    void isValidLonDegWorksOnValidAndNonValidLon() {
        assertTrue(GeographicCoordinates.isValidLonDeg(67.312));
        assertFalse(GeographicCoordinates.isValidLonDeg(180));
        assertFalse(GeographicCoordinates.isValidLonDeg(-182.35));
    }

    @Test
    void isValidLatDegWorksOnValidAndNonValidLat() {
        assertTrue(GeographicCoordinates.isValidLatDeg(89));
        assertFalse(GeographicCoordinates.isValidLatDeg(-91));
    }

    @Test
    void lonReturnsCorrect() {
        SphericalCoordinates coord = GeographicCoordinates.ofDeg(31.124, 47.824);
        assertEquals(0.543216276, coord.lon(), 1e-8);
    }

    @Test
    void lonDegReturnsCorrect() {
        SphericalCoordinates coord = GeographicCoordinates.ofDeg(31.124, 47.824);
        assertEquals(31.124, coord.lonDeg());
    }

    @Test
    void latReturnsCorrect() {
        SphericalCoordinates coord = GeographicCoordinates.ofDeg(124.124, 67.8543);
        assertEquals(1.169370599, coord.lat(), 1e-8);
    }

    @Test
    void latDegReturnsCorrect() {
        SphericalCoordinates coord = GeographicCoordinates.ofDeg(124.124, 67.8543);
        assertEquals(67.8543, coord.latDeg());
    }

    @Test
    void toStringWorks() {
        SphericalCoordinates coord = GeographicCoordinates.ofDeg(6.57, 46.52);
        System.out.println(coord.toString());
        assertEquals("(lon=6.5700°, lat=46.5200°)", coord.toString());
    }

}