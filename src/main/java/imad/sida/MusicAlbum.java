// Imad Karahodza - klass för musikalbum, ärver från Media
package imad.sida;

public class MusicAlbum extends Media implements Comparable<MusicAlbum> {

    private String artist;

    public MusicAlbum(String id, String artist, String title, boolean isAvailable) {
        // anropar Medias konstruktor, type sätts till "music_album"
        super(id, "music_album", title, isAvailable);

        // validering — artist får inte vara null eller tom
        if (artist == null || artist.isEmpty()) {
            this.artist = "Okänd artist";
        } else {
            this.artist = artist;
        }
    }

    public String getArtist() { return artist; }

    // compareTo sorterar album på titel
    @Override
    public int compareTo(MusicAlbum other) {
        return this.title.compareTo(other.title);
    }

    @Override
    public String toString() {
        return super.toString() + " | Artist: " + artist;
    }
}
