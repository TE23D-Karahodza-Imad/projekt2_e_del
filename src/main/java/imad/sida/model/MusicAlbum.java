package imad.sida.model;

/**
 * Representerar ett musikalbum i biblioteket.
 * Ärver från Media som i sin tur ärver från LibraryItem.
 *
 */
public class MusicAlbum extends Media implements Comparable<MusicAlbum> {

    /** Albumets artist. */
    private String artist;

    /**
     * Skapar ett nytt musikalbum med validering.
     * @param id          unikt id
     * @param artist      artist (får inte vara tom)
     * @param title       titel
     * @param isAvailable om albumet är tillgängligt
     */
    public MusicAlbum(String id, String artist, String title, boolean isAvailable) {
        super(id, "music_album", title, isAvailable);
        this.artist = (artist != null && !artist.isEmpty()) ? artist : "Okänd artist";
    }

    /** @return albumets artist */
    public String getArtist() { return artist; }

    /** Sorterar album alfabetiskt på titel. */
    @Override
    public int compareTo(MusicAlbum other) {
        return this.title.compareTo(other.title);
    }

    @Override
    public String toString() {
        return super.toString() + " | Artist: " + artist;
    }
}
