package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.*;

public enum HygDatabaseLoader implements StarCatalogue.Loader{
    INSTANCE;


    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
       try(BufferedReader stream = new BufferedReader(new InputStreamReader(inputStream))){
           while (stream.readLine() != null){
               String[] tab = stream.readLine().split(",");

               String name = !tab[Column.PROPER.ordinal()].equals("")  ? tab[Column.PROPER.ordinal()] : tab[Column.BAYER.ordinal()] + tab[Column.CON.ordinal()];

               EquatorialCoordinates coord = EquatorialCoordinates.of(Double.parseDouble(tab[Column.RARAD.ordinal()]), Double.parseDouble(tab[Column.DECRAD.ordinal()]));

               builder.addStar(new Star(Integer.parseInt(tab[Column.HIP.ordinal()]), name, coord, Float.parseFloat(tab[Column.MAG.ordinal()]), Float.parseFloat(tab[Column.CI.ordinal()])));
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
