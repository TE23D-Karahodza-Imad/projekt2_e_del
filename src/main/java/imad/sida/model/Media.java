package imad.sida.model;

/**
 * Abstrakt basklass för alla medietyper: spel, musikalbum och film.
 * Ärver id, titel och tillgänglighet från LibraryItem.
 *
 */
public abstract class Media extends LibraryItem {

    /** Mediats typ — "game", "music_album" eller "movie". */
    protected String type;

    /**
     * Skapar ett nytt mediaobjekt med validering av typen.
     * @param id          unikt id
     * @param type        mediatyp
     * @param title       titel
     * @param isAvailable om mediet är tillgängligt
     */
    public Media(String id, String type, String title, boolean isAvailable) {
        super(id, title, isAvailable);
        // validering — type får inte vara null eller tom
        this.type = (type != null && !type.isEmpty()) ? type : "unknown";
    }

    /** @return mediats typ */
    public String getType() { return type; }

    @Override
    public String toString() {
        return super.toString() + " | Typ: " + type;
    }
}
