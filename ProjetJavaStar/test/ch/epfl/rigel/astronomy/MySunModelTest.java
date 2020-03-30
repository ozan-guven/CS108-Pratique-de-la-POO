package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class MySunModelTest {

    //******************** TESTS DE LA CLASSE *******************

    @Test
    void at() {
        assertEquals(5.9325494700300885,
                SunModel.SUN.at(27 + 31,
                        new EclipticToEquatorialConversion(
                                ZonedDateTime.of(
                                        LocalDate.of(2010,  Month.FEBRUARY, 27),
                                        LocalTime.of(0,0),
                                        ZoneOffset.UTC)))
                .equatorialPos().ra());

        assertEquals(8.3926828082978,
                SunModel.SUN.at(-2349,
                        new EclipticToEquatorialConversion(
                                ZonedDateTime.of(
                                        LocalDate.of(2003, Month.JULY, 27),
                                        LocalTime.of(0, 0, 0, 0),
                                        ZoneOffset.UTC)))
                        .equatorialPos().raHr(), 1e-13);

        assertEquals(19.35288373097352,
                SunModel.SUN.at(-2349,
                        new EclipticToEquatorialConversion(
                                ZonedDateTime.of(
                                        LocalDate.of(2003, Month.JULY, 27),
                                        LocalTime.of(0, 0, 0, 0),
                                        ZoneOffset.UTC)))
                        .equatorialPos().decDeg(), 1e-14);

        ZonedDateTime date = ZonedDateTime.of(
                LocalDate.of(1988, Month.JULY, 27),
                LocalTime.of(0, 0),
                ZoneOffset.UTC);

        double daySinceJ2010 = Epoch.J2010.daysUntil(date);

        assertEquals(Angle.ofDMS(0, 31, 30), (float)  SunModel.SUN.at(daySinceJ2010,
                new EclipticToEquatorialConversion(date)).angularSize(), 1e-6);

        assertEquals(3.528210, (float)  Angle.normalizePositive(SunModel.SUN.at(daySinceJ2010,
                new EclipticToEquatorialConversion(date)).meanAnomaly()), 1e-2);

        ZonedDateTime date1 = ZonedDateTime.of(
                LocalDate.of(2003, Month.JULY, 27),
                LocalTime.of(0, 0),
                ZoneOffset.UTC);

        assertEquals(Angle.ofDeg(201.159131), (float) Angle.normalizePositive(SunModel.SUN.at(-2349,
                new EclipticToEquatorialConversion(date1)).meanAnomaly()), 1e-5);
    }
}