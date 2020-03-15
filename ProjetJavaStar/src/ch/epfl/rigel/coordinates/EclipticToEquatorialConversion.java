package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Tools to convert from Ecliptic Coordinates to Equatorial Coordinates
 *
 * @author Ozan Güven (297076)
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private final double COS_OBLI;
    private final double SIN_OBLI;

    /**
     * Constructs the conversion system from Ecliptic to Equatorial coordinates
     * at the given date (when)
     *
     * @param when the date for which the conversion must hold
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        double eclipticObliquity = Polynomial.of(
                Angle.ofArcsec(0.00181),
                Angle.ofArcsec(-0.0006),
                Angle.ofArcsec(-46.815),
                Angle.ofDMS(23, 26, 21.45))
                .at(Epoch.J2000.julianCenturiesUntil(when));

        COS_OBLI = Math.cos(eclipticObliquity);
        SIN_OBLI = Math.sin(eclipticObliquity);
    }

    /**
     * Converts the Ecliptic Coordinates ecl to Equatorial Coordinates using the conversion system
     *
     * @param ecl the Ecliptic Coordinates that needs to be converted
     * @return the converted coordinates in Equatorial Coordinates
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {
        RightOpenInterval interval = RightOpenInterval.of(0, Angle.TAU);

        double alphaEqu = Math.atan2(Math.sin(ecl.lon()) * COS_OBLI - Math.tan(ecl.lat()) * SIN_OBLI, Math.cos(ecl.lon()));
        double deltaEqu = Math.asin(Math.sin(ecl.lat()) * COS_OBLI + Math.cos(ecl.lat()) * SIN_OBLI * Math.sin(ecl.lon()));

        return EquatorialCoordinates.of(interval.reduce(alphaEqu), deltaEqu);
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}
