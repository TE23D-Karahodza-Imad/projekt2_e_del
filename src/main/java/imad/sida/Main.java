// Imad Karahodza - main klass med meny för bibliotekssystemet (C-nivå)
package imad.sida;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    // lokala samlingar för alla datatyper
    static ArrayList<Book> books = new ArrayList<>();
    static ArrayList<Magazine> magazines = new ArrayList<>();
    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<SuspendedUser> suspended = new ArrayList<>();

    static LibraryClient client = new LibraryClient();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== LibSys C-nivå ===");

        // hämtar all data från servern direkt när programmet startar
        books     = client.fetchBooks();
        magazines = client.fetchMagazines();
        users     = client.fetchUsers();
        suspended = client.fetchSuspendedUsers();

        boolean running = true;
        while (running) {
            printMenu();
            String val = scanner.nextLine().trim();

            // switch kollar vilket val användaren matade in
            switch (val) {
                case "1" -> menyHämta();
                case "2" -> menySkapa();
                case "3" -> menyTaBort();
                case "4" -> menyHitta();
                case "5" -> menySkrivUt();
                case "6" -> menyKanLåna();
                case "7" -> {
                    running = false;
                    System.out.println("Avslutar LibSys...");
                }
                default -> System.out.println("Ogiltigt val, försök igen.");
            }
        }
        scanner.close();
    }

    // skriver ut huvudmenyn
    static void printMenu() {
        System.out.println("\n========= MENY =========");
        System.out.println("1. Hämta data från servern");
        System.out.println("2. Skapa ny post");
        System.out.println("3. Ta bort post");
        System.out.println("4. Hitta post");
        System.out.println("5. Skriv ut allt");
        System.out.println("6. Kolla om användare kan låna");
        System.out.println("7. Avsluta");
        System.out.print("Välj: ");
    }

    // ==================== HÄMTA ====================

    // submeny för att hämta data från servern
    static void menyHämta() {
        System.out.println("\n--- Hämta ---");
        System.out.println("1. Böcker");
        System.out.println("2. Tidningar");
        System.out.println("3. Användare");
        System.out.println("4. Avstängda användare");
        System.out.println("5. Allt");
        System.out.print("Välj: ");
        String val = scanner.nextLine().trim();

        switch (val) {
            case "1" -> books     = client.fetchBooks();
            case "2" -> magazines = client.fetchMagazines();
            case "3" -> users     = client.fetchUsers();
            case "4" -> suspended = client.fetchSuspendedUsers();
            case "5" -> {
                // hämtar allt på en gång
                books     = client.fetchBooks();
                magazines = client.fetchMagazines();
                users     = client.fetchUsers();
                suspended = client.fetchSuspendedUsers();
                System.out.println("All data hämtad!");
            }
            default -> System.out.println("Ogiltigt val.");
        }
    }

    // ==================== SKAPA ====================

    // submeny för att skapa ny post på servern
    static void menySkapa() {
        System.out.println("\n--- Skapa ny ---");
        System.out.println("1. Ny bok");
        System.out.println("2. Ny tidning");
        System.out.println("3. Ny användare");
        System.out.println("4. Stäng av användare");
        System.out.print("Välj: ");
        String val = scanner.nextLine().trim();

        switch (val) {
            case "1" -> skapaBok();
            case "2" -> skapaTidning();
            case "3" -> skapaAnvändare();
            case "4" -> stängAvAnvändare();
            default  -> System.out.println("Ogiltigt val.");
        }
    }

    // låter användaren skapa en ny bok och skicka den till servern
    static void skapaBok() {
        System.out.println("\n--- Ny bok ---");
        System.out.print("Titel: ");
        String title = scanner.nextLine();
        System.out.print("Författare: ");
        String author = scanner.nextLine();
        System.out.print("Genre: ");
        String genre = scanner.nextLine();

        // hanterar om användaren skriver in något som inte är ett tal
        int pages = 0;
        System.out.print("Sidor: ");
        try {
            pages = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt antal sidor, sätter till 0.");
        }

        // id lämnas tomt — servern genererar ett riktigt id
        Book bok = new Book("", title, author, genre, pages, true);
        boolean ok = client.addBook(bok);
        if (ok) {
            System.out.println("Boken lades till på servern!");
            books = client.fetchBooks(); // uppdaterar lokala listan
        } else {
            System.out.println("Fel: kunde inte lägga till boken.");
        }
    }

    // låter användaren skapa en ny tidning och skicka den till servern
    static void skapaTidning() {
        System.out.println("\n--- Ny tidning ---");
        System.out.print("Titel: ");
        String title = scanner.nextLine();

        int nummer = 0;
        System.out.print("Nummer: ");
        try {
            nummer = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt nummer, sätter till 0.");
        }

        System.out.print("Kategori: ");
        String category = scanner.nextLine();

        int år = 0;
        System.out.print("Utgivningsår: ");
        try {
            år = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt år, sätter till 0.");
        }

        Magazine tidning = new Magazine("", title, nummer, category, år, true);
        boolean ok = client.addMagazine(tidning);
        if (ok) {
            System.out.println("Tidningen lades till på servern!");
            magazines = client.fetchMagazines(); // uppdaterar lokala listan
        } else {
            System.out.println("Fel: kunde inte lägga till tidningen.");
        }
    }

    // låter användaren skapa en ny användare och skicka den till servern
    static void skapaAnvändare() {
        System.out.println("\n--- Ny användare ---");
        System.out.print("Namn: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        User user = new User("", name, email);
        boolean ok = client.addUser(user);
        if (ok) {
            System.out.println("Användaren lades till på servern!");
            users = client.fetchUsers(); // uppdaterar lokala listan
        } else {
            System.out.println("Fel: kunde inte lägga till användaren.");
        }
    }

    // låter bibliotekarien stänga av en användare via email
    static void stängAvAnvändare() {
        System.out.println("\n--- Stäng av användare ---");
        System.out.print("Ange email på användaren som ska stängas av: ");
        String email = scanner.nextLine();

        // söker upp användaren med email
        User user = client.findUserByEmail(users, email);
        if (user == null) {
            System.out.println("Ingen användare hittades med den emailen.");
            return;
        }

        // kollar om de redan är avstängda
        if (client.isUserSuspended(user.getId(), suspended)) {
            System.out.println(user.getName() + " är redan avstängd.");
            return;
        }

        boolean ok = client.addSuspendedUser(user.getId());
        if (ok) {
            System.out.println(user.getName() + " är nu avstängd!");
            suspended = client.fetchSuspendedUsers(); // uppdaterar lokala listan
        } else {
            System.out.println("Fel: kunde inte stänga av användaren.");
        }
    }

    // ==================== TA BORT ====================

    // submeny för att ta bort en post från servern
    static void menyTaBort() {
        System.out.println("\n--- Ta bort ---");
        System.out.println("1. Bok (via titel)");
        System.out.println("2. Tidning (via titel)");
        System.out.println("3. Användare (via email)");
        System.out.println("4. Häv avstängning (via email)");
        System.out.print("Välj: ");
        String val = scanner.nextLine().trim();

        switch (val) {
            case "1" -> taBortBok();
            case "2" -> taBortTidning();
            case "3" -> taBortAnvändare();
            case "4" -> hävAvstängning();
            default  -> System.out.println("Ogiltigt val.");
        }
    }

    // tar bort en bok från servern via titel
    static void taBortBok() {
        System.out.print("Bokens titel: ");
        String title = scanner.nextLine();

        Book bok = client.findBookByTitle(books, title);
        if (bok == null) {
            System.out.println("Ingen bok hittades med titeln \"" + title + "\".");
            return;
        }

        boolean ok = client.deleteBook(bok.getId());
        if (ok) {
            System.out.println("\"" + bok.getTitle() + "\" togs bort från servern!");
            books = client.fetchBooks(); // uppdaterar listan
        } else {
            System.out.println("Fel: kunde inte ta bort boken.");
        }
    }

    // tar bort en tidning från servern via titel
    static void taBortTidning() {
        System.out.print("Tidningens titel: ");
        String title = scanner.nextLine();

        Magazine tidning = client.findMagazineByTitle(magazines, title);
        if (tidning == null) {
            System.out.println("Ingen tidning hittades med titeln \"" + title + "\".");
            return;
        }

        boolean ok = client.deleteMagazine(tidning.getId());
        if (ok) {
            System.out.println("\"" + tidning.getTitle() + "\" togs bort från servern!");
            magazines = client.fetchMagazines(); // uppdaterar listan
        } else {
            System.out.println("Fel: kunde inte ta bort tidningen.");
        }
    }

    // tar bort en användare från servern via email
    static void taBortAnvändare() {
        System.out.print("Användarens email: ");
        String email = scanner.nextLine();

        User user = client.findUserByEmail(users, email);
        if (user == null) {
            System.out.println("Ingen användare hittades med den emailen.");
            return;
        }

        boolean ok = client.deleteUser(user.getId());
        if (ok) {
            System.out.println(user.getName() + " togs bort från servern!");
            users = client.fetchUsers(); // uppdaterar listan
        } else {
            System.out.println("Fel: kunde inte ta bort användaren.");
        }
    }

    // häver en avstängning via användarens email
    static void hävAvstängning() {
        System.out.print("Användarens email: ");
        String email = scanner.nextLine();

        User user = client.findUserByEmail(users, email);
        if (user == null) {
            System.out.println("Ingen användare hittades med den emailen.");
            return;
        }

        // hittar rätt SuspendedUser-objekt via userId
        SuspendedUser träff = null;
        for (SuspendedUser s : suspended) {
            if (s.getUserId().equals(user.getId())) {
                träff = s;
                break;
            }
        }

        if (träff == null) {
            System.out.println(user.getName() + " är inte avstängd.");
            return;
        }

        boolean ok = client.deleteSuspendedUser(träff.getId());
        if (ok) {
            System.out.println("Avstängningen för " + user.getName() + " är hävd!");
            suspended = client.fetchSuspendedUsers(); // uppdaterar listan
        } else {
            System.out.println("Fel: kunde inte häva avstängningen.");
        }
    }

    // ==================== HITTA ====================

    // submeny för att söka efter en specifik post
    static void menyHitta() {
        System.out.println("\n--- Hitta ---");
        System.out.println("1. Bok (via titel)");
        System.out.println("2. Tidning (via titel)");
        System.out.println("3. Användare (via email)");
        System.out.print("Välj: ");
        String val = scanner.nextLine().trim();

        switch (val) {
            case "1" -> {
                System.out.print("Titel: ");
                String t = scanner.nextLine();
                Book bok = client.findBookByTitle(books, t);
                if (bok != null) System.out.println("Hittad: " + bok);
                else System.out.println("Ingen bok hittades med den titeln.");
            }
            case "2" -> {
                System.out.print("Titel: ");
                String t = scanner.nextLine();
                Magazine m = client.findMagazineByTitle(magazines, t);
                if (m != null) System.out.println("Hittad: " + m);
                else System.out.println("Ingen tidning hittades med den titeln.");
            }
            case "3" -> {
                System.out.print("Email: ");
                String e = scanner.nextLine();
                User u = client.findUserByEmail(users, e);
                if (u != null) System.out.println("Hittad: " + u);
                else System.out.println("Ingen användare hittades med den emailen.");
            }
            default -> System.out.println("Ogiltigt val.");
        }
    }

    // ==================== SKRIV UT ====================

    // submeny för att skriva ut alla poster av en typ
    static void menySkrivUt() {
        System.out.println("\n--- Skriv ut ---");
        System.out.println("1. Böcker");
        System.out.println("2. Tidningar");
        System.out.println("3. Användare");
        System.out.println("4. Avstängda användare");
        System.out.print("Välj: ");
        String val = scanner.nextLine().trim();

        switch (val) {
            case "1" -> {
                if (books.isEmpty()) System.out.println("Inga böcker laddade.");
                else { System.out.println("\n--- BÖCKER ---"); for (Book b : books) System.out.println(b); }
            }
            case "2" -> {
                if (magazines.isEmpty()) System.out.println("Inga tidningar laddade.");
                else { System.out.println("\n--- TIDNINGAR ---"); for (Magazine m : magazines) System.out.println(m); }
            }
            case "3" -> {
                if (users.isEmpty()) System.out.println("Inga användare laddade.");
                else { System.out.println("\n--- ANVÄNDARE ---"); for (User u : users) System.out.println(u); }
            }
            case "4" -> {
                if (suspended.isEmpty()) System.out.println("Inga avstängda användare.");
                else { System.out.println("\n--- AVSTÄNGDA ---"); for (SuspendedUser s : suspended) System.out.println(s); }
            }
            default -> System.out.println("Ogiltigt val.");
        }
    }

    // ==================== KAN LÅNA ====================

    // kollar om en specifik användare får låna (inte avstängd)
    static void menyKanLåna() {
        System.out.println("\n--- Kolla lånestatus ---");
        System.out.print("Ange användarens email: ");
        String email = scanner.nextLine();

        // söker upp användaren
        User user = client.findUserByEmail(users, email);
        if (user == null) {
            System.out.println("Ingen användare hittades med den emailen.");
            return;
        }

        // canUserBorrow — returnerar true om användaren INTE är avstängd
        boolean kanLåna = canUserBorrow(user.getId());
        if (kanLåna) {
            System.out.println(user.getName() + " är aktiv och får låna.");
        } else {
            System.out.println(user.getName() + " är AVSTÄNGD och får INTE låna.");
        }
    }

    // hjälpmetod — returnerar true om användaren inte är avstängd
    static boolean canUserBorrow(String userId) {
        return !client.isUserSuspended(userId, suspended);
    }
}
