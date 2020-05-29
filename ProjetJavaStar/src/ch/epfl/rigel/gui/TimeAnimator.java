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
    private final DateTimeBean timeBean;
    private boolean firstTime;
    private ZonedDateTime zDT;
    private long initialTime;

    private final ObjectProperty<TimeAccelerator> accelerator = new SimpleObjectProperty<>(null);
    private final BooleanProperty running = new SimpleBooleanProperty();

    public TimeAnimator(DateTimeBean timeBean) {
        this.timeBean = timeBean;
    }

    /**
     * @see AnimationTimer#start()
     */
    @Override
    public void start() {
        firstTime = true;
        running.set(true);
        super.start();
    }

    /**
     * @see AnimationTimer#stop()
     */
    @Override
    public void stop() {
        super.stop();
        running.set(false);
    }

    /**
     * @see AnimationTimer#handle(long)
     */
    @Override
    public void handle(long now) {
        if (firstTime) {
            zDT = timeBean.getZonedDateTime();
            initialTime = now;
            firstTime = false;
        }
        long elapsedTime = now - initialTime;
        timeBean.setZonedDateTime(getAccelerator().adjust(zDT, elapsedTime));
    }

    /**
     * Getter for the time accelerator
     *
     * @return the time accelerator
     */
    public TimeAccelerator getAccelerator() {
        return accelerator.get();
    }

    /**
     * Getter for the time accelerator property
     *
     * @return the time accelerator property
     */
    public ObjectProperty<TimeAccelerator> acceleratorProperty() {
        return accelerator;
    }

    /**
     * Setter for the time accelerator
     *
     * @param accelerator the accelerator to set
     */
    public void setAccelerator(TimeAccelerator accelerator) {
        this.accelerator.set(accelerator);
    }

    /**
     * Getter for the running boolean
     *
     * @return the running boolean
     */
    public boolean isRunning() {
        return running.get();
    }

    /**
     * Getter for the running property
     *
     * @return the running property
     */
    public ReadOnlyBooleanProperty runningProperty() {
        return running;
    }
}
