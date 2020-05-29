package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * A ZonedDateTime bean
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class DateTimeBean {
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(null);
    private final ObjectProperty<LocalTime> time = new SimpleObjectProperty<>(null);
    private final ObjectProperty<ZoneId> zone = new SimpleObjectProperty<>(null);

    /**
     * Constructor of DateTimeBean initializing the properties to the default date when
     *
     * @param when the first date to use
     */
    public DateTimeBean(ZonedDateTime when) {
        setZonedDateTime(when);
    }

    /**
     * Default constructor of DateTimeBean
     */
    public DateTimeBean() {
    }

    /**
     * Getter for the date
     * @return (localDate)
     */
    public LocalDate getDate() {
        return date.get();
    }

    /**
     * Getter for the date property
     * @return (ObjectProperty<LocalDate>)
     */
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    /**
     * Setter for the date
     * @param newDate (LocalDate) new date to be set
     */
    public void setDate(LocalDate newDate) {
        date.set(newDate);
    }

    /**
     * Getter for the time
     * @return (localTime)
     */
    public LocalTime getTime() {
        return time.get();
    }

    /**
     * Getter for the time property
     * @return (ObjectProperty<LocalTime>)
     */
    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    /**
     * Setter for the time
     * @param newTime (LocalTime) new time to be set
     */
    public void setTime(LocalTime newTime) {
        time.set(newTime);
    }

    /**
     * Getter for the zone
     * @return (ZoneId)
     */
    public ZoneId getZone() {
        return zone.get();
    }

    /**
     * Getter for the zone property
     * @return (ObjectProperty<ZoneId>)
     */
    public ObjectProperty<ZoneId> zoneProperty() {
        return zone;
    }

    /**
     * Setter for the zone
     * @param newZone (ZoneId) new zone to be set
     */
    public void setZone(ZoneId newZone) {
        zone.set(newZone);
    }

    /**
     * Getter for the zoned date time
     * @return (ZonedDateTime)
     */
    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.of(getDate(), getTime(), getZone());
    }

    /**
     * Setter for the zoned date time
     * @param newDate (ZonedDateTime) new zoned date time to be set
     */
    public void setZonedDateTime(ZonedDateTime newDate) {
        setDate(newDate.toLocalDate());
        setTime(newDate.toLocalTime());
        setZone(newDate.getZone());
    }
}
