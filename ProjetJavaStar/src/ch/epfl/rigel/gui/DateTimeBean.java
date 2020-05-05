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

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate newDate) {
        date.set(newDate);
    }

    public LocalTime getTime() {
        return time.get();
    }

    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    public void setTime(LocalTime newTime) {
        time.set(newTime);
    }

    public ZoneId getZone() {
        return zone.get();
    }

    public ObjectProperty<ZoneId> zoneProperty() {
        return zone;
    }

    public void setZone(ZoneId newZone) {
        zone.set(newZone);
    }

    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.of(getDate(), getTime(), getZone());
    }

    public void setZonedDateTime(ZonedDateTime newDate) {
        setDate(newDate.toLocalDate());
        setTime(newDate.toLocalTime());
        setZone(newDate.getZone());
    }
}
