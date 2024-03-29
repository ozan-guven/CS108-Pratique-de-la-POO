package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Class used to convert from Ecliptic Coordinates to Equatorial Coordinates
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private static final Polynomial ECL_OBL_POLYNOMIAL = Polynomial.of(
            Angle.ofArcsec(0.00181),
            Angle.ofArcsec(-0.0006),
            Angle.ofArcsec(-46.815),
            Angle.ofDMS(23, 26, 21.45));

    private final double cosObli;
    private final double sinObli;

    /**
     * Constructs the conversion system from Ecliptic to Equatorial coordinates
     * at the given date (when)
     *
     * @param when the date for which the conversion must hold
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        double eclipticObliquity = ECL_OBL_POLYNOMIAL.at(Epoch.J2000.julianCenturiesUntil(when));

        cosObli = Math.cos(eclipticObliquity);
        sinObli = Math.sin(eclipticObliquity);
    }

    /**
     * Converts the Ecliptic Coordinates ecl to Equatorial Coordinates using the conversion system
     *
     * @param ecl the Ecliptic Coordinates that needs to be converted
     * @return the converted coordinates in Equatorial Coordinates
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {
        double sinLon = Math.sin(ecl.lon());
        double rightAscension = Math.atan2(sinLon * cosObli - Math.tan(ecl.lat()) * sinObli, Math.cos(ecl.lon()));
        double declination = Math.asin(Math.sin(ecl.lat()) * cosObli + Math.cos(ecl.lat()) * sinObli * sinLon);

        return EquatorialCoordinates.of(Angle.normalizePositive(rightAscension), declination);
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
