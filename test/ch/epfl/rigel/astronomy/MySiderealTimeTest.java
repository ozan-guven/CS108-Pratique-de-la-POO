package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static java.time.ZonedDateTime.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MySiderealTimeTest {

    @Test
    void greenwichWorksOnGivenExample() {
        ZonedDateTime d = of(
                LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51, 670_000_000),
                ZoneId.of("-4"));
        ZonedDateTime d1 = of(
                LocalDate.of(2001, Month.OCTOBER, 3),
                LocalTime.of(6, 30),
                ZoneOffset.UTC);
        ZonedDateTime d2 = of(
                LocalDate.of(1994, Month.JUNE, 16),
                LocalTime.of(18, 0),
                ZoneOffset.UTC);
        ZonedDateTime d3 = of(
                LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0),
                ZoneOffset.UTC);

        assertEquals(8.679071, Angle.toHr(SiderealTime.greenwich(d)), 1e-6); //Precision of 10^-6
        assertEquals(7.3023135494, Angle.toHr(SiderealTime.greenwich(d1)), 1e-7); //Precision of 10^-7
        //assertEquals(175.771113474402, Angle.toDeg(SiderealTime.greenwich(d2)), 1e-5);
        assertEquals(18.697374558, Angle.toHr(SiderealTime.greenwich(d3)), 1e-7); //Precision of 10^-7

        /*ZonedDateTime d1 = ZonedDateTime.of(
                LocalDate.of(2001,Month.JANUARY, 27),
                LocalTime.of(12,0),
                ZoneOffset.UTC);

        assertEquals(5.371129, SiderealTime.greenwich(d1), 1e-6);*/

        assertEquals(1.2220619247737088, SiderealTime.greenwich(of(1980, 4, 22, 14, 36, 51, 67, ZoneId.of("UTC"))), 1e-10);
        assertEquals(1.2221107819499082, SiderealTime.greenwich(ZonedDateTime.of(1980, 4, 22, 14, 36, 51, (int) 67e7, ZoneOffset.UTC)), 1e-10);
        assertEquals(5.355270290366605, SiderealTime.greenwich(of(2001, 1, 27, 12, 0, 0, 0, ZoneId.of("UTC"))), 1e-10);
        assertEquals(2.9257399567031235, SiderealTime.greenwich(of(2004, 9, 23, 11, 0, 0, 0, ZoneId.of("UTC"))), 1e-10);

    }

    @Test
    void localWorksOnGivenExample() {
        ZonedDateTime d = of(
                LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51, 670_000_000),
                ZoneId.of("-4"));

        assertEquals(4.412404, Angle.toHr(SiderealTime.local(d, GeographicCoordinates.ofDeg(-64, 90))), 1e-6);

        assertEquals(1.74570958832716, SiderealTime.local(
                ZonedDateTime.of(
                        LocalDate.of(1980, Month.APRIL, 22),
                        LocalTime.of(14, 36, 51, 670_000_000),
                        ZoneOffset.UTC
                ), GeographicCoordinates.ofDeg(30, 45)), 1e-7);
    }

}