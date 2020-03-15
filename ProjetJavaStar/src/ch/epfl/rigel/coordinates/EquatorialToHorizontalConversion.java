package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Tools to convert from Equatorial Coordinates to Horizontal Coordinates
 *
 * @author Ozan Güven (297076)
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    private final double LOCAL_SIDERAL; //The local sidereal time

    private final double COS_LAT;
    private final double SIN_LAT;

    /**
     * Constructs the conversion system from Equatorial to Horizontal coordinates
     * for the given date when and the given place where
     *
     * @param when the date for which the conversion must hold
     * @param where the place where the conversion must hold
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
        LOCAL_SIDERAL = SiderealTime.local(when, where);

        COS_LAT = Math.cos(where.lat());
        SIN_LAT = Math.sin(where.lat());
    }

    /**
     * Converts the Equatorial Coordinates to Horizontal Coordinates using the conversion system
     *
     * @param equ the Equatorial Coordinates that needs to be converted
     * @return the converted coordinates in Horizontal Coordinates
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {
        double hourAngle = LOCAL_SIDERAL - equ.ra();

        double sinAltitude = (Math.sin(equ.dec()) * SIN_LAT) + (Math.cos(equ.dec()) * COS_LAT * Math.cos(hourAngle));
        double altitude = Math.asin((Math.sin(equ.dec()) * SIN_LAT) + (Math.cos(equ.dec()) * COS_LAT * Math.cos(hourAngle)));

        double azimuth = Math.atan2(-Math.cos(equ.dec()) * COS_LAT * Math.sin(hourAngle), Math.sin(equ.dec()) - (SIN_LAT * sinAltitude));

        RightOpenInterval interval = RightOpenInterval.of(0, Angle.TAU);

        return HorizontalCoordinates.of(interval.reduce(azimuth), altitude);
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
