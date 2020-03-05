package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class EclipticToEquatorialConversionTest {

    @Test
    void apply() {
        ZonedDateTime d = ZonedDateTime.of(
                LocalDate.of(2009, Month.JULY, 6),
                LocalTime.of(12, 0),
                ZoneOffset.UTC);

        EclipticToEquatorialConversion eclToEqu = new EclipticToEquatorialConversion(d);

        EclipticCoordinates toConvert = EclipticCoordinates.of(Angle.ofDeg(139.686111), Angle.ofDeg(4.875278));

        EquatorialCoordinates newCoord = eclToEqu.apply(toConvert);

        assertEquals(Angle.ofHr(9.5814777777778), newCoord.ra(), 1e-6);
        assertEquals(Angle.ofDMS(19,32,6.01), newCoord.dec(), 1e-7);
    }
}