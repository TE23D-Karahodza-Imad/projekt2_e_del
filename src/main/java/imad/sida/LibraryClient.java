// Imad Karahodza - hanterar anrop mot servern och omvandlar JSON till objekt
package imad.sida;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LibraryClient {

    // serverns adress
    private static final String BASE_URL = "http://10.151.168.5:3109";

    // hämtar JSON-text från servern
    private String fetchJson(String endpoint) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // läser svaret rad för rad
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

    // hämtar böcker från /books och returnerar en ArrayList med Book-objekt
    public ArrayList<Book> fetchBooks() {
        ArrayList<Book> books = new ArrayList<>();
        String json = fetchJson("/books");
        if (json.isEmpty()) return books;

        // delar upp JSON-arrayen i enskilda objekt
        String[] objects = json.split("\\},\\{");
        for (String obj : objects) {
            obj = obj.replace("[", "").replace("]", "").replace("{", "").replace("}", "");

            String id         = getString(obj, "id");
            String title      = getString(obj, "title");
            String author     = getString(obj, "author");
            String genre      = getString(obj, "genre");
            int pages         = getInt(obj, "pages");
            boolean available = getBoolean(obj, "isAvailable");

            if (!id.isEmpty()) {
                books.add(new Book(id, title, author, genre, pages, available));
            }
        }
        System.out.println(books.size() + " böcker hämtades från servern.");
        return books;
    }

    // hämtar tidningar från /magazines och returnerar en ArrayList med Magazine-objekt
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

            if (!id.isEmpty()) {
                magazines.add(new Magazine(id, title, issueNumber, category, publishedYear, available));
            }
        }
        System.out.println(magazines.size() + " tidningar hämtades från servern.");
        return magazines;
    }
}
