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
        double hrsFromZeroToWhen = whenInGreenwichAtZero.until(whenInGreenwich, ChronoUnit.MILLIS) / (1000.0 * 60.0 * 60.0);

        //double sidereal0 = (julCentToWhenZero * ((0.000025862 * julCentToWhenZero) + 2400.051336)) + 6.697374558;
        double sidereal0 = Polynomial.of(0.000025862, 2400.051336, 6.697374558).at(julCentToWhenZero);
        double sidereal1 = 1.002737909 * hrsFromZeroToWhen;

        double siderealG = sidereal0 + sidereal1; //Sidereal time in Greenwich in hours

        return Angle.normalizePositive(Angle.ofHr(siderealG));
    }

    /**
     * Gives the sidereal time (in radians) for the given place at the given date/time couple (ZonedDateTime)
     *
     * @param when  the date for which the sidereal time is wanted
     * @param where the place for which the sidereal time is wanted
     * @return the sidereal time (in radians) at the date when for the given place where
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where) {

        double siderealL = greenwich(when) + where.lon(); //In radians

        return Angle.normalizePositive(siderealL);
    }

}
