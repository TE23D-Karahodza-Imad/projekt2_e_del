// Imad Karahodza - klass för tidningar, ärver från LibraryItem
package imad.sida;

public class Magazine extends LibraryItem {

    int issueNumber;
    String category;
    int publishedYear;

    public Magazine(String id, String title, int issueNumber, String category, int publishedYear, boolean isAvailable) {
        super(id, title, isAvailable);
        this.issueNumber = issueNumber;
        this.category = category;
        this.publishedYear = publishedYear;
    }
}
