package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import static ch.epfl.rigel.astronomy.Epoch.J2000;
import static ch.epfl.rigel.math.Angle.TAU;

/**
 * Tools to compute sidereal time of a given place at a given date/time couple
 *
 * @author Ozan Güven (297076)
 */
public final class SiderealTime {

    private SiderealTime() {
    }

    /**
     * Gives the Greenwich sidereal time (in radians) for the given date/time couple (ZonedDateTime)
     *
     * @param when the date for which the Greenwich sidereal time wants to be known
     * @return the greenwich sidereal time (in radians) at the date when
     */
    public static double greenwich(ZonedDateTime when) {
        RightOpenInterval interval = RightOpenInterval.of(0, TAU);
        ZonedDateTime whenInGreenwich = when.withZoneSameInstant(ZoneOffset.UTC); //Converting when to UTC
        ZonedDateTime whenInGreenwichAtZero = whenInGreenwich.truncatedTo(ChronoUnit.DAYS); //Truncates UTC when at 00:00

        double julCentToWhenZero = Epoch.J2000.julianCenturiesUntil(whenInGreenwichAtZero);
        double hrsFromZeroToWhen = whenInGreenwichAtZero.until(whenInGreenwich, ChronoUnit.MILLIS) / (1000.0 * 60.0 * 60.0);

        double sidereal0 = (julCentToWhenZero * ((0.000025862 * julCentToWhenZero) + 2400.051336)) + 6.697374558;
        double sidereal1 = 1.002737909 * hrsFromZeroToWhen;

        double siderealG = sidereal0 + sidereal1; //Sidereal time in Greenwich in hours

        return interval.reduce(Angle.ofHr(siderealG));
    }

    /**
     * Gives the sidereal time (in radians) for the given place at the given date/time couple (ZonedDateTime)
     *
     * @param when  the date for which the sidereal time is wanted
     * @param where the place for which the sidereal time is wanted
     * @return the sidereal time (in radians) at the date when for the given place where
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where) {
        RightOpenInterval interval = RightOpenInterval.of(0, TAU);

        double siderealL = Angle.toHr(greenwich(when)) + Angle.toHr(where.lon());

        return interval.reduce(Angle.ofHr(siderealL));
    }

}