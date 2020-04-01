package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyAsterismLoaderTest {

    private static final String HYG_CATALOGUE_NAME =
            "/hygdata_v3.csv";

    private static final String ASTERISM_CATALOGUE_NAME =
            "/asterisms.txt";

    //**************** TEST DE LA CLASSE *******************************

    @Test
    void load() throws IOException {

        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            InputStream asterismStream = getClass()
                    .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();
            List<Star> allStar = new ArrayList<Star>();
            allStar.addAll(catalogue.stars());

            System.out.println("LIST OF STARS :");
            for (Star s : allStar) {
                System.out.print(s.hipparcosId() + " ");
            } //should print out the same star IDS as in the fichier (check visually)
            System.out.println();
            System.out.println();

            System.out.println("ASTERISMS : ");
            int i;

            for (Asterism asterism : catalogue.asterisms()) {
                List<Integer> cAstInd = catalogue.asterismIndices(asterism);
                i = 0;
                for (Star star : asterism.stars()) {
                    System.out.print("Hip : ");
                    System.out.printf("%-6d", star.hipparcosId());
                    System.out.print("  foundHipparcos : ");
                    System.out.printf("%-6d", allStar.get(cAstInd.get(i)).hipparcosId());

                /*TEST : l'index stoqué dans asterismIndices renvoie le meme hipparcosId que
                l'index stoqué dans l'astérisme voulu : */
                    assertEquals(allStar.get(cAstInd.get(i)).hipparcosId(), star.hipparcosId());
                    System.out.print(" ||| ");
                    i++;
                }
                System.out.println();
            }
            assertEquals(153, catalogue.asterisms().size());
        }

    }
}