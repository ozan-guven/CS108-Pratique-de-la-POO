package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyPlanetModelTest {

    @Test
    void at() {
        assertEquals(11.18715493470968, PlanetModel.JUPITER.at(-2231.0,
                new EclipticToEquatorialConversion(
                        ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
                .equatorialPos().raHr());

        assertEquals(6.35663550668575, PlanetModel.JUPITER.at(-2231.0,
                new EclipticToEquatorialConversion(
                        ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
                .equatorialPos().decDeg(), 1e-10);

        assertEquals(35.11141185362771, Angle.toDeg(PlanetModel.JUPITER.at(-2231.0,
                new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                        LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
                .angularSize()) * 3600);

        assertEquals(-1.9885659217834473, PlanetModel.JUPITER.at(-2231.0,
                new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                        LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
                .magnitude());

        assertEquals(16.8200745658971, PlanetModel.MERCURY.at(-2231.0,
                new EclipticToEquatorialConversion(
                        ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
                .equatorialPos().raHr(), 1e-12);

        assertEquals(-24.500872462861, PlanetModel.MERCURY.at(-2231.0,
                new EclipticToEquatorialConversion(
                        ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
                .equatorialPos().decDeg(), 1e-12);


        assertEquals("Vénus", PlanetModel.VENUS.at(-2231.0,
                new EclipticToEquatorialConversion(
                        ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
                .name());

        ZonedDateTime date = ZonedDateTime.of(
                LocalDate.of(2003, Month.NOVEMBER, 22),
                LocalTime.of(0, 0),
                ZoneOffset.UTC);

        assertEquals(Angle.ofArcsec(35.1), PlanetModel.JUPITER.at(Epoch.J2010.daysUntil(date),
                new EclipticToEquatorialConversion(date)).angularSize(), 1e-7);

        assertEquals(-1.9, PlanetModel.JUPITER.at(Epoch.J2010.daysUntil(date),
                new EclipticToEquatorialConversion(date)).magnitude(), 1e-1);
    }
}