package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

/**
 * Class offering methods to get the sky color and modify colors accordingly
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class SkyColorManager {

    private static final ClosedInterval INTERVAL_FOR_BLUE = ClosedInterval.of(0, 255);
    private static final ClosedInterval INTERVAL_FOR_OPACITY = ClosedInterval.of(0, 1);
    private static final int TRANSITION_LIMIT = 200; //Represents roughly the moment when the sky begins changing color
    private static final int TRANSITION_FACTOR = 28; //Factor for the speed at which the transition from blue to black is made

    private final double sunAltDegBlue;
    private final boolean allowDayNightCycle;

    /**
     * Creates the skyColorManager knowing the sky and if the day night cycle is wanted
     *
     * @param sky                the sky
     * @param allowDayNightCycle the boolean if the day night cycle is wanted
     */
    public SkyColorManager(ObservedSky sky, boolean allowDayNightCycle) {
        sunAltDegBlue = INTERVAL_FOR_BLUE.clip(TRANSITION_LIMIT + TRANSITION_FACTOR * sky.sunHorizontalCoordinates().altDeg()); //Each time clear is called, sunAltDegBlue is updated
        this.allowDayNightCycle = allowDayNightCycle;
    }

    /**
     * Returns the color of the sky, knowing the sun's altitude
     *
     * @return the color of the sky
     */
    public Color setSkyColor() {
        if (allowDayNightCycle) {
            int blue = (int) sunAltDegBlue;
            int green = (int) (127 * sunAltDegBlue / 255d);

            //RGB (0, 127, 255) is the color called Azure
            return Color.rgb(0, green, blue);
        } else {
            return Color.BLACK;
        }
    }

    /**
     * Derives the given color by changing its opacity with respect to the altitude of the sun
     *
     * @param color the color the be derived
     * @return the derived color
     */
    public Color deriveColor(Color color) {
        return color.deriveColor(0, 1, 1,
                allowDayNightCycle ? INTERVAL_FOR_OPACITY.clip(1 - sunAltDegBlue / 255d) : 1);
    }
}
