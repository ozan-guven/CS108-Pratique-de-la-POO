package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.*;

import java.time.ZonedDateTime;

/**
 * Class representing a time animator
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class TimeAnimator extends AnimationTimer {
    private DateTimeBean timeBean;
    private boolean firstTime;
    private ZonedDateTime zoneDateTime;
    private long initialTime;
    private long elapsedTime;

    private final ObjectProperty<TimeAccelerator> accelerator = new SimpleObjectProperty<>(null);
    private final BooleanProperty running = new SimpleBooleanProperty();

    public TimeAnimator(DateTimeBean timeBean) {
        this.timeBean = timeBean;
    }

    @Override
    public void start() {
        firstTime = true;
        running.set(true);
        super.start();

    }

    @Override
    public void stop() {
        super.stop();
        running.set(false);
    }

    @Override
    public void handle(long now) {
        if(firstTime){
            zoneDateTime = timeBean.getZonedDateTime();
            initialTime = now;
            firstTime = false;
        }
        elapsedTime = now - initialTime;
        timeBean.setZonedDateTime(getAccelerator().adjust(zoneDateTime, elapsedTime));
    }

    public TimeAccelerator getAccelerator() {
        return accelerator.get();
    }

    public ObjectProperty<TimeAccelerator> acceleratorProperty() {
        return accelerator;
    }

    public void setAccelerator(TimeAccelerator accelerator) {
        this.accelerator.setValue(accelerator);
    }

    public boolean isRunning() {
        return running.get();
    }

    public ReadOnlyBooleanProperty runningProperty() {
        return running;
    }
}
