// Imad Karahodza - klass för böcker, ärver från LibraryItem
package imad.sida;

// extends = ärver från LibraryItem, Comparable = kan sorteras på titel
public class Book extends LibraryItem implements Comparable<Book> {

    // private så att variablerna bara kan ändras inifrån klassen
    private String author;
    private String genre;
    private int pages;

    public Book(String id, String title, String author, String genre, int pages, boolean isAvailable) {
        // super() anropar konstruktorn i LibraryItem (förälderklassen)
        super(id, title, isAvailable);
        this.author = author;
        this.genre = genre;
        this.pages = pages;
    }

    // getters för att hämta bokens variabler
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public int getPages() { return pages; }

    // compareTo används av Collections.sort() för att sortera böcker på titel
    @Override
    public int compareTo(Book other) {
        return this.title.compareTo(other.title);
    }

    @Override
    public String toString() {
        return super.toString() + " | Författare: " + author + " | Genre: " + genre + " | Sidor: " + pages;
    }
}
