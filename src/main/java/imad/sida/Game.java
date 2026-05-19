// Imad Karahodza - klass för spel, ärver från Media
package imad.sida;

// extends Media = ärver typ, titel, id och isAvailable från Media och LibraryItem
public class Game extends Media implements Comparable<Game> {

    private String genre;
    private int age; // åldersgräns i år

    public Game(String id, String title, String genre, int age, boolean isAvailable) {
        // anropar Medias konstruktor, type sätts till "game"
        super(id, "game", title, isAvailable);

        // validering — genre får inte vara tom
        if (genre == null || genre.isEmpty()) {
            this.genre = "Okänd";
        } else {
            this.genre = genre;
        }

        // validering — åldersgräns måste vara ett positivt tal
        if (age < 0) {
            this.age = 0;
        } else {
            this.age = age;
        }
    }

    public String getGenre()  { return genre; }
    public int getAge()       { return age; }

    // compareTo sorterar spel på titel
    @Override
    public int compareTo(Game other) {
        return this.title.compareTo(other.title);
    }

    @Override
    public String toString() {
        return super.toString() + " | Genre: " + genre + " | Åldersgräns: " + age + "+";
    }
}
