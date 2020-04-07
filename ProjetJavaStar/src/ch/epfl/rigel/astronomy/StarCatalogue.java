package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Class representing a catalogue of stars and asterisms
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class StarCatalogue {

    private final List<Star> stars;
    private final List<Asterism> asterisms;

    private final Map<Asterism, List<Integer>> starIndexMap;

    /**
     * Constructor of the star and asterism catalogue
     *
     * @param stars     list containing all the stars
     * @param asterisms list containing all the asterisms
     * @throws IllegalArgumentException if one of the asterisms
     *                                  has a star that is not in the list of stars
     */
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) {
        this.stars = List.copyOf(stars);
        this.asterisms = List.copyOf(asterisms);
        starIndexMap = new HashMap<>();

        for (Asterism asterism : asterisms) {
            List<Integer> listOfIndex = new ArrayList<>();

            for (Star star : asterism.stars()) {
                Preconditions.checkArgument(stars.contains(star)); //Checks if all the stars of all the asterisims are in the list of stars
                listOfIndex.add(stars.indexOf(star)); //Adds the index of the current star contained in the list star to the index list
            }

            starIndexMap.put(asterism, listOfIndex);
        }
    }

    /**
     * Gets the list of stars
     *
     * @return the list of stars of the catalogue
     */
    public List<Star> stars() {
        return List.copyOf(stars);
    }

    /**
     * Gets a set of asterisms
     *
     * @return the set containing all the asterisims of the catalogue
     */
    public Set<Asterism> asterisms() {
        return new HashSet<>(asterisms);
    }

    /**
     * Gets the list of the star indexes contained in
     * the catalogue that constitutes the given asterism
     *
     * @param asterism the asterisms for which the star indexes are wanted
     * @return the list of the star indexes contained in the asterism
     * @throws IllegalArgumentException if the asterism is not in the catalogue
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        Preconditions.checkArgument(asterisms.contains(asterism)); //Can throw the exception

        return List.copyOf(starIndexMap.get(asterism));
    }

    /**
     * Builder for the StarCatalogue (non immutable StarCatalogue used
     * to build the catalogue)
     *
     * @author Robin Goumaz (301420)
     * @author Ozan Güven (297076)
     */
    public static final class Builder {

        private List<Star> starBuild;
        private List<Asterism> asterismBuild;

        /**
         * Default constructor of the builder that initializes the catalogue
         */
        public Builder() {
            starBuild = new ArrayList<>();
            asterismBuild = new ArrayList<>();
        }

        /**
         * Adds the given star to the catalogue and returns the builder
         *
         * @param star the star that needs to be added
         * @return the new builder
         */
        public Builder addStar(Star star) {
            starBuild.add(star);
            return this;
        }

        /**
         * Gets a non modifiable view, but non immutable,
         * of the stars of the catalogue being built
         *
         * @return a view of the stars of the catalogue being built
         */
        public List<Star> stars() {
            return Collections.unmodifiableList(starBuild);
        }

        /**
         * Adds the given asterism to the catalogue and returns the builder
         *
         * @param asterism the asterism that needs to be added
         * @return the new builder
         */
        public Builder addAsterism(Asterism asterism) {
            asterismBuild.add(asterism);
            return this;
        }

        /**
         * Gets a non modifiable view, but non immutable,
         * of the asterisms of the catalogue being built
         *
         * @return a view of the asterisms of the catalogue being built
         */
        public List<Asterism> asterisms() {
            return Collections.unmodifiableList(asterismBuild);
        }

        /**
         * Asks the loader to add to the catalogue the stars and/or the
         * asterisms obtained by the input flow and returns the builder.
         *
         * @param inputStream input stream of the stars and asterisms
         * @param loader      the loader
         * @return the new builder
         * @throws IOException in case of an input/output exception
         */
        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException {
            try (inputStream) {
                loader.load(inputStream, this);
            }
            return this;
        }

        /**
         * Builds the final catalogue of stars containing the stars
         * and the asterisms added until now to the builder
         *
         * @return the completed star catalogue
         */
        public StarCatalogue build() {
            return new StarCatalogue(starBuild, asterismBuild);
        }

    }

    /**
     * Interface that represents a loader of
     * the stars and asterisms for the catalogue
     *
     * @author Robin Goumaz (301420)
     * @author Ozan Güven (297076)
     */
    public interface Loader {

        /**
         * Loads the stars or asterisms of the input stream and add them to the catalogue
         * that is being built by the builder
         *
         * @param inputStream the input stream for the stars and asterisms
         * @param builder     the builder that constructs the star catalogue
         * @throws IOException in case of an input/output exception
         */
        public abstract void load(InputStream inputStream, Builder builder) throws IOException;

    }

}
