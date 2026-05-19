// Imad Karahodza - abstrakt basklass för alla medietyper (spel, musik, film)
package imad.sida;

// abstract = kan inte skapas direkt, används som mall för Game, MusicAlbum, Movie
public abstract class Media extends LibraryItem {

    // type berättar vilken sorts media det är: "game", "music_album", "movie"
    protected String type;

    public Media(String id, String type, String title, boolean isAvailable) {
        // anropar LibraryItems konstruktor för id, title, isAvailable
        super(id, title, isAvailable);

        // validering — type får inte vara null eller tom
        if (type == null || type.isEmpty()) {
            this.type = "unknown";
        } else {
            this.type = type;
        }
    }

    // getter för typen
    public String getType() { return type; }

    @Override
    public String toString() {
        return super.toString() + " | Typ: " + type;
    }
}
