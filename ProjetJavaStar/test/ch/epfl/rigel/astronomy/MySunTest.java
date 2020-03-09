package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MySunTest {

    @Test
    void meanAnomaly() {
        Sun a = new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0), 0, 0);
        Sun b = new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0), 0, 1);
        Sun c = new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0), 0, 0.234f);
        Sun d = new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0), 0, -0.123f);
        Sun e = new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0), 0, 1203.123f);

        assertEquals(0, a.meanAnomaly());
        assertEquals(1, b.meanAnomaly());
        assertEquals(0.234, c.meanAnomaly());
        assertEquals(-0.123, d.meanAnomaly());
        assertEquals(1203.123, e.meanAnomaly());
    }

    @Test
    void magnitude(){
        Sun a = new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0), 0, 0);

        assertEquals(-26.7, a.magnitude());
    }

    @Test
    void name(){
        Sun a = new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0), 0, 0);

        assertEquals("Soleil", a.info());
    }


}