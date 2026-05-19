// Imad Karahodza - hjälpklass med stream-operationer för sökning och filtrering
package imad.sida;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * StreamHelper innehåller statiska metoder som använder Java Streams
 * för att filtrera, sortera, räkna och mappa biblioteksdata.
 */
public class StreamHelper {

    /**
     * Filtrerar böcker på genre.
     * @param books  listan med böcker att filtrera
     * @param genre  genren att filtrera på
     * @return lista med böcker som matchar genren
     */
    public static List<Book> filterByGenre(ArrayList<Book> books, String genre) {
        // stream + filter plockar ut bara de böcker som matchar genren
        return books.stream()
                .filter(b -> b.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    /**
     * Filtrerar böcker på författare.
     * @param books   listan med böcker att filtrera
     * @param author  författaren att filtrera på
     * @return lista med böcker av den författaren
     */
    public static List<Book> filterByAuthor(ArrayList<Book> books, String author) {
        return books.stream()
                .filter(b -> b.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }

    /**
     * Sorterar böcker alfabetiskt på författarnamn.
     * @param books  listan med böcker
     * @return sorterad lista
     */
    public static List<Book> sortByAuthor(ArrayList<Book> books) {
        // sorted + Comparator.comparing sorterar på det fält man väljer
        return books.stream()
                .sorted(Comparator.comparing(Book::getAuthor))
                .collect(Collectors.toList());
    }

    /**
     * Sorterar böcker alfabetiskt på genre.
     * @param books  listan med böcker
     * @return sorterad lista
     */
    public static List<Book> sortByGenre(ArrayList<Book> books) {
        return books.stream()
                .sorted(Comparator.comparing(Book::getGenre))
                .collect(Collectors.toList());
    }

    /**
     * Räknar hur många böcker en specifik författare har i systemet.
     * Använder stream-metoden count().
     * @param books   listan med böcker
     * @param author  författarens namn
     * @return antal böcker av den författaren
     */
    public static long countByAuthor(ArrayList<Book> books, String author) {
        // count() räknar antalet element som passerar filtret
        return books.stream()
                .filter(b -> b.getAuthor().equalsIgnoreCase(author))
                .count();
    }

    /**
     * Mappar böckerna och returnerar bara deras titlar.
     * Använder stream-metoden map().
     * @param books  listan med böcker
     * @return lista med bara titlarna
     */
    public static List<String> getAllTitles(ArrayList<Book> books) {
        // map() omvandlar varje Book-objekt till bara dess titel-sträng
        return books.stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }
}
