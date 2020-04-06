package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MySunTest {

    @Test
    void meanAnomaly() {
        Sun a = new Sun(EclipticCoordinates.of(0, 0), EquatorialCoordinates.of(0, 0), 0, 0);
        Sun b = new Sun(EclipticCoordinates.of(0, 0), EquatorialCoordinates.of(0, 0), 0, 1);
        Sun c = new Sun(EclipticCoordinates.of(0, 0), EquatorialCoordinates.of(0, 0), 0, 0.234f);
        Sun d = new Sun(EclipticCoordinates.of(0, 0), EquatorialCoordinates.of(0, 0), 0, -0.123f);
        Sun e = new Sun(EclipticCoordinates.of(0, 0), EquatorialCoordinates.of(0, 0), 0, 1203.123f);

        assertEquals(0, a.meanAnomaly());
        assertEquals(1, b.meanAnomaly());
        assertEquals(0.234f, c.meanAnomaly());
        assertEquals(-0.123f, d.meanAnomaly());
        assertEquals(1203.123f, e.meanAnomaly());
    }

    @Test
    void magnitude() {
        Sun a = new Sun(EclipticCoordinates.of(0, 0), EquatorialCoordinates.of(0, 0), 0, 0);

        assertEquals(-26.7f, a.magnitude());
    }

    @Test
    void name() {
        Sun a = new Sun(EclipticCoordinates.of(0, 0), EquatorialCoordinates.of(0, 0), 0, 0);

        assertEquals("Soleil", a.info());
    }


}