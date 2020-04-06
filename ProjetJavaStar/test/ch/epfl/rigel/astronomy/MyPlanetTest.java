package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MyPlanetTest {

    private final static Planet p = new Planet("Terre", EquatorialCoordinates.of(0, 0), 3.14f, -100);
    private final static Planet q = new Planet("Terre", EquatorialCoordinates.of(0, 0), 3.14f, -100f);

    @Test
    void planetWorksOnNonEmptyPlanet() {
        assertEquals("Terre", p.name());
        assertEquals(3.14f, p.angularSize());
        assertEquals(-100f, p.magnitude());
        assertEquals(EquatorialCoordinates.of(0, 0).toString(), p.equatorialPos().toString());

        assertEquals("Terre", q.name());
        assertEquals(3.14f, q.angularSize());
        assertEquals(-100f, q.magnitude()); //With float
        assertEquals(EquatorialCoordinates.of(0, 0).toString(), q.equatorialPos().toString());
    }

    @Test
    void planetWorksWithNonValidPlanets() {
        assertThrows(IllegalArgumentException.class, () -> {
            Planet p1 = new Planet("Terre", EquatorialCoordinates.of(0, 0), -3.14f, -100);
        });
        assertThrows(NullPointerException.class, () -> {
            Planet p2 = new Planet(null, EquatorialCoordinates.of(0, 0), 3.14f, -100f);
        });
        assertThrows(NullPointerException.class, () -> {
            Planet p3 = new Planet("Terre", null, 3.14f, -100f);
        });
    }

}