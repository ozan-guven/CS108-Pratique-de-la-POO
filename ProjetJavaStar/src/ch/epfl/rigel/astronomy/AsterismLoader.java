package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum AsterismLoader implements StarCatalogue.Loader{
    INSTANCE;

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        Map<Integer, Star> map = new HashMap<>();
        for (Star s: builder.stars()) {
            map.put(s.hipparcosId(), s);
        }

        try(BufferedReader stream = new BufferedReader(new InputStreamReader(inputStream))){
            List<Star> stars = new ArrayList<>();
            while (stream.readLine() != null){
                String[] tab = stream.readLine().split(",");
                for(String s : tab){
                    stars.add(map.get(Integer.parseInt(s)));
                }
                builder.addAsterism(new Asterism(stars));
                stars.clear();
            }
        }
    }
}
