package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Tools to convert from Ecliptic Coordinates to Equatorial Coordinates
 *
 * @author Ozan Güven (297076)
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private double julianCentToJ2000;
    private double eclipticObliquity;

    private double alphaEqu;
    private double deltaEqu;

    /**
     * Constructs the conversion system from Ecliptic to Equatorial coordinates
     * at the given date (when)
     *
     * @param when the date for which the conversion must hold
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        julianCentToJ2000 = Epoch.J2000.julianCenturiesUntil(when);
        eclipticObliquity = Polynomial.of(
                Angle.ofArcsec(0.00181),
                Angle.ofArcsec(-0.0006),
                Angle.ofArcsec(-46.815),
                Angle.ofDMS(23, 26, 21.45))
                .at(julianCentToJ2000);
    }

    /**
     * Converts the Ecliptic Coordinates ecl to Equatorial Coordinates using the conversion system
     *
     * @param ecl the Ecliptic Coordinates that needs to be converted
     * @return the converted coordinates in Equatorial Coordinates
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {
        double argtan = (Math.sin(ecl.lon()) * Math.cos(eclipticObliquity) - Math.tan(ecl.lat()) * Math.sin(eclipticObliquity)) / Math.cos(ecl.lon());
        double argsin = Math.sin(ecl.lat()) * Math.cos(eclipticObliquity) + Math.cos(ecl.lat()) * Math.sin(eclipticObliquity) * Math.sin(ecl.lon());

        alphaEqu = Math.atan2(argtan, 1);
        deltaEqu = Math.asin(argsin);

        return EquatorialCoordinates.of(alphaEqu, deltaEqu);
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
