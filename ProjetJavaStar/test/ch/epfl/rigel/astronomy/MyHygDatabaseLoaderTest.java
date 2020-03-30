package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class MyHygDatabaseLoaderTest {

    private static final String HYG_CATALOGUE_NAME =
            "/hygdata_v3.csv";

    @Test
    void hygDatabaseIsCorrectlyInstalled() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            assertNotNull(hygStream);
        }
    }

    @Test
    void hygDatabaseContainsRigel() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .build();
            Star rigel = null;
            for (Star s : catalogue.stars()) {
                if (s.name().equalsIgnoreCase("rigel"))
                    rigel = s;
                //System.out.printf("%5d : %-10s in position %s%n", s.hipparcosId(), s.name(), s.equatorialPos()); //Added
            }
            //System.out.printf("The number of stars is : %d", catalogue.stars().size());
            assertNotNull(rigel);
        }
    }

    //**************** TEST DE LA CLASSE *******************************

    private static final String ASTERISM_CATALOGUE_NAME =
            "/asterisms.txt";
    @Test
    void variousTestsAndReadablePrintfOnCompletelyFinishedStarCatalogue() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            InputStream asterismStream = getClass()
                    .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();
            Star rigel = null;
            for (Star s : catalogue.stars()) {
                if (s.name().equalsIgnoreCase("rigel"))
                    rigel = s;
            }
            assertNotNull(rigel);
        }
    }

}