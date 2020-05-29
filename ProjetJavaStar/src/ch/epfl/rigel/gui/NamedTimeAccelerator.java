package ch.epfl.rigel.gui;

import java.time.Duration;

/**
 * Enumeration representing pairs of a name
 * and its associated accelerator
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public enum NamedTimeAccelerator {
    //Continuous
    TIMES_1("1x", TimeAccelerator.continuous(1)),
    TIMES_30("30x", TimeAccelerator.continuous(30)),
    TIMES_300("300x", TimeAccelerator.continuous(300)),
    TIMES_3000("3000x", TimeAccelerator.continuous(3000)),
    //Discrete
    DAY("jour", TimeAccelerator.discrete(60, Duration.ofHours(24))),
    SIDEREAL_DAY("jour sidéral", TimeAccelerator.discrete(60,
            Duration.ofHours(23)
                    .plus(Duration.ofMinutes(56))
                    .plus(Duration.ofSeconds(4))));

    private final String name;
    private final TimeAccelerator accelerator;

    NamedTimeAccelerator(String name, TimeAccelerator type) {
        this.name = name;
        this.accelerator = type;
    }

    /**
     * Gets the name of the pair
     *
     * @return the name of the pair
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the accelerator of the pair
     *
     * @return the accelerator of the pair
     */
    public TimeAccelerator getAccelerator() {
        return accelerator;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return getName();
    }
}
