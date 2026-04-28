// Imad Karahodza - klass för böcker, ärver från LibraryItem
package imad.sida;

// extends betyder att Book ärver allt från LibraryItem
public class Book extends LibraryItem {

    // egna variabler som bara böcker har
    String author;
    String genre;
    int pages;

    public Book(String id, String title, String author, String genre, int pages, boolean isAvailable) {
        // super() anropar konstruktorn i LibraryItem (förälderklassen)
        super(id, title, isAvailable);
        this.author = author;
        this.genre = genre;
        this.pages = pages;
    }
}
