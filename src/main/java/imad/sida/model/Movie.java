package imad.sida.model;

/**
 * Representerar en film i biblioteket.
 * Ärver från Media som i sin tur ärver från LibraryItem.
 *
 * @author Imad Karahodza
 */
public class Movie extends Media implements Comparable<Movie> {

    /** Filmens genre. */
    private String genre;

    /** Filmens längd i minuter. */
    private int minutes;

    /**
     * Skapar en ny film med validering.
     * @param id          unikt id
     * @param title       titel
     * @param genre       genre (får inte vara tom)
     * @param minutes     längd i minuter (måste vara &gt;= 0)
     * @param isAvailable om filmen är tillgänglig
     */
    public Movie(String id, String title, String genre, int minutes, boolean isAvailable) {
        super(id, "movie", title, isAvailable);
        this.genre   = (genre != null && !genre.isEmpty()) ? genre : "Okänd";
        this.minutes = (minutes >= 0) ? minutes : 0;
    }

    /** @return filmens genre */
    public String getGenre()  { return genre; }

    /** @return filmens längd i minuter */
    public int getMinutes()   { return minutes; }

    /** Sorterar filmer alfabetiskt på titel. */
    @Override
    public int compareTo(Movie other) {
        return this.title.compareTo(other.title);
    }

    @Override
    public String toString() {
        return super.toString() + " | Genre: " + genre + " | Minuter: " + minutes;
    }
}
