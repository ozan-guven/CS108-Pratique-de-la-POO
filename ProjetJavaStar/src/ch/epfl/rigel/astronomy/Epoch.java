package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Enumeration that represents astronomical epochs and their tools
 *
 * @author Ozan Güven (297076)
 */
public enum Epoch {

    //Definition of the J2000 epoch : 1st January 2000 at 12:00 UTC
    J2000(ZonedDateTime.of(
            LocalDate.of(2000, Month.JANUARY, 1),
            LocalTime.of(12, 0),
            ZoneOffset.UTC)),

    //Definition of the J2010 epoch : 31st December 2009 (0th January 2010) at 00:00 UTC
    J2010(ZonedDateTime.of(
            LocalDate.of(2010, Month.JANUARY, 1).minusDays(1),
            LocalTime.of(0, 0),
            ZoneOffset.UTC));

    private ZonedDateTime epochDate;

    private final double MILLIS_PER_DAY = 1000 * 60 * 60 * 24; //Number of milliseconds in a day
    private final double MILLIS_PER_JULIAN_CENTURIES = 1000 * 60 * 60 * 24 * 365.25 * 100; //Number of milliseconds in a Julian century

    private Epoch(ZonedDateTime epochDate) {
        this.epochDate = epochDate;
    }

    //TODO : Je ne suis pas sur si l'on peut directement retourner la date comme ça ou si y'a même besoin de faire ça
    public ZonedDateTime getEpochDate() {
        return epochDate;
    }

    /**
     * Computes the number of days between the selected epoch (J2000 or J2010)
     * and the given date (ZonedDateTime)
     *
     * @param when the date to which the number of days is wanted (ZonedDateTime)
     * @return the number of days between when and the epoch
     */
    public double daysUntil(ZonedDateTime when) {
        double timeToWhen;

        //TODO : JE ne sais pas lequels des deux utiliser
        timeToWhen = epochDate.until(when, ChronoUnit.MILLIS);
        //timeToWhen = this.getEpochDate().until(when, ChronoUnit.MILLIS);

        return timeToWhen / MILLIS_PER_DAY;
    }

    /**
     * Computes the number of Julian centuries between the selected epoch (J2000 or J2010)
     * and the given date (ZonedDateTime)
     *
     * @param when the date to which the number of days is wanted (ZonedDateTime)
     * @return the number of days between when and the epoch
     */
    public double julianCenturiesUntil(ZonedDateTime when) {
        double timeToWhen;

        timeToWhen = epochDate.until(when, ChronoUnit.MILLIS);
        //timeToWhen = this.getEpochDate().until(when, ChronoUnit.MILLIS);

        return timeToWhen / MILLIS_PER_JULIAN_CENTURIES;
    }
}