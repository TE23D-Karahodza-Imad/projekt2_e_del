package imad.sida.service;

import imad.sida.model.Book;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Hjälpklass med statiska metoder för stream-operationer på biblioteksdata.
 * Används för filtrering, sortering, räkning och mappning av böcker.
 *
 */
public class StreamHelper {

    /**
     * Filtrerar böcker på genre med stream filter().
     * @param books  listan med böcker att filtrera
     * @param genre  genren att filtrera på
     * @return lista med matchande böcker
     */
    public static List<Book> filterByGenre(ArrayList<Book> books, String genre) {
        return books.stream()
                .filter(b -> b.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    /**
     * Filtrerar böcker på författare med stream filter().
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
     * Sorterar böcker alfabetiskt på författarnamn med stream sorted().
     * @param books  listan med böcker
     * @return sorterad lista
     */
    public static List<Book> sortByAuthor(ArrayList<Book> books) {
        return books.stream()
                .sorted(Comparator.comparing(Book::getAuthor))
                .collect(Collectors.toList());
    }

    /**
     * Sorterar böcker alfabetiskt på genre med stream sorted().
     * @param books  listan med böcker
     * @return sorterad lista
     */
    public static List<Book> sortByGenre(ArrayList<Book> books) {
        return books.stream()
                .sorted(Comparator.comparing(Book::getGenre))
                .collect(Collectors.toList());
    }

    /**
     * Räknar hur många böcker en specifik författare har med stream count().
     * @param books   listan med böcker
     * @param author  författarens namn
     * @return antal böcker
     */
    public static long countByAuthor(ArrayList<Book> books, String author) {
        return books.stream()
                .filter(b -> b.getAuthor().equalsIgnoreCase(author))
                .count();
    }

    /**
     * Mappar böckerna till bara deras titlar med stream map().
     * @param books  listan med böcker
     * @return lista med bara titlarna
     */
    public static List<String> getAllTitles(ArrayList<Book> books) {
        return books.stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }
}
