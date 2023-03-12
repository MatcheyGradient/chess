package resources;

import piece.Piece;
import piece.PieceImages;
import piece.PieceManager;

import java.awt.*;

public class ChessBoardUI {

    static boolean checked = false;
    public static boolean color = false;
    static boolean checkmate = false;
    public static boolean showPromote = false;
    public static int rank = 0;
    static boolean promoteColor = false;
    public static Piece pi;

    public static void updateChecks(){
        checkmate = false;
        checked = false;

        checkmate = PieceManager.checkForCheckmate(PieceManager.move);
        checked = PieceManager.checkForCheck(!PieceManager.move);
        color = PieceManager.move;
    }

    public static void render(Graphics g){
        g.setFont(new Font("Arial", Font.BOLD, 40));

        g.setColor(new Color(120, 70, 34, 255));

        g.fillRoundRect(870, 40, 320, 120, 10, 10);

        g.setColor(color ? Color.black : Color.white);
        if(checked){
            g.drawString(color ? "Black" : "White", 1030 - g.getFontMetrics().stringWidth(color ? "Black" : "White") / 2, 90);
        }

        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.setColor(Color.white);
        if(checkmate && checked) {
            g.drawString("has been checkmated", 1030 - g.getFontMetrics().stringWidth("has been checkmated") / 2, 130);
        } else if (checked){
            g.drawString("has been checked", 1030 - g.getFontMetrics().stringWidth("has been checked") / 2, 130);
        }

        if(checkmate && !checked){
            g.drawString("Stalemate", 1030 - g.getFontMetrics().stringWidth("Stalemate") / 2, 120);
        }

        g.setColor(new Color(0, 0, 0, 123));
        if(showPromote){
            g.fillRect(50, 50, 800, 800);
            g.setColor(Color.white);
            g.fillRect(50 + rank * 100, 50 + (color ? 0 : 400), 100, 400);

            if(color){
                g.drawImage(PieceImages.images[0][4], 50 + rank * 100, 50, null);
                g.drawImage(PieceImages.images[color ? 0 : 1][3], 50 + rank * 100, 150, null);
                g.drawImage(PieceImages.images[color ? 0 : 1][2], 50 + rank * 100, 250, null);
                g.drawImage(PieceImages.images[color ? 0 : 1][1], 50 + rank * 100, 350, null);
            } else {
                g.drawImage(PieceImages.images[1][4], 50 + rank * 100, 750, null);
                g.drawImage(PieceImages.images[color ? 0 : 1][3], 50 + rank * 100, 650, null);
                g.drawImage(PieceImages.images[color ? 0 : 1][2], 50 + rank * 100, 550, null);
                g.drawImage(PieceImages.images[color ? 0 : 1][1], 50 + rank * 100, 450, null);
            }
        }
    }

    public static void promote(Piece p){
        showPromote = true;
        rank = p.rank;
        promoteColor = p.color;
        pi = p;
    }
}
