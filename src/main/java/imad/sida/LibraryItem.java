// Imad Karahodza - basklass för biblioteksföremål
package imad.sida;

public class LibraryItem {

    // variabler som både böcker och tidningar har gemensamt
    String id;
    String title;
    boolean isAvailable; // true om den är tillgänglig att låna

    // konstruktor - körs när man skapar ett nytt objekt
    public LibraryItem(String id, String title, boolean isAvailable) {
        // this. betyder att vi sätter klassens variabel till det som skickas in
        this.id = id;
        this.title = title;
        this.isAvailable = isAvailable;
    }
}
