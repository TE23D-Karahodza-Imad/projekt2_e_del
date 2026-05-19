package imad.sida.model;

/**
 * Representerar en avstängd användare.
 * Kopplar ett eget id till användarens id i User-klassen.
 *
 * @author Imad Karahodza
 */
public class SuspendedUser implements Comparable<SuspendedUser> {

    /** Eget id för denna avstängningspost. */
    private String id;

    /** Id på den avstängda användaren (referens till User). */
    private String userId;

    /**
     * Skapar en ny avstängningspost.
     * @param id     eget id
     * @param userId id på den användare som stängs av
     */
    public SuspendedUser(String id, String userId) {
        this.id     = id;
        this.userId = userId;
    }

    /** @return posten eget id */
    public String getId()     { return id; }

    /** @return id på den avstängda användaren */
    public String getUserId() { return userId; }

    /** Sorterar på userId. */
    @Override
    public int compareTo(SuspendedUser other) {
        return this.userId.compareTo(other.userId);
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Avstängd användarID: " + userId;
    }
}
