package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Loader that puts all the asterism in the catalogue
 *
 * @author Ozan Güven (297076)
 * @author Robin Goumaz (301420)
 */
public enum AsterismLoader implements StarCatalogue.Loader {
    INSTANCE;

    /**
     * @see StarCatalogue.Loader#load(InputStream inputStream, StarCatalogue.Builder builder)
     */
    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        Map<Integer, Star> map = new HashMap<>();
        for (Star s : builder.stars()) {
            map.put(s.hipparcosId(), s);
        }

        try (BufferedReader stream = new BufferedReader(new InputStreamReader(inputStream, US_ASCII))) {
            List<Star> stars = new ArrayList<>();
            String d;
            while ((d = stream.readLine()) != null) {
                String[] tab = d.split(",");
                for (String s : tab) {
                    stars.add(map.get(Integer.parseInt(s)));
                }
                builder.addAsterism(new Asterism(stars));
                stars.clear();
            }
        }
    }
}
