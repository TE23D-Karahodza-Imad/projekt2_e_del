package imad.sida.service;

import imad.sida.model.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Hanterar all kommunikation mot servern via HTTP (GET, POST, DELETE).
 * Omvandlar JSON-svar till Java-objekt och skickar objekt som JSON.
 *
 */
public class LibraryClient {

    /** Serverns bas-URL. */
private static final String BASE_URL = "http://127.0.0.1:3109";    /**
     * Hämtar JSON-text från servern via GET.
     * @param endpoint API-ändpunkten, ex "/books"
     * @return JSON-sträng eller tom sträng vid fel
     */
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
            while ((line = reader.readLine()) != null) result.append(line);
            reader.close();
            conn.disconnect();
        } catch (Exception e) {
            System.out.println("Fel: kunde inte ansluta till servern. " + e.getMessage());
        }
        return result.toString();
    }

    /**
     * Skickar ett POST-anrop med JSON-data för att skapa ett nytt objekt på servern.
     * @param endpoint API-ändpunkten
     * @param jsonBody JSON-kroppen att skicka
     * @return true om servern svarade med 201 Created
     */
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
            return responseCode == 201;
        } catch (Exception e) {
            System.out.println("Fel vid POST: " + e.getMessage());
            return false;
        }
    }

    /**
     * Skickar ett DELETE-anrop för att ta bort ett objekt via id.
     * @param endpoint API-ändpunkten
     * @param id       id på objektet som ska tas bort
     * @return true om servern svarade med 200 OK
     */
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

    // ==================== JSON-PARSNING ====================

    /** Plockar ut ett strängvärde från JSON, ex "title":"Harry Potter". */
    private String getString(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1) return "";
        start += search.length();
        // Hoppa över eventuella mellanslag efter kolon
        while (start < json.length() && json.charAt(start) == ' ') start++;
        if (start >= json.length()) return "";
        if (json.charAt(start) == '"') {
            // Strängvärde med citattecken
            start++;
            int end = json.indexOf("\"", start);
            return end == -1 ? "" : json.substring(start, end);
        } else {
            // Numeriskt id utan citattecken — läs som sträng
            int end = json.indexOf(",", start);
            if (end == -1) end = json.indexOf("}", start);
            return end == -1 ? "" : json.substring(start, end).trim();
        }
    }

    /** Plockar ut ett heltal från JSON, ex "pages":450. */
    private int getInt(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1) return 0;
        start += search.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        try { return Integer.parseInt(json.substring(start, end).trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    /** Plockar ut ett booleskt värde från JSON, ex "isAvailable":true. */
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

    /**
     * Hämtar alla böcker från servern, sorterade på titel.
     * @return sorterad lista med böcker
     */
    public ArrayList<Book> fetchBooks() {
        ArrayList<Book> books = new ArrayList<>();
        String json = fetchJson("/books");
        if (json.isEmpty()) return books;

        for (String obj : json.split("\\},\\{")) {
            obj = obj.replace("[","").replace("]","").replace("{","").replace("}","");
            String id = getString(obj, "id");
            if (!id.isEmpty()) books.add(new Book(id,
                    getString(obj, "title"), getString(obj, "author"),
                    getString(obj, "genre"), getInt(obj, "pages"),
                    getBoolean(obj, "isAvailable")));
        }
        Collections.sort(books);
        System.out.println(books.size() + " böcker hämtades från servern.");
        return books;
    }

    /**
     * Hämtar alla tidningar från servern, sorterade på titel.
     * @return sorterad lista med tidningar
     */
    public ArrayList<Magazine> fetchMagazines() {
        ArrayList<Magazine> magazines = new ArrayList<>();
        String json = fetchJson("/magazines");
        if (json.isEmpty()) return magazines;

        for (String obj : json.split("\\},\\{")) {
            obj = obj.replace("[","").replace("]","").replace("{","").replace("}","");
            String id = getString(obj, "id");
            if (!id.isEmpty()) magazines.add(new Magazine(id,
                    getString(obj, "title"), getInt(obj, "issueNumber"),
                    getString(obj, "category"), getInt(obj, "publishedYear"),
                    getBoolean(obj, "isAvailable")));
        }
        Collections.sort(magazines);
        System.out.println(magazines.size() + " tidningar hämtades från servern.");
        return magazines;
    }

    /**
     * Hämtar alla användare från servern, sorterade på namn.
     * @return sorterad lista med användare
     */
    public ArrayList<User> fetchUsers() {
        ArrayList<User> users = new ArrayList<>();
        String json = fetchJson("/users");
        if (json.isEmpty()) return users;

        for (String obj : json.split("\\},\\{")) {
            obj = obj.replace("[","").replace("]","").replace("{","").replace("}","");
            String id = getString(obj, "id");
            if (!id.isEmpty()) users.add(new User(id, getString(obj, "name"), getString(obj, "email")));
        }
        Collections.sort(users);
        System.out.println(users.size() + " användare hämtades från servern.");
        return users;
    }

    /**
     * Hämtar alla avstängda användare från servern.
     * @return lista med avstängda användare
     */
    public ArrayList<SuspendedUser> fetchSuspendedUsers() {
        ArrayList<SuspendedUser> suspended = new ArrayList<>();
        String json = fetchJson("/suspended");
        if (json.isEmpty()) return suspended;

        for (String obj : json.split("\\},\\{")) {
            obj = obj.replace("[","").replace("]","").replace("{","").replace("}","");
            String id = getString(obj, "id");
            if (!id.isEmpty()) suspended.add(new SuspendedUser(id, getString(obj, "userId")));
        }
        System.out.println(suspended.size() + " avstängda hämtades från servern.");
        return suspended;
    }

    /**
     * Hämtar alla mediaobjekt från servern.
     * Använder polymorfism — skapar rätt subklass (Game, MusicAlbum, Movie)
     * baserat på "type"-fältet i JSON.
     * @return lista med Media-objekt
     */
    public ArrayList<Media> fetchMedia() {
        ArrayList<Media> mediaList = new ArrayList<>();
        String json = fetchJson("/media");
        if (json.isEmpty()) return mediaList;

        for (String obj : json.split("\\},\\{")) {
            obj = obj.replace("[","").replace("]","").replace("{","").replace("}","");
            String id    = getString(obj, "id");
            String type  = getString(obj, "type");
            String title = getString(obj, "title");
            boolean available = getBoolean(obj, "isAvailable");

            if (id.isEmpty()) continue;

            // polymorfism — skapar rätt subklass beroende på type-fältet
            switch (type) {
                case "game"        -> mediaList.add(new Game(id, title, getString(obj, "genre"), getInt(obj, "age"), available));
                case "music_album" -> mediaList.add(new MusicAlbum(id, getString(obj, "artist"), title, available));
                case "movie"       -> mediaList.add(new Movie(id, title, getString(obj, "genre"), getInt(obj, "minutes"), available));
                default            -> System.out.println("Okänd mediatyp: " + type);
            }
        }
        System.out.println(mediaList.size() + " mediaobjekt hämtades från servern.");
        return mediaList;
    }

    // ==================== HITTA ====================

    /** Hittar en bok via titel (skiftlägesokänslig), returnerar null om den inte finns. */
    public Book findBookByTitle(ArrayList<Book> books, String title) {
        for (Book b : books) { if (b.getTitle().equalsIgnoreCase(title)) return b; }
        return null;
    }

    /** Hittar en tidning via titel (skiftlägesokänslig), returnerar null om den inte finns. */
    public Magazine findMagazineByTitle(ArrayList<Magazine> magazines, String title) {
        for (Magazine m : magazines) { if (m.getTitle().equalsIgnoreCase(title)) return m; }
        return null;
    }

    /** Hittar en användare via email (skiftlägesokänslig), returnerar null om den inte finns. */
    public User findUserByEmail(ArrayList<User> users, String email) {
        for (User u : users) { if (u.getEmail().equalsIgnoreCase(email)) return u; }
        return null;
    }

    /** Hittar ett mediaobjekt via titel (skiftlägesokänslig), returnerar null om det inte finns. */
    public Media findMediaByTitle(ArrayList<Media> mediaList, String title) {
        for (Media m : mediaList) { if (m.getTitle().equalsIgnoreCase(title)) return m; }
        return null;
    }

    /**
     * Kollar om en användare är avstängd.
     * @param userId    användarens id
     * @param suspended listan med avstängda
     * @return true om användaren är avstängd
     */
    public boolean isUserSuspended(String userId, ArrayList<SuspendedUser> suspended) {
        for (SuspendedUser s : suspended) { if (s.getUserId().equals(userId)) return true; }
        return false;
    }

    // ==================== LÄGG TILL ====================

    /** Lägger till en ny bok på servern via POST. */
    public boolean addBook(Book book) {
        return postJson("/books", "{\"title\":\"" + book.getTitle() + "\",\"author\":\"" + book.getAuthor() +
                "\",\"genre\":\"" + book.getGenre() + "\",\"pages\":" + book.getPages() + ",\"isAvailable\":true}");
    }

    /** Lägger till en ny tidning på servern via POST. */
    public boolean addMagazine(Magazine mag) {
        return postJson("/magazines", "{\"title\":\"" + mag.getTitle() + "\",\"issueNumber\":" + mag.getIssueNumber() +
                ",\"category\":\"" + mag.getCategory() + "\",\"publishedYear\":" + mag.getPublishedYear() + ",\"isAvailable\":true}");
    }

    /** Lägger till en ny användare på servern via POST. */
    public boolean addUser(User user) {
        return postJson("/users", "{\"name\":\"" + user.getName() + "\",\"email\":\"" + user.getEmail() + "\"}");
    }

    /** Lägger till en avstängd användare på servern via POST. */
    public boolean addSuspendedUser(String userId) {
        return postJson("/suspended", "{\"userId\":\"" + userId + "\"}");
    }

    /** Lägger till ett spel på servern via POST. */
    public boolean addGame(Game game) {
        return postJson("/media", "{\"type\":\"game\",\"title\":\"" + game.getTitle() +
                "\",\"genre\":\"" + game.getGenre() + "\",\"age\":" + game.getAge() + ",\"isAvailable\":true}");
    }

    /** Lägger till ett musikalbum på servern via POST. */
    public boolean addMusicAlbum(MusicAlbum album) {
        return postJson("/media", "{\"type\":\"music_album\",\"artist\":\"" + album.getArtist() +
                "\",\"title\":\"" + album.getTitle() + "\",\"isAvailable\":true}");
    }

    /** Lägger till en film på servern via POST. */
    public boolean addMovie(Movie movie) {
        return postJson("/media", "{\"type\":\"movie\",\"title\":\"" + movie.getTitle() +
                "\",\"genre\":\"" + movie.getGenre() + "\",\"minutes\":" + movie.getMinutes() + ",\"isAvailable\":true}");
    }

    // ==================== TA BORT ====================

    /** Tar bort en bok från servern via id. */
    public boolean deleteBook(String id)          { return deleteById("/books", id); }

    /** Tar bort en tidning från servern via id. */
    public boolean deleteMagazine(String id)      { return deleteById("/magazines", id); }

    /** Tar bort en användare från servern via id. */
    public boolean deleteUser(String id)          { return deleteById("/users", id); }

    /** Tar bort en avstängd användare från servern via id. */
    public boolean deleteSuspendedUser(String id) { return deleteById("/suspended", id); }

    /** Tar bort ett mediaobjekt från servern via id. */
    public boolean deleteMedia(String id)         { return deleteById("/media", id); }
}
