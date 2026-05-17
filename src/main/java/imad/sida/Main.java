// Imad Karahodza - main klass med meny för bibliotekssystemet
package imad.sida;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    // lokala samlingar där böcker och tidningar sparas
    static ArrayList<Book> books = new ArrayList<>();
    static ArrayList<Magazine> magazines = new ArrayList<>();

    static LibraryClient client = new LibraryClient();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== LibSys ===");

        boolean running = true;
        while (running) {
            printMenu();
            String val = scanner.nextLine().trim();

            // switch kollar vilket val användaren matade in
            switch (val) {
                case "1" -> books = client.fetchBooks();
                case "2" -> magazines = client.fetchMagazines();
                case "3" -> skrivUt();
                case "4" -> läggTillBok();
                case "5" -> läggTillTidning();
                case "6" -> {
                    running = false;
                    System.out.println("Avslutar...");
                }
                default -> System.out.println("Ogiltigt val, försök igen.");
            }
        }
        scanner.close();
    }

    // skriver ut menyalternativen
    static void printMenu() {
        System.out.println("\n--- MENY ---");
        System.out.println("1. Hämta böcker från servern");
        System.out.println("2. Hämta tidningar från servern");
        System.out.println("3. Skriv ut böcker / tidningar");
        System.out.println("4. Lägg till bok");
        System.out.println("5. Lägg till tidning");
        System.out.println("6. Avsluta");
        System.out.print("Välj: ");
    }

    // skriver ut böcker eller tidningar beroende på val
    static void skrivUt() {
        System.out.print("(1) Böcker eller (2) Tidningar: ");
        String val = scanner.nextLine().trim();

        if (val.equals("1")) {
            if (books.isEmpty()) {
                System.out.println("Inga böcker laddade, hämta dem först (val 1).");
            } else {
                System.out.println("\n--- BÖCKER ---");
                // loopar igenom alla böcker och skriver ut dem
                for (Book b : books) System.out.println(b);
            }
        } else if (val.equals("2")) {
            if (magazines.isEmpty()) {
                System.out.println("Inga tidningar laddade, hämta dem först (val 2).");
            } else {
                System.out.println("\n--- TIDNINGAR ---");
                for (Magazine m : magazines) System.out.println(m);
            }
        }
    }

    // låter användaren lägga till en ny bok manuellt
    static void läggTillBok() {
        System.out.println("\n--- Lägg till bok ---");
        System.out.print("Titel: ");
        String title = scanner.nextLine();
        System.out.print("Författare: ");
        String author = scanner.nextLine();
        System.out.print("Genre: ");
        String genre = scanner.nextLine();
        System.out.print("Sidor: ");
        int pages = 0;
        try {
            pages = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt antal, sätter sidor till 0.");
        }

        // skapar ett lokalt ID så boken får ett unikt id
        String id = "local-" + (books.size() + 1);
        Book bok = new Book(id, title, author, genre, pages, true);
        books.add(bok);
        System.out.println("Tillagd: " + bok);
    }

    // låter användaren lägga till en ny tidning manuellt
    static void läggTillTidning() {
        System.out.println("\n--- Lägg till tidning ---");
        System.out.print("Titel: ");
        String title = scanner.nextLine();
        System.out.print("Nummer: ");
        int nummer = 0;
        try {
            nummer = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt nummer, sätter till 0.");
        }
        System.out.print("Kategori: ");
        String category = scanner.nextLine();
        System.out.print("Utgivningsår: ");
        int år = 0;
        try {
            år = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt år, sätter till 0.");
        }

        String id = "local-" + (magazines.size() + 1);
        Magazine tidning = new Magazine(id, title, nummer, category, år, true);
        magazines.add(tidning);
        System.out.println("Tillagd: " + tidning);
    }
}
