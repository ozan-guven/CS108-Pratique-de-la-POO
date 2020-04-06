package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Tools to convert from Equatorial Coordinates to Horizontal Coordinates
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    private final double localSidereal; //The local sidereal time

    private final double cosLat;
    private final double sinLat;

    /**
     * Constructs the conversion system from Equatorial to Horizontal coordinates
     * for the given date when and the given place where
     *
     * @param when  the date for which the conversion must hold
     * @param where the place where the conversion must hold
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
        localSidereal = SiderealTime.local(when, where);

        cosLat = Math.cos(where.lat());
        sinLat = Math.sin(where.lat());
    }

    /**
     * Converts the Equatorial Coordinates to Horizontal Coordinates using the conversion system
     *
     * @param equ the Equatorial Coordinates that needs to be converted
     * @return the converted coordinates in Horizontal Coordinates
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {
        double hourAngle = localSidereal - equ.ra();

        double sinAltitude = (Math.sin(equ.dec()) * sinLat) + (Math.cos(equ.dec()) * cosLat * Math.cos(hourAngle));
        double altitude = Math.asin((Math.sin(equ.dec()) * sinLat) + (Math.cos(equ.dec()) * cosLat * Math.cos(hourAngle)));

        double azimuth = Math.atan2(-Math.cos(equ.dec()) * cosLat * Math.sin(hourAngle), Math.sin(equ.dec()) - (sinLat * sinAltitude));

        return HorizontalCoordinates.of(Angle.normalizePositive(azimuth), altitude);
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
