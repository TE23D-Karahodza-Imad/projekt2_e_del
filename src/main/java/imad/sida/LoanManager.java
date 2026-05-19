// Imad Karahodza - hanterar alla lån, sparar och läser från fil
package imad.sida;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

// implements Storable = klassen måste ha saveToFile och loadFromFile
public class LoanManager implements Storable {

    // lokal lista med alla aktiva lån
    private ArrayList<Loan> loans = new ArrayList<>();

    // sökvägen till filen där lån sparas
    private static final String FILE_PATH = "loans.txt";

    // konstruktor — läser in sparade lån direkt när programmet startar
    public LoanManager() {
        loadFromFile(FILE_PATH);
    }

    // ==================== LÅN ====================

    // registrerar ett nytt lån — sätter isAvailable till false på objektet
    public boolean borrowItem(String userId, LibraryItem item) {
        // kollar om objektet redan är utlånat
        if (!item.getIsAvailable()) {
            System.out.println("\"" + item.getTitle() + "\" är redan utlånad.");
            return false;
        }

        // tar fram typ-namn baserat på klassen, ex "Game" -> "game"
        String itemType = item.getClass().getSimpleName().toLowerCase();

        // skapar ett unikt lån-id
        String loanId = "loan-" + (loans.size() + 1);

        Loan loan = new Loan(loanId, userId, item.getId(), itemType);
        loans.add(loan);

        // markerar objektet som utlånat
        item.setIsAvailable(false);

        // sparar direkt till fil
        saveToFile(FILE_PATH);
        System.out.println("\"" + item.getTitle() + "\" är nu utlånad till användare " + userId + ".");
        return true;
    }

    // registrerar en återlämning — sätter isAvailable till true på objektet
    public boolean returnItem(String userId, LibraryItem item) {
        // letar upp lånet i listan
        Loan träff = null;
        for (Loan l : loans) {
            if (l.getUserId().equals(userId) && l.getItemId().equals(item.getId())) {
                träff = l;
                break;
            }
        }

        if (träff == null) {
            System.out.println("Inget lån hittades för den användaren och det objektet.");
            return false;
        }

        loans.remove(träff);

        // markerar objektet som tillgängligt igen
        item.setIsAvailable(true);

        saveToFile(FILE_PATH);
        System.out.println("\"" + item.getTitle() + "\" är återlämnad.");
        return true;
    }

    // returnerar alla lån för en specifik användare
    public ArrayList<Loan> getLoansByUser(String userId) {
        ArrayList<Loan> result = new ArrayList<>();
        for (Loan l : loans) {
            if (l.getUserId().equals(userId)) {
                result.add(l);
            }
        }
        return result;
    }

    // returnerar alla aktiva lån
    public ArrayList<Loan> getAllLoans() {
        return loans;
    }

    // ==================== FIL ====================

    // sparar alla lån till fil, en rad per lån
    @Override
    public void saveToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Loan l : loans) {
                // toString() ger formatet: loanId,userId,itemId,itemType,loanDate
                writer.write(l.toString());
                writer.newLine();
            }
        } catch (Exception e) {
            System.out.println("Fel vid sparning av lån: " + e.getMessage());
        }
    }

    // läser in lån från fil när programmet startar
    @Override
    public void loadFromFile(String filePath) {
        loans.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // delar upp raden på kommatecken
                String[] delar = line.split(",");
                if (delar.length == 5) {
                    // skapar ett Loan-objekt från de 5 delarna
                    loans.add(new Loan(delar[0], delar[1], delar[2], delar[3], delar[4]));
                }
            }
            System.out.println(loans.size() + " lån lästes in från fil.");
        } catch (Exception e) {
            // om filen inte finns än är det lugnt — inga lån sparade
            System.out.println("Ingen lånfil hittades, startar med tomt register.");
        }
    }
}
