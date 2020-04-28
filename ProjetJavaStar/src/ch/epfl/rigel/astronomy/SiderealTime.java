package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Tools to compute sidereal time of a given place at a given date/time couple
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class SiderealTime {

    private static final double MILLIS_PER_HOUR = 1000.0 * 60.0 * 60.0;
    private static final Polynomial SIDEREAL_POLY = Polynomial.of(0.000025862, 2400.051336, 6.697374558);
    private static final double SIDEREAL_CONSTANT = 1.002737909;

    private SiderealTime() {
    }

    /**
     * Gives the Greenwich sidereal time (in radians) for the given date/time couple (ZonedDateTime)
     *
     * @param when the date for which the Greenwich sidereal time wants to be known
     * @return the greenwich sidereal time (in radians) at the date when
     */
    public static double greenwich(ZonedDateTime when) {
        ZonedDateTime whenInGreenwich = when.withZoneSameInstant(ZoneOffset.UTC); //Converting when to UTC
        ZonedDateTime whenInGreenwichAtZero = whenInGreenwich.truncatedTo(ChronoUnit.DAYS); //Truncates UTC when at 00:00

        double julCentToWhenZero = Epoch.J2000.julianCenturiesUntil(whenInGreenwichAtZero);
        double hrsFromZeroToWhen = whenInGreenwichAtZero.until(whenInGreenwich, ChronoUnit.MILLIS) / MILLIS_PER_HOUR;

        double sidereal0 = SIDEREAL_POLY.at(julCentToWhenZero);
        double sidereal1 = SIDEREAL_CONSTANT * hrsFromZeroToWhen;

        return Angle.normalizePositive(Angle.ofHr(sidereal0 + sidereal1)); //Where sidereal0 + sidereal1 is the sidereal time in Greenwich in hours
    }

    /**
     * Gives the sidereal time (in radians) for the given place at the given date/time couple (ZonedDateTime)
     *
     * @param when  the date for which the sidereal time is wanted
     * @param where the place for which the sidereal time is wanted
     * @return the sidereal time (in radians) at the date when for the given place where
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where) {
        return Angle.normalizePositive(greenwich(when) + where.lon());
    }

}
