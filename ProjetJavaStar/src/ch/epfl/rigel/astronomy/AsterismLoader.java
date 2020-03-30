package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public enum AsterismLoader implements StarCatalogue.Loader{
    INSTANCE;


    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        try(BufferedReader stream = new BufferedReader(new InputStreamReader(inputStream))){
            while (stream.readLine() != null){
                String[] tab = stream.readLine().split(",");
                builder.stars()
                builder.addAsterism(new Asterism())

            }
        }
    }
}
