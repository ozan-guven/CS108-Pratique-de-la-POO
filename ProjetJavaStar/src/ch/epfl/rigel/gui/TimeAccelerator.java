package ch.epfl.rigel.gui;

import java.time.ZonedDateTime;

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


}
