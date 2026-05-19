// Imad Karahodza - main klass med meny för bibliotekssystemet (A-nivå)
package imad.sida;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main är programmets startpunkt.
 * Hanterar all menynavigering och kopplar ihop alla klasser.
 */
public class Main {

    // lokala samlingar för alla datatyper
    static ArrayList<Book>          books     = new ArrayList<>();
    static ArrayList<Magazine>      magazines = new ArrayList<>();
    static ArrayList<User>          users     = new ArrayList<>();
    static ArrayList<SuspendedUser> suspended = new ArrayList<>();
    static ArrayList<Media>         mediaList = new ArrayList<>();

    static LibraryClient client      = new LibraryClient();
    static LoanManager   loanManager = new LoanManager(); // läser loans.txt vid start
    static Scanner       scanner     = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== LibSys A-nivå ===");

        // hämtar all data från servern direkt när programmet startar
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
                case "10" -> {
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
        System.out.println("1. Böcker");
        System.out.println("2. Tidningar");
        System.out.println("3. Användare");
        System.out.println("4. Avstängda användare");
        System.out.println("5. Media");
        System.out.println("6. Allt");
        System.out.print("Välj: ");
        String val = scanner.nextLine().trim();

        switch (val) {
            case "1" -> books     = client.fetchBooks();
            case "2" -> magazines = client.fetchMagazines();
            case "3" -> users     = client.fetchUsers();
            case "4" -> suspended = client.fetchSuspendedUsers();
            case "5" -> mediaList = client.fetchMedia();
            case "6" -> {
                books     = client.fetchBooks();
                magazines = client.fetchMagazines();
                users     = client.fetchUsers();
                suspended = client.fetchSuspendedUsers();
                mediaList = client.fetchMedia();
                System.out.println("All data hämtad!");
            }
            default -> System.out.println("Ogiltigt val.");
        }
    }

    // ==================== SKAPA ====================

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

    static void skapaBok() {
        System.out.println("\n--- Ny bok ---");
        System.out.print("Titel: ");    String title  = scanner.nextLine();
        System.out.print("Författare: "); String author = scanner.nextLine();
        System.out.print("Genre: ");    String genre  = scanner.nextLine();
        int pages = 0;
        System.out.print("Sidor: ");
        try { pages = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Ogiltigt antal sidor, sätter till 0."); }

        Book bok = new Book("", title, author, genre, pages, true);
        if (client.addBook(bok)) {
            System.out.println("Boken lades till!");
            books = client.fetchBooks();
        } else System.out.println("Fel: kunde inte lägga till boken.");
    }

    static void skapaTidning() {
        System.out.println("\n--- Ny tidning ---");
        System.out.print("Titel: "); String title = scanner.nextLine();
        int nummer = 0;
        System.out.print("Nummer: ");
        try { nummer = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Ogiltigt nummer, sätter till 0."); }
        System.out.print("Kategori: "); String category = scanner.nextLine();
        int år = 0;
        System.out.print("Utgivningsår: ");
        try { år = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Ogiltigt år, sätter till 0."); }

        Magazine tidning = new Magazine("", title, nummer, category, år, true);
        if (client.addMagazine(tidning)) {
            System.out.println("Tidningen lades till!");
            magazines = client.fetchMagazines();
        } else System.out.println("Fel: kunde inte lägga till tidningen.");
    }

    static void skapaAnvändare() {
        System.out.println("\n--- Ny användare ---");
        System.out.print("Namn: ");  String name  = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();

        User user = new User("", name, email);
        if (client.addUser(user)) {
            System.out.println("Användaren lades till!");
            users = client.fetchUsers();
        } else System.out.println("Fel: kunde inte lägga till användaren.");
    }

    static void stängAvAnvändare() {
        System.out.println("\n--- Stäng av användare ---");
        System.out.print("Email: "); String email = scanner.nextLine();

        User user = client.findUserByEmail(users, email);
        if (user == null) { System.out.println("Ingen användare hittades."); return; }
        if (client.isUserSuspended(user.getId(), suspended)) {
            System.out.println(user.getName() + " är redan avstängd."); return;
        }
        if (client.addSuspendedUser(user.getId())) {
            System.out.println(user.getName() + " är nu avstängd!");
            suspended = client.fetchSuspendedUsers();
        } else System.out.println("Fel: kunde inte stänga av användaren.");
    }

    // ==================== TA BORT ====================

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

    static void taBortBok() {
        System.out.print("Bokens titel: "); String title = scanner.nextLine();
        Book bok = client.findBookByTitle(books, title);
        if (bok == null) { System.out.println("Ingen bok hittades med den titeln."); return; }
        if (client.deleteBook(bok.getId())) {
            System.out.println("\"" + bok.getTitle() + "\" togs bort!");
            books = client.fetchBooks();
        } else System.out.println("Fel: kunde inte ta bort boken.");
    }

    static void taBortTidning() {
        System.out.print("Tidningens titel: "); String title = scanner.nextLine();
        Magazine tidning = client.findMagazineByTitle(magazines, title);
        if (tidning == null) { System.out.println("Ingen tidning hittades med den titeln."); return; }
        if (client.deleteMagazine(tidning.getId())) {
            System.out.println("\"" + tidning.getTitle() + "\" togs bort!");
            magazines = client.fetchMagazines();
        } else System.out.println("Fel: kunde inte ta bort tidningen.");
    }

    static void taBortAnvändare() {
        System.out.print("Användarens email: "); String email = scanner.nextLine();
        User user = client.findUserByEmail(users, email);
        if (user == null) { System.out.println("Ingen användare hittades."); return; }
        if (client.deleteUser(user.getId())) {
            System.out.println(user.getName() + " togs bort!");
            users = client.fetchUsers();
        } else System.out.println("Fel: kunde inte ta bort användaren.");
    }

    static void hävAvstängning() {
        System.out.print("Användarens email: "); String email = scanner.nextLine();
        User user = client.findUserByEmail(users, email);
        if (user == null) { System.out.println("Ingen användare hittades."); return; }

        SuspendedUser träff = null;
        for (SuspendedUser s : suspended) {
            if (s.getUserId().equals(user.getId())) { träff = s; break; }
        }
        if (träff == null) { System.out.println(user.getName() + " är inte avstängd."); return; }

        if (client.deleteSuspendedUser(träff.getId())) {
            System.out.println("Avstängningen för " + user.getName() + " är hävd!");
            suspended = client.fetchSuspendedUsers();
        } else System.out.println("Fel: kunde inte häva avstängningen.");
    }

    // ==================== HITTA ====================

    static void menyHitta() {
        System.out.println("\n--- Hitta ---");
        System.out.println("1. Bok (via titel)");
        System.out.println("2. Tidning (via titel)");
        System.out.println("3. Användare (via email)");
        System.out.println("4. Media (via titel)");
        System.out.print("Välj: ");
        String val = scanner.nextLine().trim();

        switch (val) {
            case "1" -> {
                System.out.print("Titel: "); String t = scanner.nextLine();
                Book b = client.findBookByTitle(books, t);
                System.out.println(b != null ? "Hittad: " + b : "Ingen bok hittades.");
            }
            case "2" -> {
                System.out.print("Titel: "); String t = scanner.nextLine();
                Magazine m = client.findMagazineByTitle(magazines, t);
                System.out.println(m != null ? "Hittad: " + m : "Ingen tidning hittades.");
            }
            case "3" -> {
                System.out.print("Email: "); String e = scanner.nextLine();
                User u = client.findUserByEmail(users, e);
                System.out.println(u != null ? "Hittad: " + u : "Ingen användare hittades.");
            }
            case "4" -> {
                System.out.print("Titel: "); String t = scanner.nextLine();
                Media med = client.findMediaByTitle(mediaList, t);
                System.out.println(med != null ? "Hittad: " + med : "Ingen media hittades.");
            }
            default -> System.out.println("Ogiltigt val.");
        }
    }

    // ==================== SKRIV UT ====================

    static void menySkrivUt() {
        System.out.println("\n--- Skriv ut ---");
        System.out.println("1. Böcker");
        System.out.println("2. Tidningar");
        System.out.println("3. Användare");
        System.out.println("4. Avstängda användare");
        System.out.println("5. Media");
        System.out.println("6. Aktiva lån");
        System.out.print("Välj: ");
        String val = scanner.nextLine().trim();

        switch (val) {
            case "1" -> skrivUtLista("BÖCKER", books);
            case "2" -> skrivUtLista("TIDNINGAR", magazines);
            case "3" -> skrivUtLista("ANVÄNDARE", users);
            case "4" -> skrivUtLista("AVSTÄNGDA", suspended);
            case "5" -> skrivUtLista("MEDIA", mediaList);
            case "6" -> {
                ArrayList<Loan> lån = loanManager.getAllLoans();
                if (lån.isEmpty()) System.out.println("Inga aktiva lån.");
                else { System.out.println("\n--- AKTIVA LÅN ---"); for (Loan l : lån) System.out.println(l); }
            }
            default -> System.out.println("Ogiltigt val.");
        }
    }

    // hjälpmetod — skriver ut en lista med valfri rubrik
    static void skrivUtLista(String rubrik, ArrayList<?> lista) {
        if (lista.isEmpty()) { System.out.println("Inga poster laddade."); return; }
        System.out.println("\n--- " + rubrik + " ---");
        for (Object o : lista) System.out.println(o);
    }

    // ==================== KAN LÅNA ====================

    static void menyKanLåna() {
        System.out.println("\n--- Kolla lånestatus ---");
        System.out.print("Användarens email: "); String email = scanner.nextLine();
        User user = client.findUserByEmail(users, email);
        if (user == null) { System.out.println("Ingen användare hittades."); return; }

        if (canUserBorrow(user.getId())) {
            System.out.println(user.getName() + " är aktiv och får låna.");
        } else {
            System.out.println(user.getName() + " är AVSTÄNGD och får INTE låna.");
        }
    }

    // returnerar true om användaren inte är avstängd
    static boolean canUserBorrow(String userId) {
        return !client.isUserSuspended(userId, suspended);
    }

    // ==================== MEDIA ====================

    /**
     * Submeny för att hantera media (spel, musik, film).
     */
    static void menyMedia() {
        System.out.println("\n--- Media ---");
        System.out.println("1. Hämta all media");
        System.out.println("2. Lägg till spel");
        System.out.println("3. Lägg till musikalbum");
        System.out.println("4. Lägg till film");
        System.out.println("5. Ta bort media (via titel)");
        System.out.println("6. Skriv ut all media");
        System.out.print("Välj: ");
        String val = scanner.nextLine().trim();

        switch (val) {
            case "1" -> mediaList = client.fetchMedia();
            case "2" -> läggTillSpel();
            case "3" -> läggTillAlbum();
            case "4" -> läggTillFilm();
            case "5" -> {
                System.out.print("Titel: "); String t = scanner.nextLine();
                Media med = client.findMediaByTitle(mediaList, t);
                if (med == null) { System.out.println("Ingen media hittades."); break; }
                if (client.deleteMedia(med.getId())) {
                    System.out.println("\"" + med.getTitle() + "\" togs bort!");
                    mediaList = client.fetchMedia();
                } else System.out.println("Fel: kunde inte ta bort.");
            }
            case "6" -> skrivUtLista("MEDIA", mediaList);
            default  -> System.out.println("Ogiltigt val.");
        }
    }

    static void läggTillSpel() {
        System.out.println("\n--- Nytt spel ---");
        System.out.print("Titel: ");       String title = scanner.nextLine();
        System.out.print("Genre: ");       String genre = scanner.nextLine();
        int age = 0;
        System.out.print("Åldersgräns: ");
        try { age = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Ogiltigt, sätter till 0."); }

        Game spel = new Game("", title, genre, age, true);
        if (client.addGame(spel)) {
            System.out.println("Spelet lades till!");
            mediaList = client.fetchMedia();
        } else System.out.println("Fel: kunde inte lägga till spelet.");
    }

    static void läggTillAlbum() {
        System.out.println("\n--- Nytt musikalbum ---");
        System.out.print("Titel: ");    String title  = scanner.nextLine();
        System.out.print("Artist: ");   String artist = scanner.nextLine();

        MusicAlbum album = new MusicAlbum("", artist, title, true);
        if (client.addMusicAlbum(album)) {
            System.out.println("Albumet lades till!");
            mediaList = client.fetchMedia();
        } else System.out.println("Fel: kunde inte lägga till albumet.");
    }

    static void läggTillFilm() {
        System.out.println("\n--- Ny film ---");
        System.out.print("Titel: "); String title = scanner.nextLine();
        System.out.print("Genre: "); String genre = scanner.nextLine();
        int minutes = 0;
        System.out.print("Minuter: ");
        try { minutes = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Ogiltigt, sätter till 0."); }

        Movie film = new Movie("", title, genre, minutes, true);
        if (client.addMovie(film)) {
            System.out.println("Filmen lades till!");
            mediaList = client.fetchMedia();
        } else System.out.println("Fel: kunde inte lägga till filmen.");
    }

    // ==================== UTLÅNING ====================

    /**
     * Submeny för att låna och lämna tillbaka objekt.
     */
    static void menyUtlåning() {
        System.out.println("\n--- Utlåning ---");
        System.out.println("1. Låna bok");
        System.out.println("2. Låna tidning");
        System.out.println("3. Låna media");
        System.out.println("4. Lämna tillbaka bok");
        System.out.println("5. Lämna tillbaka tidning");
        System.out.println("6. Lämna tillbaka media");
        System.out.println("7. Visa lån för en användare");
        System.out.print("Välj: ");
        String val = scanner.nextLine().trim();

        switch (val) {
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

    // hjälpmetod — hämtar användare via email och kollar att de får låna
    static User hämtaAktivAnvändare() {
        System.out.print("Användarens email: "); String email = scanner.nextLine();
        User user = client.findUserByEmail(users, email);
        if (user == null) { System.out.println("Ingen användare hittades."); return null; }
        if (!canUserBorrow(user.getId())) {
            System.out.println(user.getName() + " är avstängd och får inte låna."); return null;
        }
        return user;
    }

    static void lånaBok() {
        User user = hämtaAktivAnvändare();
        if (user == null) return;
        System.out.print("Bokens titel: "); String titel = scanner.nextLine();
        Book bok = client.findBookByTitle(books, titel);
        if (bok == null) { System.out.println("Ingen bok hittades."); return; }
        loanManager.borrowItem(user.getId(), bok);
    }

    static void lånaTidning() {
        User user = hämtaAktivAnvändare();
        if (user == null) return;
        System.out.print("Tidningens titel: "); String titel = scanner.nextLine();
        Magazine tidning = client.findMagazineByTitle(magazines, titel);
        if (tidning == null) { System.out.println("Ingen tidning hittades."); return; }
        loanManager.borrowItem(user.getId(), tidning);
    }

    static void lånaMedia() {
        User user = hämtaAktivAnvändare();
        if (user == null) return;
        System.out.print("Mediats titel: "); String titel = scanner.nextLine();
        Media med = client.findMediaByTitle(mediaList, titel);
        if (med == null) { System.out.println("Ingen media hittades."); return; }
        loanManager.borrowItem(user.getId(), med);
    }

    static void lämnaÅterBok() {
        System.out.print("Användarens email: "); String email = scanner.nextLine();
        User user = client.findUserByEmail(users, email);
        if (user == null) { System.out.println("Ingen användare hittades."); return; }
        System.out.print("Bokens titel: "); String titel = scanner.nextLine();
        Book bok = client.findBookByTitle(books, titel);
        if (bok == null) { System.out.println("Ingen bok hittades."); return; }
        loanManager.returnItem(user.getId(), bok);
    }

    static void lämnaÅterTidning() {
        System.out.print("Användarens email: "); String email = scanner.nextLine();
        User user = client.findUserByEmail(users, email);
        if (user == null) { System.out.println("Ingen användare hittades."); return; }
        System.out.print("Tidningens titel: "); String titel = scanner.nextLine();
        Magazine tidning = client.findMagazineByTitle(magazines, titel);
        if (tidning == null) { System.out.println("Ingen tidning hittades."); return; }
        loanManager.returnItem(user.getId(), tidning);
    }

    static void lämnaÅterMedia() {
        System.out.print("Användarens email: "); String email = scanner.nextLine();
        User user = client.findUserByEmail(users, email);
        if (user == null) { System.out.println("Ingen användare hittades."); return; }
        System.out.print("Mediats titel: "); String titel = scanner.nextLine();
        Media med = client.findMediaByTitle(mediaList, titel);
        if (med == null) { System.out.println("Ingen media hittades."); return; }
        loanManager.returnItem(user.getId(), med);
    }

    static void visaLånFörAnvändare() {
        System.out.print("Användarens email: "); String email = scanner.nextLine();
        User user = client.findUserByEmail(users, email);
        if (user == null) { System.out.println("Ingen användare hittades."); return; }
        ArrayList<Loan> lån = loanManager.getLoansByUser(user.getId());
        if (lån.isEmpty()) System.out.println(user.getName() + " har inga aktiva lån.");
        else { System.out.println("\n--- Lån för " + user.getName() + " ---"); for (Loan l : lån) System.out.println(l); }
    }

    // ==================== SÖK OCH FILTRERA (STREAMS) ====================

    /**
     * Submeny för stream-operationer: filtrering, sortering, räkning och mappning.
     */
    static void menySök() {
        System.out.println("\n--- Sök och filtrera ---");
        System.out.println("1. Filtrera böcker på genre");
        System.out.println("2. Filtrera böcker på författare");
        System.out.println("3. Sortera böcker på författare");
        System.out.println("4. Sortera böcker på genre");
        System.out.println("5. Räkna böcker av en författare");
        System.out.println("6. Visa alla boktitlar (map)");
        System.out.print("Välj: ");
        String val = scanner.nextLine().trim();

        switch (val) {
            case "1" -> {
                System.out.print("Genre: "); String genre = scanner.nextLine();
                List<Book> result = StreamHelper.filterByGenre(books, genre);
                skrivUtStreamResultat("Böcker med genre: " + genre, result);
            }
            case "2" -> {
                System.out.print("Författare: "); String author = scanner.nextLine();
                List<Book> result = StreamHelper.filterByAuthor(books, author);
                skrivUtStreamResultat("Böcker av: " + author, result);
            }
            case "3" -> {
                List<Book> result = StreamHelper.sortByAuthor(books);
                skrivUtStreamResultat("Böcker sorterade på författare", result);
            }
            case "4" -> {
                List<Book> result = StreamHelper.sortByGenre(books);
                skrivUtStreamResultat("Böcker sorterade på genre", result);
            }
            case "5" -> {
                System.out.print("Författare: "); String author = scanner.nextLine();
                long antal = StreamHelper.countByAuthor(books, author);
                System.out.println(author + " har " + antal + " bok/böcker i systemet.");
            }
            case "6" -> {
                // map — omvandlar Book-objekt till bara deras titlar
                List<String> titlar = StreamHelper.getAllTitles(books);
                System.out.println("\n--- Alla boktitlar ---");
                titlar.forEach(System.out::println);
            }
            default -> System.out.println("Ogiltigt val.");
        }
    }

    // hjälpmetod — skriver ut resultatet från en stream-sökning
    static void skrivUtStreamResultat(String rubrik, List<?> result) {
        System.out.println("\n--- " + rubrik + " ---");
        if (result.isEmpty()) System.out.println("Inga träffar hittades.");
        else result.forEach(System.out::println);
    }
}
