package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class SiderealTimeTest {

    @Test
    void greenwichWorksOnGivenExample() {
        ZonedDateTime d = ZonedDateTime.of(
                LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51, 670_000_000),
                ZoneId.of("-4"));
        ZonedDateTime d1 = ZonedDateTime.of(
                LocalDate.of(2001, Month.OCTOBER, 3),
                LocalTime.of(6, 30),
                ZoneOffset.UTC);
        ZonedDateTime d2 = ZonedDateTime.of(
                LocalDate.of(1994, Month.JUNE, 16),
                LocalTime.of(18, 0),
                ZoneOffset.UTC);

        assertEquals(8.679071, Angle.toHr(SiderealTime.greenwich(d)), 1e-6); //Precision of 10^-6
        assertEquals(7.3023135494, Angle.toHr(SiderealTime.greenwich(d1)), 1e-7); //Precision of 10^-7
        assertEquals(175.771113474402, Angle.toDeg(SiderealTime.greenwich(d2)), 1e-5);


        /*ZonedDateTime d1 = ZonedDateTime.of(
                LocalDate.of(2001,Month.JANUARY, 27),
                LocalTime.of(12,0),
                ZoneOffset.UTC);

        assertEquals(5.371129, SiderealTime.greenwich(d1), 1e-6);*/
    }

    @Test
    void localWorksOnGivenExample() {
        ZonedDateTime d = ZonedDateTime.of(
                LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51, 670_000_000),
                ZoneId.of("-4"));

        assertEquals(4.412404, Angle.toHr(SiderealTime.local(d, GeographicCoordinates.ofDeg(-64, 90))), 1e-6);
    }

}