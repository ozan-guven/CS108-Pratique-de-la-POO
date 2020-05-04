package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

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
    private long initialTime;
    private long elapsedTime;

    private final ObjectProperty<TimeAccelerator> accelerator = null;
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
            initialTime = now;
            firstTime = false;
        }
        elapsedTime = now - initialTime;
        timeBean.setZonedDateTime(getAccelerator().adjust(timeBean.getZonedDateTime(), elapsedTime));
    }

    public TimeAccelerator getAccelerator() {
        return accelerator.getValue();
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
