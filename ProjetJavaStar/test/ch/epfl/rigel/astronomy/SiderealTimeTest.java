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

        assertEquals(8.679071, Angle.toHr(SiderealTime.greenwich(d)), 1e-6);
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