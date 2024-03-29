package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SiderealTimeTestClass {
    @Test
    void greenwichTest() {
        assertEquals(Angle.ofHr(4.668119327), SiderealTime.greenwich(ZonedDateTime.of(of(1980, Month.APRIL, 22), LocalTime.of(14, 36, 51, (int) 6.7e8), ZoneOffset.UTC)), 1e-6);
    }
}