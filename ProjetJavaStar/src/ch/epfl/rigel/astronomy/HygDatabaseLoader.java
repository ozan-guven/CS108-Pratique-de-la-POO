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

               Integer hypID = !tab[Column.HIP.ordinal()].equals("")  ? Integer.parseInt(tab[Column.HIP.ordinal()]) : 0;

               String name = !tab[Column.PROPER.ordinal()].equals("")  ? tab[Column.PROPER.ordinal()] : tab[Column.BAYER.ordinal()] + tab[Column.CON.ordinal()];

               EquatorialCoordinates coord = EquatorialCoordinates.of(Double.parseDouble(tab[Column.RARAD.ordinal()]), Double.parseDouble(tab[Column.DECRAD.ordinal()]));

               float magnitude = !tab[Column.MAG.ordinal()].equals("")  ? Float.parseFloat(tab[Column.MAG.ordinal()]) : 0;

               float color = !tab[Column.CI.ordinal()].equals("")  ? Float.parseFloat(tab[Column.CI.ordinal()]) : 0;

               System.out.println(name);

               builder.addStar(new Star(hypID, name, coord, magnitude, color));
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
