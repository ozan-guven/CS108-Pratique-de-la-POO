package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class MyMoonModelTest {

    //********************* TEST DE LA CLASSE *********************

    @Test
    void at() {
        assertEquals(14.211456457836,
                MoonModel.MOON.at(
                        -2313,
                        new EclipticToEquatorialConversion(
                                ZonedDateTime.of(
                                        LocalDate.of(2003,  Month.SEPTEMBER, 1),
                                        LocalTime.of(0,0),
                                        ZoneOffset.UTC)))
                        .equatorialPos().raHr());

        assertEquals(-0.20114171346019355,
                MoonModel.MOON.at(
                        -2313,
                        new EclipticToEquatorialConversion(
                                ZonedDateTime.of(
                                        LocalDate.of(2003,  Month.SEPTEMBER, 1),
                                        LocalTime.of(0,0),
                                        ZoneOffset.UTC)))
                        .equatorialPos().dec());

        assertEquals(0.009225908666849136,
                MoonModel.MOON.at(
                        Epoch.J2010.daysUntil(
                                ZonedDateTime.of(
                                        LocalDate.of(1979, 9, 1),
                                        LocalTime.of(0, 0),
                                        ZoneOffset.UTC)),
                        new EclipticToEquatorialConversion(
                                ZonedDateTime.of(
                                        LocalDate.of(1979, 9, 1),
                                        LocalTime.of(0, 0),
                                        ZoneOffset.UTC)))
                        .angularSize());

        assertEquals("Lune (22.5%)",
                MoonModel.MOON.at(
                        Epoch.J2010.daysUntil(
                                ZonedDateTime.of(
                                        LocalDate.of(2003, 9, 1),
                                        LocalTime.of(0, 0),
                                        ZoneOffset.UTC)),
                        new EclipticToEquatorialConversion(
                                ZonedDateTime.of(
                                        LocalDate.of(2003, 9, 1),
                                        LocalTime.of(0, 0),ZoneOffset.UTC)))
                        .info());
    }
}