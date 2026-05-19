// Imad Karahodza - klass för filmer, ärver från Media
package imad.sida;

public class Movie extends Media implements Comparable<Movie> {

    private String genre;
    private int minutes; // filmlängd i minuter

    public Movie(String id, String title, String genre, int minutes, boolean isAvailable) {
        // anropar Medias konstruktor, type sätts till "movie"
        super(id, "movie", title, isAvailable);

        // validering — genre får inte vara tom
        if (genre == null || genre.isEmpty()) {
            this.genre = "Okänd";
        } else {
            this.genre = genre;
        }

        // validering — minuter måste vara ett positivt tal
        if (minutes < 0) {
            this.minutes = 0;
        } else {
            this.minutes = minutes;
        }
    }

    public String getGenre()   { return genre; }
    public int getMinutes()    { return minutes; }

    // compareTo sorterar filmer på titel
    @Override
    public int compareTo(Movie other) {
        return this.title.compareTo(other.title);
    }

    @Override
    public String toString() {
        return super.toString() + " | Genre: " + genre + " | Minuter: " + minutes;
    }
}
