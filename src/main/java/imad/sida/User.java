// Imad Karahodza - klass för användare/kunder i biblioteket
package imad.sida;

// Comparable gör att vi kan sortera användare på namn
public class User implements Comparable<User> {

    private String id;
    private String name;
    private String email;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    // compareTo används av Collections.sort() för att sortera på namn
    @Override
    public int compareTo(User other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Namn: " + name + " | Email: " + email;
    }
}
