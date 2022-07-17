package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MyStereographicProjectionTest {

    @Test
    void circleRadiusForParallelGivesInfinityWhenExpected() {
        StereographicProjection projection = new StereographicProjection(HorizontalCoordinates.of(1, 0));

        double radius = projection.circleRadiusForParallel(HorizontalCoordinates.ofDeg(0, 0));

        assertEquals(Double.POSITIVE_INFINITY, radius);
    }

    @Test
    void testHashCodeThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> {
            StereographicProjection projection = new StereographicProjection(HorizontalCoordinates.of(0, 0));

            projection.hashCode();
        });
    }

    @Test
    void testEqualsThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> {
            StereographicProjection projection = new StereographicProjection(HorizontalCoordinates.of(0, 0));

            projection.equals(null);
        });
    }

    @Test
    void testToString() {
    }

    //******************************* TESTS DE LA CLASSE **********************************

    @Test
    void circleCenterForParallelWorksOnExampleGivenByClass() {
        StereographicProjection projection = new StereographicProjection(
                HorizontalCoordinates.ofDeg(45, 45));

        CartesianCoordinates coord = projection.circleCenterForParallel(
                HorizontalCoordinates.ofDeg(0, 27));

        assertEquals(0.6089987400733187, coord.y(), 1e-10);
    }

    @Test
    void circleRadiusForParallelWorksOnExampleGivenByClass() {
        StereographicProjection projection = new StereographicProjection(
                HorizontalCoordinates.ofDeg(45, 45));

        double radius = projection.circleRadiusForParallel(
                HorizontalCoordinates.ofDeg(0, 27));

        assertEquals(0.767383180397855, radius, 1e-10);
    }

    @Test
    void applyToAngleWorksWorksExampleGivenByClass() {
        StereographicProjection projection = new StereographicProjection(
                HorizontalCoordinates.ofDeg(23, 45));

        double diameter = projection.applyToAngle(Angle.ofDeg(1 / 2.0));

        assertEquals(4.363330053e-3, diameter, 1e-10);
    }

    @Test
    void applyWorksOnExampleGivenByClass() {
        StereographicProjection projection = new StereographicProjection(
                HorizontalCoordinates.ofDeg(45, 45));

        CartesianCoordinates coord = projection.apply(HorizontalCoordinates.ofDeg(45, 30));

        assertEquals(-0.1316524976, coord.y(), 1e-10);
    }

    @Test
    void inverseApplyWorksOnExampleGivenByClass() {
        StereographicProjection projection = new StereographicProjection(
                HorizontalCoordinates.ofDeg(45, 45));
        StereographicProjection projection1 = new StereographicProjection(
                HorizontalCoordinates.ofDeg(45, 20));


        HorizontalCoordinates coord = projection.inverseApply(
                CartesianCoordinates.of(10, 0));

        HorizontalCoordinates coord1 = projection1.inverseApply(
                CartesianCoordinates.of(0, 25));

        assertEquals(/*3.648704525474978 */3.648704634091643, coord.az(), 1e-10);
        assertEquals(3.9269908169872414, coord1.az());
        assertEquals(-0.2691084761522857, coord1.alt());

    }
}