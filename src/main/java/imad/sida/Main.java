package imad.sida;

import imad.sida.model.*;
import imad.sida.service.LibraryClient;
import imad.sida.service.LoanManager;
import imad.sida.service.StreamHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Programmets startpunkt.
 * Hanterar all menynavigering och kopplar ihop alla klasser.
 *
 * @author Imad Karahodza
 */
public class Main {

    static ArrayList<Book>          books     = new ArrayList<>();
    static ArrayList<Magazine>      magazines = new ArrayList<>();
    static ArrayList<User>          users     = new ArrayList<>();
    static ArrayList<SuspendedUser> suspended = new ArrayList<>();
    static ArrayList<Media>         mediaList = new ArrayList<>();

    static LibraryClient client      = new LibraryClient();
    static LoanManager   loanManager = new LoanManager();
    static Scanner       scanner     = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== LibSys A-nivå ===");

        books     = client.fetchBooks();
        magazines = client.fetchMagazines();
        users     = client.fetchUsers();
        suspended = client.fetchSuspendedUsers();
        mediaList = client.fetchMedia();

        boolean running = true;
        while (running) {
            printMenu();
            String val = scanner.nextLine().trim();

            switch (val) {
                case "1"  -> menyHämta();
                case "2"  -> menySkapa();
                case "3"  -> menyTaBort();
                case "4"  -> menyHitta();
                case "5"  -> menySkrivUt();
                case "6"  -> menyKanLåna();
                case "7"  -> menyMedia();
                case "8"  -> menyUtlåning();
                case "9"  -> menySök();
                case "10" -> { running = false; System.out.println("Avslutar LibSys..."); }
                default   -> System.out.println("Ogiltigt val, försök igen.");
            }
        }
        scanner.close();
    }

    static void printMenu() {
        System.out.println("\n========= MENY =========");
        System.out.println("1.  Hämta data från servern");
        System.out.println("2.  Skapa ny post");
        System.out.println("3.  Ta bort post");
        System.out.println("4.  Hitta post");
        System.out.println("5.  Skriv ut allt");
        System.out.println("6.  Kolla om användare kan låna");
        System.out.println("7.  Media (spel/musik/film)");
        System.out.println("8.  Utlåning");
        System.out.println("9.  Sök och filtrera");
        System.out.println("10. Avsluta");
        System.out.print("Välj: ");
    }

    // ==================== HÄMTA ====================

    static void menyHämta() {
        System.out.println("\n--- Hämta ---");
        System.out.println("1. Böcker  2. Tidningar  3. Användare  4. Avstängda  5. Media  6. Allt");
        System.out.print("Välj: ");
        String val = scanner.nextLine().trim();
        switch (val) {
            case "1" -> books     = client.fetchBooks();
            case "2" -> magazines = client.fetchMagazines();
            case "3" -> users     = client.fetchUsers();
            case "4" -> suspended = client.fetchSuspendedUsers();
            case "5" -> mediaList = client.fetchMedia();
            case "6" -> { books = client.fetchBooks(); magazines = client.fetchMagazines();
                          users = client.fetchUsers(); suspended = client.fetchSuspendedUsers();
                          mediaList = client.fetchMedia(); System.out.println("All data hämtad!"); }
            default  -> System.out.println("Ogiltigt val.");
        }
    }

    // ==================== SKAPA ====================

    static void menySkapa() {
        System.out.println("\n--- Skapa ny ---");
        System.out.println("1. Bok  2. Tidning  3. Användare  4. Stäng av användare");
        System.out.print("Välj: ");
        switch (scanner.nextLine().trim()) {
            case "1" -> skapaBok();
            case "2" -> skapaTidning();
            case "3" -> skapaAnvändare();
            case "4" -> stängAvAnvändare();
            default  -> System.out.println("Ogiltigt val.");
        }
    }

    static void skapaBok() {
        System.out.println("\n--- Ny bok ---");
        System.out.print("Titel: ");     String title  = scanner.nextLine();
        System.out.print("Författare: "); String author = scanner.nextLine();
        System.out.print("Genre: ");     String genre  = scanner.nextLine();
        int pages = 0;
        System.out.print("Sidor: ");
        try { pages = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Ogiltigt antal, sätter till 0."); }
        if (client.addBook(new Book("", title, author, genre, pages, true))) {
            System.out.println("Boken lades till!"); books = client.fetchBooks();
        } else System.out.println("Fel: kunde inte lägga till boken.");
    }

    static void skapaTidning() {
        System.out.println("\n--- Ny tidning ---");
        System.out.print("Titel: "); String title = scanner.nextLine();
        int nummer = 0; System.out.print("Nummer: ");
        try { nummer = Integer.parseInt(scanner.nextLine().trim()); } catch (NumberFormatException e) { System.out.println("Sätter till 0."); }
        System.out.print("Kategori: "); String cat = scanner.nextLine();
        int år = 0; System.out.print("Utgivningsår: ");
        try { år = Integer.parseInt(scanner.nextLine().trim()); } catch (NumberFormatException e) { System.out.println("Sätter till 0."); }
        if (client.addMagazine(new Magazine("", title, nummer, cat, år, true))) {
            System.out.println("Tidningen lades till!"); magazines = client.fetchMagazines();
        } else System.out.println("Fel: kunde inte lägga till tidningen.");
    }

    static void skapaAnvändare() {
        System.out.println("\n--- Ny användare ---");
        System.out.print("Namn: ");  String name  = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();
        if (client.addUser(new User("", name, email))) {
            System.out.println("Användaren lades till!"); users = client.fetchUsers();
        } else System.out.println("Fel: kunde inte lägga till användaren.");
    }

    static void stängAvAnvändare() {
        System.out.print("Email: "); String email = scanner.nextLine();
        User user = client.findUserByEmail(users, email);
        if (user == null) { System.out.println("Ingen användare hittades."); return; }
        if (client.isUserSuspended(user.getId(), suspended)) { System.out.println(user.getName() + " är redan avstängd."); return; }
        if (client.addSuspendedUser(user.getId())) {
            System.out.println(user.getName() + " är nu avstängd!"); suspended = client.fetchSuspendedUsers();
        } else System.out.println("Fel: kunde inte stänga av användaren.");
    }

    // ==================== TA BORT ====================

    static void menyTaBort() {
        System.out.println("\n--- Ta bort ---");
        System.out.println("1. Bok  2. Tidning  3. Användare  4. Häv avstängning");
        System.out.print("Välj: ");
        switch (scanner.nextLine().trim()) {
            case "1" -> taBortBok();
            case "2" -> taBortTidning();
            case "3" -> taBortAnvändare();
            case "4" -> hävAvstängning();
            default  -> System.out.println("Ogiltigt val.");
        }
    }

    static void taBortBok() {
        System.out.print("Titel: "); Book b = client.findBookByTitle(books, scanner.nextLine());
        if (b == null) { System.out.println("Ingen bok hittades."); return; }
        if (client.deleteBook(b.getId())) { System.out.println("\"" + b.getTitle() + "\" togs bort!"); books = client.fetchBooks(); }
        else System.out.println("Fel: kunde inte ta bort.");
    }

    static void taBortTidning() {
        System.out.print("Titel: "); Magazine m = client.findMagazineByTitle(magazines, scanner.nextLine());
        if (m == null) { System.out.println("Ingen tidning hittades."); return; }
        if (client.deleteMagazine(m.getId())) { System.out.println("\"" + m.getTitle() + "\" togs bort!"); magazines = client.fetchMagazines(); }
        else System.out.println("Fel: kunde inte ta bort.");
    }

    static void taBortAnvändare() {
        System.out.print("Email: "); User u = client.findUserByEmail(users, scanner.nextLine());
        if (u == null) { System.out.println("Ingen användare hittades."); return; }
        if (client.deleteUser(u.getId())) { System.out.println(u.getName() + " togs bort!"); users = client.fetchUsers(); }
        else System.out.println("Fel: kunde inte ta bort.");
    }

    static void hävAvstängning() {
        System.out.print("Email: "); User u = client.findUserByEmail(users, scanner.nextLine());
        if (u == null) { System.out.println("Ingen användare hittades."); return; }
        SuspendedUser träff = null;
        for (SuspendedUser s : suspended) { if (s.getUserId().equals(u.getId())) { träff = s; break; } }
        if (träff == null) { System.out.println(u.getName() + " är inte avstängd."); return; }
        if (client.deleteSuspendedUser(träff.getId())) {
            System.out.println("Avstängningen för " + u.getName() + " är hävd!"); suspended = client.fetchSuspendedUsers();
        } else System.out.println("Fel: kunde inte häva avstängningen.");
    }

    // ==================== HITTA ====================

    static void menyHitta() {
        System.out.println("\n--- Hitta ---");
        System.out.println("1. Bok  2. Tidning  3. Användare  4. Media");
        System.out.print("Välj: ");
        switch (scanner.nextLine().trim()) {
            case "1" -> { System.out.print("Titel: "); Book b = client.findBookByTitle(books, scanner.nextLine());
                          System.out.println(b != null ? "Hittad: " + b : "Ingen bok hittades."); }
            case "2" -> { System.out.print("Titel: "); Magazine m = client.findMagazineByTitle(magazines, scanner.nextLine());
                          System.out.println(m != null ? "Hittad: " + m : "Ingen tidning hittades."); }
            case "3" -> { System.out.print("Email: "); User u = client.findUserByEmail(users, scanner.nextLine());
                          System.out.println(u != null ? "Hittad: " + u : "Ingen användare hittades."); }
            case "4" -> { System.out.print("Titel: "); Media med = client.findMediaByTitle(mediaList, scanner.nextLine());
                          System.out.println(med != null ? "Hittad: " + med : "Ingen media hittades."); }
            default  -> System.out.println("Ogiltigt val.");
        }
    }

    // ==================== SKRIV UT ====================

    static void menySkrivUt() {
        System.out.println("\n--- Skriv ut ---");
        System.out.println("1. Böcker  2. Tidningar  3. Användare  4. Avstängda  5. Media  6. Aktiva lån");
        System.out.print("Välj: ");
        switch (scanner.nextLine().trim()) {
            case "1" -> skrivUtLista("BÖCKER", books);
            case "2" -> skrivUtLista("TIDNINGAR", magazines);
            case "3" -> skrivUtLista("ANVÄNDARE", users);
            case "4" -> skrivUtLista("AVSTÄNGDA", suspended);
            case "5" -> skrivUtLista("MEDIA", mediaList);
            case "6" -> { ArrayList<Loan> lån = loanManager.getAllLoans();
                          if (lån.isEmpty()) System.out.println("Inga aktiva lån.");
                          else { System.out.println("\n--- AKTIVA LÅN ---"); for (Loan l : lån) System.out.println(l); } }
            default  -> System.out.println("Ogiltigt val.");
        }
    }

    static void skrivUtLista(String rubrik, ArrayList<?> lista) {
        if (lista.isEmpty()) { System.out.println("Inga poster laddade."); return; }
        System.out.println("\n--- " + rubrik + " ---");
        for (Object o : lista) System.out.println(o);
    }

    // ==================== KAN LÅNA ====================

    static void menyKanLåna() {
        System.out.print("Användarens email: "); User u = client.findUserByEmail(users, scanner.nextLine());
        if (u == null) { System.out.println("Ingen användare hittades."); return; }
        if (canUserBorrow(u.getId())) System.out.println(u.getName() + " är aktiv och får låna.");
        else System.out.println(u.getName() + " är AVSTÄNGD och får INTE låna.");
    }

    static boolean canUserBorrow(String userId) {
        return !client.isUserSuspended(userId, suspended);
    }

    // ==================== MEDIA ====================

    static void menyMedia() {
        System.out.println("\n--- Media ---");
        System.out.println("1. Hämta  2. Lägg till spel  3. Lägg till album  4. Lägg till film  5. Ta bort  6. Skriv ut");
        System.out.print("Välj: ");
        switch (scanner.nextLine().trim()) {
            case "1" -> mediaList = client.fetchMedia();
            case "2" -> läggTillSpel();
            case "3" -> läggTillAlbum();
            case "4" -> läggTillFilm();
            case "5" -> { System.out.print("Titel: "); Media med = client.findMediaByTitle(mediaList, scanner.nextLine());
                          if (med == null) { System.out.println("Ingen media hittades."); break; }
                          if (client.deleteMedia(med.getId())) { System.out.println("\"" + med.getTitle() + "\" togs bort!"); mediaList = client.fetchMedia(); }
                          else System.out.println("Fel: kunde inte ta bort."); }
            case "6" -> skrivUtLista("MEDIA", mediaList);
            default  -> System.out.println("Ogiltigt val.");
        }
    }

    static void läggTillSpel() {
        System.out.print("Titel: "); String title = scanner.nextLine();
        System.out.print("Genre: "); String genre = scanner.nextLine();
        int age = 0; System.out.print("Åldersgräns: ");
        try { age = Integer.parseInt(scanner.nextLine().trim()); } catch (NumberFormatException e) { System.out.println("Sätter till 0."); }
        if (client.addGame(new Game("", title, genre, age, true))) {
            System.out.println("Spelet lades till!"); mediaList = client.fetchMedia();
        } else System.out.println("Fel: kunde inte lägga till spelet.");
    }

    static void läggTillAlbum() {
        System.out.print("Titel: ");  String title  = scanner.nextLine();
        System.out.print("Artist: "); String artist = scanner.nextLine();
        if (client.addMusicAlbum(new MusicAlbum("", artist, title, true))) {
            System.out.println("Albumet lades till!"); mediaList = client.fetchMedia();
        } else System.out.println("Fel: kunde inte lägga till albumet.");
    }

    static void läggTillFilm() {
        System.out.print("Titel: "); String title = scanner.nextLine();
        System.out.print("Genre: "); String genre = scanner.nextLine();
        int min = 0; System.out.print("Minuter: ");
        try { min = Integer.parseInt(scanner.nextLine().trim()); } catch (NumberFormatException e) { System.out.println("Sätter till 0."); }
        if (client.addMovie(new Movie("", title, genre, min, true))) {
            System.out.println("Filmen lades till!"); mediaList = client.fetchMedia();
        } else System.out.println("Fel: kunde inte lägga till filmen.");
    }

    // ==================== UTLÅNING ====================

    static void menyUtlåning() {
        System.out.println("\n--- Utlåning ---");
        System.out.println("1. Låna bok  2. Låna tidning  3. Låna media");
        System.out.println("4. Lämna bok  5. Lämna tidning  6. Lämna media  7. Visa lån");
        System.out.print("Välj: ");
        switch (scanner.nextLine().trim()) {
            case "1" -> lånaBok();
            case "2" -> lånaTidning();
            case "3" -> lånaMedia();
            case "4" -> lämnaÅterBok();
            case "5" -> lämnaÅterTidning();
            case "6" -> lämnaÅterMedia();
            case "7" -> visaLånFörAnvändare();
            default  -> System.out.println("Ogiltigt val.");
        }
    }

    static User hämtaAktivAnvändare() {
        System.out.print("Användarens email: "); User u = client.findUserByEmail(users, scanner.nextLine());
        if (u == null) { System.out.println("Ingen användare hittades."); return null; }
        if (!canUserBorrow(u.getId())) { System.out.println(u.getName() + " är avstängd och får inte låna."); return null; }
        return u;
    }

    static void lånaBok() {
        User u = hämtaAktivAnvändare(); if (u == null) return;
        System.out.print("Bokens titel: "); Book b = client.findBookByTitle(books, scanner.nextLine());
        if (b == null) { System.out.println("Ingen bok hittades."); return; }
        loanManager.borrowItem(u.getId(), b);
    }

    static void lånaTidning() {
        User u = hämtaAktivAnvändare(); if (u == null) return;
        System.out.print("Tidningens titel: "); Magazine m = client.findMagazineByTitle(magazines, scanner.nextLine());
        if (m == null) { System.out.println("Ingen tidning hittades."); return; }
        loanManager.borrowItem(u.getId(), m);
    }

    static void lånaMedia() {
        User u = hämtaAktivAnvändare(); if (u == null) return;
        System.out.print("Mediats titel: "); Media med = client.findMediaByTitle(mediaList, scanner.nextLine());
        if (med == null) { System.out.println("Ingen media hittades."); return; }
        loanManager.borrowItem(u.getId(), med);
    }

    static void lämnaÅterBok() {
        System.out.print("Email: "); User u = client.findUserByEmail(users, scanner.nextLine()); if (u == null) { System.out.println("Ingen användare hittades."); return; }
        System.out.print("Bokens titel: "); Book b = client.findBookByTitle(books, scanner.nextLine()); if (b == null) { System.out.println("Ingen bok hittades."); return; }
        loanManager.returnItem(u.getId(), b);
    }

    static void lämnaÅterTidning() {
        System.out.print("Email: "); User u = client.findUserByEmail(users, scanner.nextLine()); if (u == null) { System.out.println("Ingen användare hittades."); return; }
        System.out.print("Tidningens titel: "); Magazine m = client.findMagazineByTitle(magazines, scanner.nextLine()); if (m == null) { System.out.println("Ingen tidning hittades."); return; }
        loanManager.returnItem(u.getId(), m);
    }

    static void lämnaÅterMedia() {
        System.out.print("Email: "); User u = client.findUserByEmail(users, scanner.nextLine()); if (u == null) { System.out.println("Ingen användare hittades."); return; }
        System.out.print("Mediats titel: "); Media med = client.findMediaByTitle(mediaList, scanner.nextLine()); if (med == null) { System.out.println("Ingen media hittades."); return; }
        loanManager.returnItem(u.getId(), med);
    }

    static void visaLånFörAnvändare() {
        System.out.print("Email: "); User u = client.findUserByEmail(users, scanner.nextLine());
        if (u == null) { System.out.println("Ingen användare hittades."); return; }
        ArrayList<Loan> lån = loanManager.getLoansByUser(u.getId());
        if (lån.isEmpty()) System.out.println(u.getName() + " har inga aktiva lån.");
        else { System.out.println("\n--- Lån för " + u.getName() + " ---"); for (Loan l : lån) System.out.println(l); }
    }

    // ==================== SÖK OCH FILTRERA (STREAMS) ====================

    static void menySök() {
        System.out.println("\n--- Sök och filtrera ---");
        System.out.println("1. Filtrera böcker på genre");
        System.out.println("2. Filtrera böcker på författare");
        System.out.println("3. Sortera böcker på författare");
        System.out.println("4. Sortera böcker på genre");
        System.out.println("5. Räkna böcker av en författare");
        System.out.println("6. Visa alla boktitlar (map)");
        System.out.print("Välj: ");

        switch (scanner.nextLine().trim()) {
            case "1" -> { System.out.print("Genre: "); skrivUtStreamResultat("Genre-sökning", StreamHelper.filterByGenre(books, scanner.nextLine())); }
            case "2" -> { System.out.print("Författare: "); skrivUtStreamResultat("Sökning på författare", StreamHelper.filterByAuthor(books, scanner.nextLine())); }
            case "3" -> skrivUtStreamResultat("Böcker sorterade på författare", StreamHelper.sortByAuthor(books));
            case "4" -> skrivUtStreamResultat("Böcker sorterade på genre", StreamHelper.sortByGenre(books));
            case "5" -> { System.out.print("Författare: "); String a = scanner.nextLine();
                          System.out.println(a + " har " + StreamHelper.countByAuthor(books, a) + " bok/böcker i systemet."); }
            case "6" -> { List<String> titlar = StreamHelper.getAllTitles(books);
                          System.out.println("\n--- Alla boktitlar ---"); titlar.forEach(System.out::println); }
            default  -> System.out.println("Ogiltigt val.");
        }
    }

    static void skrivUtStreamResultat(String rubrik, List<?> result) {
        System.out.println("\n--- " + rubrik + " ---");
        if (result.isEmpty()) System.out.println("Inga träffar hittades.");
        else result.forEach(System.out::println);
    }
}
