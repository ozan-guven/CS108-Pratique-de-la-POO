package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Tools to convert from Equatorial Coordinates to Horizontal Coordinates
 *
 * @author Ozan Güven (297076)
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    private double localSidereal; //The local sidereal time
    private double lat; //The latitude of the observer

    private double azimuth;
    private double heigth;

    private double cosLat;
    private double sinLat;

    /**
     * Constructs the conversion system from Equatorial to Horizontal coordinates
     * for the given date when and the given place where
     *
     * @param when the date for which the conversion must hold
     * @param where the place where the conversion must hold
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
        localSidereal = SiderealTime.local(when, where);
        lat = where.lat();

        cosLat = Math.cos(lat);
        sinLat = Math.sin(lat);
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

        double argsin = (Math.sin(equ.dec()) * sinLat) + (Math.cos(equ.dec()) * cosLat * Math.cos(hourAngle));
        heigth = Math.asin(argsin);

        //double argtan = (-Math.cos(equ.dec()) * Math.cos(lat) * Math.sin(hourAngle)) / (Math.sin(equ.dec()) - (Math.sin(lat) * Math.sin(altitude)));
        //azimuth = Math.atan2(argtan, 1);
        azimuth = Math.atan2(-Math.cos(equ.dec()) * cosLat * Math.sin(hourAngle), Math.sin(equ.dec()) - (sinLat * Math.sin(heigth)));

        return HorizontalCoordinates.of(azimuth, heigth);
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
