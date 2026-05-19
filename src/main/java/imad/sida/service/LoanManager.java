package imad.sida.service;

import imad.sida.model.LibraryItem;
import imad.sida.model.Loan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Hanterar alla lån i biblioteket.
 * Implementerar Storable — sparar och läser lån från loans.txt.
 *
 * @author Imad Karahodza
 */
public class LoanManager implements Storable {

    /** Lokal lista med alla aktiva lån. */
    private ArrayList<Loan> loans = new ArrayList<>();

    /** Sökvägen till lånfilen. */
    private static final String FILE_PATH = "loans.txt";

    /**
     * Skapar en LoanManager och läser in sparade lån från fil direkt.
     */
    public LoanManager() {
        loadFromFile(FILE_PATH);
    }

    /**
     * Registrerar ett nytt lån. Sätter isAvailable till false på objektet.
     * @param userId  id på låntagaren
     * @param item    objektet som ska lånas
     * @return true om lånet lyckades, false om objektet redan är utlånat
     */
    public boolean borrowItem(String userId, LibraryItem item) {
        if (!item.getIsAvailable()) {
            System.out.println("\"" + item.getTitle() + "\" är redan utlånad.");
            return false;
        }
        String loanId  = "loan-" + (loans.size() + 1);
        String itemType = item.getClass().getSimpleName().toLowerCase();
        loans.add(new Loan(loanId, userId, item.getId(), itemType));
        item.setIsAvailable(false);
        saveToFile(FILE_PATH);
        System.out.println("\"" + item.getTitle() + "\" är nu utlånad till " + userId + ".");
        return true;
    }

    /**
     * Registrerar en återlämning. Sätter isAvailable till true på objektet.
     * @param userId  id på låntagaren
     * @param item    objektet som lämnas tillbaka
     * @return true om återlämningen lyckades, false om inget lån hittades
     */
    public boolean returnItem(String userId, LibraryItem item) {
        Loan träff = null;
        for (Loan l : loans) {
            if (l.getUserId().equals(userId) && l.getItemId().equals(item.getId())) {
                träff = l; break;
            }
        }
        if (träff == null) {
            System.out.println("Inget lån hittades för den användaren och det objektet.");
            return false;
        }
        loans.remove(träff);
        item.setIsAvailable(true);
        saveToFile(FILE_PATH);
        System.out.println("\"" + item.getTitle() + "\" är återlämnad.");
        return true;
    }

    /**
     * Returnerar alla aktiva lån för en specifik användare.
     * @param userId  användarens id
     * @return lista med lån
     */
    public ArrayList<Loan> getLoansByUser(String userId) {
        ArrayList<Loan> result = new ArrayList<>();
        for (Loan l : loans) {
            if (l.getUserId().equals(userId)) result.add(l);
        }
        return result;
    }

    /**
     * Returnerar alla aktiva lån.
     * @return lista med alla lån
     */
    public ArrayList<Loan> getAllLoans() { return loans; }

    /**
     * Sparar alla lån till fil, en rad per lån (CSV-format).
     * @param filePath sökvägen till filen
     */
    @Override
    public void saveToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Loan l : loans) {
                writer.write(l.toString());
                writer.newLine();
            }
        } catch (Exception e) {
            System.out.println("Fel vid sparning av lån: " + e.getMessage());
        }
    }

    /**
     * Läser in lån från fil vid programstart.
     * @param filePath sökvägen till filen
     */
    @Override
    public void loadFromFile(String filePath) {
        loans.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] delar = line.split(",");
                if (delar.length == 5) {
                    loans.add(new Loan(delar[0], delar[1], delar[2], delar[3], delar[4]));
                }
            }
            System.out.println(loans.size() + " lån lästes in från fil.");
        } catch (Exception e) {
            System.out.println("Ingen lånfil hittades, startar med tomt register.");
        }
    }
}
