package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MyEquatorialToHorizontalConversionTest {

    @Test
    void apply() {
        /*EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(
                ZonedDateTime.of(
                    LocalDate.of(2009, 6, 7),
                    LocalTime.of(12, 0, 0),
                    ZoneOffset.UTC),
                GeographicCoordinates.ofDeg(0, 0));

        EquatorialCoordinates coords = EquatorialCoordinates.of(0, 0);

        HorizontalCoordinates coords2 = conv.apply(coords);

        assertEquals(270, coords2.azDeg());
        assertEquals(44.06633859057, coords2.altDeg(), 1e-8);*/

        EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(
                ZonedDateTime.of(
                        LocalDate.of(2009, 6, 7),
                        LocalTime.of(12, 0, 0),
                        ZoneOffset.UTC),
                GeographicCoordinates.ofDeg(0, 52));

        EquatorialCoordinates toConvert = EquatorialCoordinates.of(0, Angle.ofDMS(23, 13, 10));
        HorizontalCoordinates newCoord = conv.apply(toConvert);

        //For those tests, use the value 1.53472618892 as the hourAngle
        //assertEquals( 19.334345, newCoord.altDeg(), 1e-6);
        //assertEquals(283.271027, newCoord.azDeg(), 1e-6);

    }


}