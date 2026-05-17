// Imad Karahodza - klass för avstängda användare
package imad.sida;

public class SuspendedUser implements Comparable<SuspendedUser> {

    private String id;
    private String userId; // refererar till användarens id i User-klassen

    public SuspendedUser(String id, String userId) {
        this.id = id;
        this.userId = userId;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }

    // sorterar på userId
    @Override
    public int compareTo(SuspendedUser other) {
        return this.userId.compareTo(other.userId);
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Avstängd användarID: " + userId;
    }
}
