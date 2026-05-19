package imad.sida.service;

/**
 * Gränssnitt för klasser som kan spara och läsa data från fil.
 * LoanManager implementerar detta interface.
 *
 * @author Imad Karahodza
 */
public interface Storable {

    /**
     * Sparar data till en fil på disk.
     * @param filePath sökvägen till filen
     */
    void saveToFile(String filePath);

    /**
     * Läser in data från en fil på disk.
     * @param filePath sökvägen till filen
     */
    void loadFromFile(String filePath);
}
