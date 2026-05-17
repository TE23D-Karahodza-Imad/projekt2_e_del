// Imad Karahodza - basklass för biblioteksföremål
package imad.sida;

public class LibraryItem {

    // protected så att barnklasserna (Book, Magazine) kan komma åt variablerna
    protected String id;
    protected String title;
    protected boolean isAvailable;

    // konstruktor - körs när man skapar ett nytt objekt
    public LibraryItem(String id, String title, boolean isAvailable) {
        // this. betyder att vi sätter klassens variabel till det som skickas in
        this.id = id;
        this.title = title;
        this.isAvailable = isAvailable;
    }

    // getters så man kan hämta variablernas värden utanför klassen
    public String getId() { return id; }
    public String getTitle() { return title; }
    public boolean getIsAvailable() { return isAvailable; }

    // toString skriver ut objektet som en sträng
    @Override
    public String toString() {
        return "ID: " + id + " | Titel: " + title + " | Tillgänglig: " + (isAvailable ? "Ja" : "Nej");
    }
}
