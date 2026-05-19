package imad.sida.model;

/**
 * Abstrakt basklass för alla föremål i biblioteket.
 * Book, Magazine och Media ärver från denna klass.
 *
 * @author Imad Karahodza
 */
public class LibraryItem {

    /** Unikt id från servern. */
    protected String id;

    /** Titeln på föremålet. */
    protected String title;

    /** Anger om föremålet är tillgängligt för lån. */
    protected boolean isAvailable;

    /**
     * Skapar ett nytt LibraryItem.
     * @param id          unikt id
     * @param title       titel
     * @param isAvailable om föremålet är tillgängligt
     */
    public LibraryItem(String id, String title, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.isAvailable = isAvailable;
    }

    /** @return föremålets id */
    public String getId() { return id; }

    /** @return föremålets titel */
    public String getTitle() { return title; }

    /** @return true om föremålet är tillgängligt */
    public boolean getIsAvailable() { return isAvailable; }

    /**
     * Uppdaterar tillgängligheten — används av LoanManager vid lån och återlämning.
     * @param isAvailable true = tillgänglig, false = utlånad
     */
    public void setIsAvailable(boolean isAvailable) { this.isAvailable = isAvailable; }

    @Override
    public String toString() {
        return "ID: " + id + " | Titel: " + title + " | Tillgänglig: " + (isAvailable ? "Ja" : "Nej");
    }
}
