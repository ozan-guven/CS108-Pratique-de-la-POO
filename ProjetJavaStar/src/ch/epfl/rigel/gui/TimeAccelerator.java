package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Interface representing a time accelerator
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
@FunctionalInterface
public interface TimeAccelerator {

    /**
     * Computes the simulated time T
     *
     * @param initialTime represents the initial simulated time
     * @param timeSinceAnimation represents the real elapsed time since
     *                           the beginning of the simulation
     * @return the simulated time
     */
    ZonedDateTime adjust(ZonedDateTime initialTime, long timeSinceAnimation);

    /**
     * Returns a continuous accelerator with respect to the
     * given acceleration factor
     *
     * @param accFactor the acceleration factor
     * @return the continuous accelerator
     */
    static TimeAccelerator continuous(int accFactor) {
        return (initialTime, timeSinceAnimation) -> initialTime.plusNanos(accFactor * timeSinceAnimation);
    }

    /**
     * Returns a discrete accelerator with respect to the
     * given advancement frequency and the step
     *
     * @param advFreq the advancement frequency
     * @param step the step
     * @return the discrete accelerator
     */
    static TimeAccelerator discrete(int advFreq, Duration step) {
        return (initialTime, timeSinceAnimation) ->
                initialTime.plusNanos(Math.abs(advFreq * timeSinceAnimation) * step.get(ChronoUnit.NANOS));
    }
}
