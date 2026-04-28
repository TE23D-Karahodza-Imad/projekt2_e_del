// Imad Karahodza - klass för böcker, ärver från LibraryItem
package imad.sida;

public class Book extends LibraryItem {

    String author;
    String genre;
    int pages;

    public Book(String id, String title, String author, String genre, int pages, boolean isAvailable) {
        super(id, title, isAvailable);
        this.author = author;
        this.genre = genre;
        this.pages = pages;
    }
}
