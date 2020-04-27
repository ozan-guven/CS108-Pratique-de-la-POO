package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Class representing a time animator
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class TimeAnimator extends AnimationTimer {
    private DateTimeBean timeBean;
    private long initialTime;
    private long elapsedTime;

    private final ObjectProperty<TimeAccelerator> accelerator = null;
    private final BooleanProperty running = new SimpleBooleanProperty();

    public TimeAnimator(DateTimeBean timeBean) {
        this.timeBean = timeBean;
    }

    @Override
    public void handle(long now) {
        if(initialTime == 0)
            initialTime = now;
        elapsedTime = now - initialTime;
    }

    public TimeAccelerator getAccelerator() {
        return accelerator.get();
    }

    public ObjectProperty<TimeAccelerator> acceleratorProperty() {
        return accelerator;
    }

    public void setAccelerator(TimeAccelerator accelerator) {
        this.accelerator.set(accelerator);
    }

    public boolean isRunning() {
        return running.get();
    }

    public BooleanProperty runningProperty() {
        return running;
    }
}
