// Imad Karahodza - representerar ett enskilt lån (kund + objekt)
package imad.sida;

import java.time.LocalDate;

public class Loan {

    private String loanId;
    private String userId;   // vem som lånade
    private String itemId;   // vilket objekt som lånades
    private String itemType; // "book", "magazine", "game", "music_album", "movie"
    private String loanDate; // datum då lånet skapades

    // konstruktor med validering
    public Loan(String loanId, String userId, String itemId, String itemType) {
        // validering — id:n får inte vara tomma
        this.loanId   = (loanId != null && !loanId.isEmpty())   ? loanId   : "okänt-id";
        this.userId   = (userId != null && !userId.isEmpty())   ? userId   : "okänd-användare";
        this.itemId   = (itemId != null && !itemId.isEmpty())   ? itemId   : "okänt-objekt";
        this.itemType = (itemType != null && !itemType.isEmpty()) ? itemType : "okänd";

        // sätter dagens datum automatiskt
        this.loanDate = LocalDate.now().toString();
    }

    // konstruktor som används när man läser in lån från fil (datum redan känt)
    public Loan(String loanId, String userId, String itemId, String itemType, String loanDate) {
        this.loanId   = loanId;
        this.userId   = userId;
        this.itemId   = itemId;
        this.itemType = itemType;
        this.loanDate = loanDate;
    }

    // getters
    public String getLoanId()   { return loanId; }
    public String getUserId()   { return userId; }
    public String getItemId()   { return itemId; }
    public String getItemType() { return itemType; }
    public String getLoanDate() { return loanDate; }

    // toString används för att spara lånet som en rad i filen
    // format: loanId,userId,itemId,itemType,loanDate
    @Override
    public String toString() {
        return loanId + "," + userId + "," + itemId + "," + itemType + "," + loanDate;
    }
}
