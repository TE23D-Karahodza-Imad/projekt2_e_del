// Imad Karahodza - hanterar anrop mot servern och omvandlar JSON till objekt
package imad.sida;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class LibraryClient {

    // serverns adress
    private static final String BASE_URL = "http://10.151.168.5:3109";

    // hämtar JSON-text från servern via GET
    private String fetchJson(String endpoint) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            conn.disconnect();
        } catch (Exception e) {
            System.out.println("Fel: kunde inte ansluta till servern. " + e.getMessage());
        }
        return result.toString();
    }

    // skickar ett POST-anrop med JSON-data för att skapa nytt objekt på servern
    private boolean postJson(String endpoint, String jsonBody) {
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);

            OutputStream os = conn.getOutputStream();
            os.write(jsonBody.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode == 201; // 201 = Created
        } catch (Exception e) {
            System.out.println("Fel vid POST: " + e.getMessage());
            return false;
        }
    }

    // skickar ett DELETE-anrop för att ta bort ett objekt på servern via id
    private boolean deleteById(String endpoint, String id) {
        try {
            URL url = new URL(BASE_URL + endpoint + "/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setConnectTimeout(5000);

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode == 200;
        } catch (Exception e) {
            System.out.println("Fel vid DELETE: " + e.getMessage());
            return false;
        }
    }

    // plockar ut ett strängvärde från JSON tex "title":"Harry Potter"
    private String getString(String json, String key) {
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search);
        if (start == -1) return "";
        start += search.length();
        int end = json.indexOf("\"", start);
        return end == -1 ? "" : json.substring(start, end);
    }

    // plockar ut ett heltal från JSON tex "pages":450
    private int getInt(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1) return 0;
        start += search.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        try {
            return Integer.parseInt(json.substring(start, end).trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // plockar ut true/false från JSON tex "isAvailable":true
    private boolean getBoolean(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1) return false;
        start += search.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        return json.substring(start, end).trim().equals("true");
    }

    // ==================== FETCH ====================

    // hämtar alla böcker från servern, sorterade på titel
    public ArrayList<Book> fetchBooks() {
        ArrayList<Book> books = new ArrayList<>();
        String json = fetchJson("/books");
        if (json.isEmpty()) return books;

        String[] objects = json.split("\\},\\{");
        for (String obj : objects) {
            obj = obj.replace("[", "").replace("]", "").replace("{", "").replace("}", "");
            String id         = getString(obj, "id");
            String title      = getString(obj, "title");
            String author     = getString(obj, "author");
            String genre      = getString(obj, "genre");
            int pages         = getInt(obj, "pages");
            boolean available = getBoolean(obj, "isAvailable");
            if (!id.isEmpty()) books.add(new Book(id, title, author, genre, pages, available));
        }
        Collections.sort(books); // sorterar på titel via Comparable
        System.out.println(books.size() + " böcker hämtades från servern.");
        return books;
    }

    // hämtar alla tidningar från servern, sorterade på titel
    public ArrayList<Magazine> fetchMagazines() {
        ArrayList<Magazine> magazines = new ArrayList<>();
        String json = fetchJson("/magazines");
        if (json.isEmpty()) return magazines;

        String[] objects = json.split("\\},\\{");
        for (String obj : objects) {
            obj = obj.replace("[", "").replace("]", "").replace("{", "").replace("}", "");
            String id         = getString(obj, "id");
            String title      = getString(obj, "title");
            int issueNumber   = getInt(obj, "issueNumber");
            String category   = getString(obj, "category");
            int publishedYear = getInt(obj, "publishedYear");
            boolean available = getBoolean(obj, "isAvailable");
            if (!id.isEmpty()) magazines.add(new Magazine(id, title, issueNumber, category, publishedYear, available));
        }
        Collections.sort(magazines); // sorterar på titel via Comparable
        System.out.println(magazines.size() + " tidningar hämtades från servern.");
        return magazines;
    }

    // hämtar alla användare från servern, sorterade på namn
    public ArrayList<User> fetchUsers() {
        ArrayList<User> users = new ArrayList<>();
        String json = fetchJson("/users");
        if (json.isEmpty()) return users;

        String[] objects = json.split("\\},\\{");
        for (String obj : objects) {
            obj = obj.replace("[", "").replace("]", "").replace("{", "").replace("}", "");
            String id    = getString(obj, "id");
            String name  = getString(obj, "name");
            String email = getString(obj, "email");
            if (!id.isEmpty()) users.add(new User(id, name, email));
        }
        Collections.sort(users); // sorterar på namn via Comparable
        System.out.println(users.size() + " användare hämtades från servern.");
        return users;
    }

    // hämtar alla avstängda användare från servern
    public ArrayList<SuspendedUser> fetchSuspendedUsers() {
        ArrayList<SuspendedUser> suspended = new ArrayList<>();
        String json = fetchJson("/suspended");
        if (json.isEmpty()) return suspended;

        String[] objects = json.split("\\},\\{");
        for (String obj : objects) {
            obj = obj.replace("[", "").replace("]", "").replace("{", "").replace("}", "");
            String id     = getString(obj, "id");
            String userId = getString(obj, "userId");
            if (!id.isEmpty()) suspended.add(new SuspendedUser(id, userId));
        }
        System.out.println(suspended.size() + " avstängda hämtades från servern.");
        return suspended;
    }

    // ==================== HITTA ====================

    // hittar en bok via titel, returnerar null om den inte finns
    public Book findBookByTitle(ArrayList<Book> books, String title) {
        for (Book b : books) {
            if (b.getTitle().equalsIgnoreCase(title)) return b;
        }
        return null;
    }

    // hittar en tidning via titel, returnerar null om den inte finns
    public Magazine findMagazineByTitle(ArrayList<Magazine> magazines, String title) {
        for (Magazine m : magazines) {
            if (m.getTitle().equalsIgnoreCase(title)) return m;
        }
        return null;
    }

    // hittar en användare via email, returnerar null om den inte finns
    public User findUserByEmail(ArrayList<User> users, String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) return u;
        }
        return null;
    }

    // kollar om en användare är avstängd — returnerar true om de INTE får låna
    public boolean isUserSuspended(String userId, ArrayList<SuspendedUser> suspended) {
        for (SuspendedUser s : suspended) {
            if (s.getUserId().equals(userId)) return true;
        }
        return false;
    }

    // ==================== LÄGG TILL PÅ SERVER ====================

    // lägger till en ny bok på servern via POST
    public boolean addBook(Book book) {
        String json = "{\"title\":\"" + book.getTitle() + "\",\"author\":\"" + book.getAuthor() +
                "\",\"genre\":\"" + book.getGenre() + "\",\"pages\":" + book.getPages() +
                ",\"isAvailable\":true}";
        return postJson("/books", json);
    }

    // lägger till en ny tidning på servern via POST
    public boolean addMagazine(Magazine mag) {
        String json = "{\"title\":\"" + mag.getTitle() + "\",\"issueNumber\":" + mag.getIssueNumber() +
                ",\"category\":\"" + mag.getCategory() + "\",\"publishedYear\":" + mag.getPublishedYear() +
                ",\"isAvailable\":true}";
        return postJson("/magazines", json);
    }

    // lägger till en ny användare på servern via POST
    public boolean addUser(User user) {
        String json = "{\"name\":\"" + user.getName() + "\",\"email\":\"" + user.getEmail() + "\"}";
        return postJson("/users", json);
    }

    // lägger till en avstängd användare på servern via POST
    public boolean addSuspendedUser(String userId) {
        String json = "{\"userId\":\"" + userId + "\"}";
        return postJson("/suspended", json);
    }

    // ==================== TA BORT PÅ SERVER ====================

    // tar bort en bok från servern med hjälp av id
    public boolean deleteBook(String id) {
        return deleteById("/books", id);
    }

    // tar bort en tidning från servern med hjälp av id
    public boolean deleteMagazine(String id) {
        return deleteById("/magazines", id);
    }

    // tar bort en användare från servern med hjälp av id
    public boolean deleteUser(String id) {
        return deleteById("/users", id);
    }

    // tar bort en avstängd användare från servern med hjälp av id
    public boolean deleteSuspendedUser(String id) {
        return deleteById("/suspended", id);
    }

    // tar bort ett mediaobjekt från servern med hjälp av id
    public boolean deleteMedia(String id) {
        return deleteById("/media", id);
    }

    // ==================== FETCH MEDIA ====================

    /**
     * Hämtar alla mediaobjekt från servern.
     * Använder polymorfism — skapar rätt subklass (Game, MusicAlbum, Movie)
     * beroende på "type"-fältet i JSON.
     * @return lista med Media-objekt
     */
    public ArrayList<Media> fetchMedia() {
        ArrayList<Media> mediaList = new ArrayList<>();
        String json = fetchJson("/media");
        if (json.isEmpty()) return mediaList;

        String[] objects = json.split("\\},\\{");
        for (String obj : objects) {
            obj = obj.replace("[", "").replace("]", "").replace("{", "").replace("}", "");
            String id       = getString(obj, "id");
            String type     = getString(obj, "type");
            String title    = getString(obj, "title");
            boolean available = getBoolean(obj, "isAvailable");

            if (id.isEmpty()) continue;

            // polymorfism — skapar rätt subklass baserat på type-fältet
            switch (type) {
                case "game" -> {
                    String genre = getString(obj, "genre");
                    int age = getInt(obj, "age");
                    mediaList.add(new Game(id, title, genre, age, available));
                }
                case "music_album" -> {
                    String artist = getString(obj, "artist");
                    mediaList.add(new MusicAlbum(id, artist, title, available));
                }
                case "movie" -> {
                    String genre = getString(obj, "genre");
                    int minutes = getInt(obj, "minutes");
                    mediaList.add(new Movie(id, title, genre, minutes, available));
                }
                default -> System.out.println("Okänd mediatyp: " + type);
            }
        }
        System.out.println(mediaList.size() + " mediaobjekt hämtades från servern.");
        return mediaList;
    }

    // hittar ett mediaobjekt via titel, returnerar null om det inte finns
    public Media findMediaByTitle(ArrayList<Media> mediaList, String title) {
        for (Media m : mediaList) {
            if (m.getTitle().equalsIgnoreCase(title)) return m;
        }
        return null;
    }

    // ==================== LÄGG TILL MEDIA ====================

    /** Lägger till ett spel på servern via POST. */
    public boolean addGame(Game game) {
        String json = "{\"type\":\"game\",\"title\":\"" + game.getTitle() +
                "\",\"genre\":\"" + game.getGenre() +
                "\",\"age\":" + game.getAge() +
                ",\"isAvailable\":true}";
        return postJson("/media", json);
    }

    /** Lägger till ett musikalbum på servern via POST. */
    public boolean addMusicAlbum(MusicAlbum album) {
        String json = "{\"type\":\"music_album\",\"artist\":\"" + album.getArtist() +
                "\",\"title\":\"" + album.getTitle() +
                "\",\"isAvailable\":true}";
        return postJson("/media", json);
    }

    /** Lägger till en film på servern via POST. */
    public boolean addMovie(Movie movie) {
        String json = "{\"type\":\"movie\",\"title\":\"" + movie.getTitle() +
                "\",\"genre\":\"" + movie.getGenre() +
                "\",\"minutes\":" + movie.getMinutes() +
                ",\"isAvailable\":true}";
        return postJson("/media", json);
    }
}
