package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Loader that puts all the stars in the catalogue
 *
 * @author Ozan Güven (297076)
 * @author Robin Goumaz (301420)
 */
public enum HygDatabaseLoader implements StarCatalogue.Loader {
    INSTANCE;

    /**
     * @see StarCatalogue.Loader#load(InputStream inputStream, StarCatalogue.Builder builder)
     */
    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        try (BufferedReader stream = new BufferedReader(new InputStreamReader(inputStream, US_ASCII))) {
            stream.readLine();
            String s;
            while ((s = stream.readLine()) != null) {
                String[] tab = s.split(",");

                int hypID = !tab[Column.HIP.ordinal()].isBlank() ? Integer.parseInt(tab[Column.HIP.ordinal()]) : 0;

                StringBuilder strBuilder = new StringBuilder();
                if (!tab[Column.PROPER.ordinal()].isBlank()) {
                    strBuilder.append(tab[Column.PROPER.ordinal()]);
                } else {
                    if (!tab[Column.BAYER.ordinal()].isBlank()) {
                        strBuilder.append(tab[Column.BAYER.ordinal()]);
                    } else {
                        strBuilder.append("?");
                    }
                    strBuilder.append(" ");
                    strBuilder.append(tab[Column.CON.ordinal()]);
                }

                EquatorialCoordinates coord = EquatorialCoordinates.of(Double.parseDouble(tab[Column.RARAD.ordinal()]), Double.parseDouble(tab[Column.DECRAD.ordinal()]));

                float magnitude = !tab[Column.MAG.ordinal()].isBlank() ? Float.parseFloat(tab[Column.MAG.ordinal()]) : 0;

                float color = !tab[Column.CI.ordinal()].isBlank() ? Float.parseFloat(tab[Column.CI.ordinal()]) : 0;

                builder.addStar(new Star(hypID, strBuilder.toString(), coord, magnitude, color));
            }
        }
    }

    private enum Column {
        ID, HIP, HD, HR, GL, BF, PROPER, RA, DEC, DIST, PMRA, PMDEC,
        RV, MAG, ABSMAG, SPECT, CI, X, Y, Z, VX, VY, VZ,
        RARAD, DECRAD, PMRARAD, PMDECRAD, BAYER, FLAM, CON,
        COMP, COMP_PRIMARY, BASE, LUM, VAR, VAR_MIN, VAR_MAX
    }
}
