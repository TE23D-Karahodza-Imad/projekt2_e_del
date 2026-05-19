package imad.sida.model;

/**
 * Representerar ett spel i biblioteket.
 * Ärver från Media som i sin tur ärver från LibraryItem.
 *
 */
public class Game extends Media implements Comparable<Game> {

    /** Spelets genre. */
    private String genre;

    /** Åldersgränsen för spelet. */
    private int age;

    /**
     * Skapar ett nytt spel med validering.
     * @param id          unikt id
     * @param title       titel
     * @param genre       genre (får inte vara tom)
     * @param age         åldersgräns (måste vara &gt;= 0)
     * @param isAvailable om spelet är tillgängligt
     */
    public Game(String id, String title, String genre, int age, boolean isAvailable) {
        super(id, "game", title, isAvailable);
        this.genre = (genre != null && !genre.isEmpty()) ? genre : "Okänd";
        this.age   = (age >= 0) ? age : 0;
    }

    /** @return spelets genre */
    public String getGenre() { return genre; }

    /** @return åldersgränsen */
    public int getAge()      { return age; }

    /** Sorterar spel alfabetiskt på titel. */
    @Override
    public int compareTo(Game other) {
        return this.title.compareTo(other.title);
    }

    @Override
    public String toString() {
        return super.toString() + " | Genre: " + genre + " | Åldersgräns: " + age + "+";
    }
}
