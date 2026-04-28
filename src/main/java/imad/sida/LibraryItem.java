// Imad Karahodza - basklass för biblioteksföremål
package imad.sida;

public class LibraryItem {

    String id;
    String title;
    boolean isAvailable;

    public LibraryItem(String id, String title, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.isAvailable = isAvailable;
    }
}
