package imad.sida.model;

/**
 * Representerar en registrerad användare i biblioteket.
 *
 * @author Imad Karahodza
 */
public class User implements Comparable<User> {

    /** Unikt id från servern. */
    private String id;

    /** Användarens namn. */
    private String name;

    /** Användarens e-postadress. */
    private String email;

    /**
     * Skapar en ny användare med validering.
     * @param id    unikt id
     * @param name  namn (får inte vara tomt)
     * @param email e-postadress (får inte vara tom)
     */
    public User(String id, String name, String email) {
        this.id    = id;
        this.name  = (name  != null && !name.isEmpty())  ? name  : "Okänt namn";
        this.email = (email != null && !email.isEmpty()) ? email : "okand@email.com";
    }

    /** @return användarens id */
    public String getId()    { return id; }

    /** @return användarens namn */
    public String getName()  { return name; }

    /** @return användarens e-postadress */
    public String getEmail() { return email; }

    /** Sorterar användare alfabetiskt på namn. */
    @Override
    public int compareTo(User other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Namn: " + name + " | Email: " + email;
    }
}
