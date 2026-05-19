// Imad Karahodza - interface för klasser som kan spara och läsa data från fil
package imad.sida;

// interface = kontrakt som klassen som implementerar det MÅSTE följa
public interface Storable {

    // sparar data till en fil på disk
    void saveToFile(String filePath);

    // läser in data från en fil på disk
    void loadFromFile(String filePath);
}
