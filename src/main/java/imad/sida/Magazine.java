// Imad Karahodza - klass för tidningar, ärver från LibraryItem
package imad.sida;

// extends betyder att Magazine ärver allt från LibraryItem
public class Magazine extends LibraryItem {

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

    // @Override betyder att vi skriver över toString från LibraryItem
    @Override
    public String toString() {
        return super.toString() + " | Nummer: " + issueNumber + " | Kategori: " + category + " | År: " + publishedYear;
    }
}
