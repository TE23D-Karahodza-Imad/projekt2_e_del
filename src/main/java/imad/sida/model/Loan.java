package imad.sida.model;

import java.time.LocalDate;

/**
 * Representerar ett enskilt lån — kopplar en användare till ett lånat objekt.
 * Sparas som en kommaseparerad rad i loans.txt.
 *
 */
public class Loan {

    /** Unikt id för detta lån. */
    private String loanId;

    /** Id på den användare som lånade. */
    private String userId;

    /** Id på det objekt som lånades. */
    private String itemId;

    /** Typ av objekt: "book", "magazine", "game", "musicalbum", "movie". */
    private String itemType;

    /** Datum då lånet skapades. */
    private String loanDate;

    /**
     * Skapar ett nytt lån. Datumet sätts automatiskt till idag.
     * @param loanId   unikt lån-id
     * @param userId   id på låntagaren
     * @param itemId   id på objektet som lånas
     * @param itemType typ av objekt
     */
    public Loan(String loanId, String userId, String itemId, String itemType) {
        this.loanId   = (loanId   != null && !loanId.isEmpty())   ? loanId   : "okänt-id";
        this.userId   = (userId   != null && !userId.isEmpty())   ? userId   : "okänd-användare";
        this.itemId   = (itemId   != null && !itemId.isEmpty())   ? itemId   : "okänt-objekt";
        this.itemType = (itemType != null && !itemType.isEmpty()) ? itemType : "okänd";
        this.loanDate = LocalDate.now().toString();
    }

    /**
     * Skapar ett lån med känt datum — används vid inläsning från fil.
     * @param loanId   lån-id
     * @param userId   användare-id
     * @param itemId   objekt-id
     * @param itemType typ
     * @param loanDate datum som sträng (YYYY-MM-DD)
     */
    public Loan(String loanId, String userId, String itemId, String itemType, String loanDate) {
        this.loanId   = loanId;
        this.userId   = userId;
        this.itemId   = itemId;
        this.itemType = itemType;
        this.loanDate = loanDate;
    }

    /** @return lånets id */
    public String getLoanId()   { return loanId; }

    /** @return låntagarens id */
    public String getUserId()   { return userId; }

    /** @return det lånade objektets id */
    public String getItemId()   { return itemId; }

    /** @return objektets typ */
    public String getItemType() { return itemType; }

    /** @return datum för lånet */
    public String getLoanDate() { return loanDate; }

    /**
     * Returnerar lånet som en kommaseparerad rad för fillagring.
     * Format: loanId,userId,itemId,itemType,loanDate
     */
    @Override
    public String toString() {
        return loanId + "," + userId + "," + itemId + "," + itemType + "," + loanDate;
    }
}
