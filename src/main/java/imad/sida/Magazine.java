// Imad Karahodza - klass för tidningar, ärver från LibraryItem
package imad.sida;

// extends = ärver från LibraryItem, Comparable = kan sorteras på titel
public class Magazine extends LibraryItem implements Comparable<Magazine> {

    // private så att variablerna bara kan ändras inifrån klassen
    private int issueNumber;
    private String category;
    private int publishedYear;

    public Magazine(String id, String title, int issueNumber, String category, int publishedYear, boolean isAvailable) {
        // super() anropar konstruktorn i LibraryItem (förälderklassen)
        super(id, title, isAvailable);
        this.issueNumber = issueNumber;
        this.category = category;
        this.publishedYear = publishedYear;
    }

    // getters för att hämta tidningens variabler
    public int getIssueNumber() { return issueNumber; }
    public String getCategory() { return category; }
    public int getPublishedYear() { return publishedYear; }

    // compareTo används av Collections.sort() för att sortera tidningar på titel
    @Override
    public int compareTo(Magazine other) {
        return this.title.compareTo(other.title);
    }

    @Override
    public String toString() {
        return super.toString() + " | Nummer: " + issueNumber + " | Kategori: " + category + " | År: " + publishedYear;
    }
}
