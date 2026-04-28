// Imad Karahodza - klass för tidningar, ärver från LibraryItem
package imad.sida;

// extends betyder att Magazine ärver allt från LibraryItem
public class Magazine extends LibraryItem {

    // egna variabler som bara tidningar har
    int issueNumber; // vilket nummer av tidningen det är
    String category;
    int publishedYear; // vilket år den gavs ut

    public Magazine(String id, String title, int issueNumber, String category, int publishedYear, boolean isAvailable) {
        // super() anropar konstruktorn i LibraryItem (förälderklassen)
        super(id, title, isAvailable);
        this.issueNumber = issueNumber;
        this.category = category;
        this.publishedYear = publishedYear;
    }
}
