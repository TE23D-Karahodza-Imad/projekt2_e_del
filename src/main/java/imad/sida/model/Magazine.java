package imad.sida.model;

/**
 * Representerar en tidning i biblioteket.
 * Ärver id, titel och tillgänglighet från LibraryItem.
 *
 */
public class Magazine extends LibraryItem implements Comparable<Magazine> {

    /** Tidningens nummer. */
    private int issueNumber;

    /** Tidningens kategori. */
    private String category;

    /** Året tidningen gavs ut. */
    private int publishedYear;

    /**
     * Skapar en ny tidning med validering.
     * @param id            unikt id
     * @param title         titel
     * @param issueNumber   nummer (måste vara &gt;= 0)
     * @param category      kategori
     * @param publishedYear utgivningsår (måste vara &gt;= 0)
     * @param isAvailable   om tidningen är tillgänglig
     */
    public Magazine(String id, String title, int issueNumber, String category, int publishedYear, boolean isAvailable) {
        super(id, title, isAvailable);
        this.issueNumber   = (issueNumber >= 0)   ? issueNumber   : 0;
        this.category      = (category != null && !category.isEmpty()) ? category : "Okänd";
        this.publishedYear = (publishedYear >= 0) ? publishedYear : 0;
    }

    /** @return tidningens nummer */
    public int getIssueNumber()  { return issueNumber; }

    /** @return tidningens kategori */
    public String getCategory()  { return category; }

    /** @return utgivningsåret */
    public int getPublishedYear(){ return publishedYear; }

    /** Sorterar tidningar alfabetiskt på titel. */
    @Override
    public int compareTo(Magazine other) {
        return this.title.compareTo(other.title);
    }

    @Override
    public String toString() {
        return super.toString() + " | Nummer: " + issueNumber + " | Kategori: " + category + " | År: " + publishedYear;
    }
}
