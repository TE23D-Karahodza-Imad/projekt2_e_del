package imad.sida.model;

/**
 * Representerar en bok i biblioteket.
 * Ärver id, titel och tillgänglighet från LibraryItem.
 *
 * @author Imad Karahodza
 */
public class Book extends LibraryItem implements Comparable<Book> {

    /** Bokens författare. */
    private String author;

    /** Bokens genre. */
    private String genre;

    /** Antal sidor — valideras så att det inte är negativt. */
    private int pages;

    /**
     * Skapar en ny bok med validering av indata.
     * @param id          unikt id
     * @param title       titel
     * @param author      författare
     * @param genre       genre
     * @param pages       antal sidor (måste vara &gt;= 0)
     * @param isAvailable om boken är tillgänglig
     */
    public Book(String id, String title, String author, String genre, int pages, boolean isAvailable) {
        super(id, title, isAvailable);
        // validering — author och genre får inte vara null eller tomma
        this.author = (author != null && !author.isEmpty()) ? author : "Okänd";
        this.genre  = (genre  != null && !genre.isEmpty())  ? genre  : "Okänd";
        // validering — sidor får inte vara negativt
        this.pages  = (pages >= 0) ? pages : 0;
    }

    /** @return bokens författare */
    public String getAuthor() { return author; }

    /** @return bokens genre */
    public String getGenre()  { return genre; }

    /** @return antal sidor */
    public int getPages()     { return pages; }

    /**
     * Sorterar böcker alfabetiskt på titel.
     * Används av Collections.sort().
     */
    @Override
    public int compareTo(Book other) {
        return this.title.compareTo(other.title);
    }

    @Override
    public String toString() {
        return super.toString() + " | Författare: " + author + " | Genre: " + genre + " | Sidor: " + pages;
    }
}
