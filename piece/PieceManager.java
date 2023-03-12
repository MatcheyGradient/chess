package piece;

import listeners.ClickListener;
import listeners.MouseTracker;
import resources.ChessBoardUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class PieceManager {

    public static java.util.List<Piece> activePieces = new ArrayList<>();
    public static Piece selectedPiece;
    public static boolean selected;

    public static boolean move = false;
    static boolean checkmate = false;

    public static void selectPiece(int x, int y){
        int mx = x - 50;
        int my = y - 50;

        activePieces.forEach(p -> {
            if(p.rank == mx / 100 && (8 - p.file) == my / 100 && p.color == move){
                selectedPiece = p;
                selected = true;
            }
        });

        if(selectedPiece != null){
            selectedPiece.generateLegalMoves();
            selectedPiece.filterCheckMoves();
        } else {
            ClickListener.highlights.clear();
        }
    }

    public static void placePiece(int x, int y){
        AtomicBoolean illegal = new AtomicBoolean(false);
        if(selectedPiece != null){
            int mx = x - 50;
            int my = y - 50;

            selectedPiece.legalMoves.clear();
            selectedPiece.generateLegalMoves();
            selectedPiece.filterCheckMoves();

            int newRank = mx / 100;
            int newFile = 8 - (my / 100);

            if(!selectedPiece.containsMove(newRank, newFile)) illegal.set(true);

            if(!illegal.get()){
                activePieces.forEach(p -> {
                    if (p.rank == newRank && p.file == newFile && selectedPiece.color == p.color){
                        illegal.set(true);
                    }
                    p.tags.remove("enpassant");
                });


                if(Objects.equals(selectedPiece.type, "k")){
                    int diff = newRank - selectedPiece.rank;
                    if(Math.abs(diff) == 2){
                        selectedPiece.castle(diff);
                    }
                }
                if(Objects.equals(selectedPiece.type, "p")){
                    int diff = newFile - selectedPiece.file;
                    if(Math.abs(diff) == 2){
                        selectedPiece.tags.add("enpassant");
                    }

                    int rankDiff = newRank - selectedPiece.rank;
                    if (rankDiff == -1){
                        if(!occupied(selectedPiece.rank - 1, selectedPiece.file + (selectedPiece.color ? -1 : 1)) && occupied(selectedPiece.rank - 1, selectedPiece.file)){
                            activePieces.remove(getPiece(selectedPiece.rank - 1, selectedPiece.file));
                        }
                    }
                    if (rankDiff == 1){
                        if(!occupied(selectedPiece.rank + 1, selectedPiece.file + (selectedPiece.color ? -1 : 1)) && occupied(selectedPiece.rank + 1, selectedPiece.file)){
                            activePieces.remove(getPiece(selectedPiece.rank + 1, selectedPiece.file));
                        }
                    }
                }

                selectedPiece.file = newFile;
                selectedPiece.rank = newRank;

                if(Objects.equals(selectedPiece.type, "p")){
                    if(selectedPiece.file == (move ? 1 : 8)){
                        selectedPiece.promote();
                    }
                }

                selectedPiece.setTags();
                move = !move;
            }

            selectedPiece.legalMoves.clear();
            activePieces.removeIf(p -> p.rank == selectedPiece.rank && p.file == selectedPiece.file && p.color != selectedPiece.color);

            ChessBoardUI.updateChecks();
        }
    }

    public static void drawSelected(Graphics g){
        g.setColor(new Color(0, 0, 0, 105));
        selectedPiece.legalMoves.forEach(m -> {
            g.fillOval(m[0] * 100 + 85, (8 - m[1]) * 100 + 85, 30, 30);
        });

        selectedPiece.drawPiece(g, false, MouseTracker.mx, MouseTracker.my);
    }

    public static boolean occupied(int rank, int file){
        if(file > 8 || file < 1 || rank < 0 || rank > 7) return true;

        for (Piece activePiece : activePieces) {
            if(activePiece.file == file && activePiece.rank == rank){
                return true;
            }
        }
        return false;
    }

    public static boolean takeable(int rank, int file, boolean color){
        if(file > 8 || file < 1 || rank < 0 || rank > 7) return false;

        for (Piece activePiece : activePieces) {
            if(activePiece.file == file && activePiece.rank == rank && activePiece.color != color){
                return true;
            }
        }
        return false;
    }

    public static boolean freeMove(int rank, int file, boolean color){
        if(file > 8 || file < 1 || rank < 0 || rank > 7) return false;

        for (Piece activePiece : activePieces) {
            if(activePiece.file == file && activePiece.rank == rank && activePiece.color == color){
                return false;
            }
        }
        return true;
    }

    public static Piece getPiece(int rank, int file){
        for (Piece activePiece : activePieces) {
            if(activePiece.rank == rank && activePiece.file == file){
                return activePiece;
            }
        }
        return null;
    }

    static int[] getKing(boolean color){
        for (Piece activePiece : activePieces) {
            if(activePiece.color == color && Objects.equals(activePiece.type, "k")) return new int[]{activePiece.rank, activePiece.file};
        }
        return null;
    }

    static boolean attacked(int rank, int file, boolean color){
        AtomicBoolean returnVal = new AtomicBoolean(false);

        activePieces.forEach(p -> {
            if(p.color == color){
                p.legalMoves.clear();
                p.generateNoKingMoves();
                p.legalMoves.forEach(m -> {
                    if (m[0] == rank && m[1] == file && p.color == color){
                        returnVal.set(true);
                    }
                });
            }
        });

        return returnVal.get();
    }

    public static boolean checkForCheck(boolean color) {
        int[] kingPos = getKing(!color);
        AtomicBoolean returnVal = new AtomicBoolean(false);

        activePieces.forEach(p -> {
            if (p.color == color) {
                p.generateLegalMoves();
                p.legalMoves.forEach(m -> {
                    if (m[0] == kingPos[0] && m[1] == kingPos[1]) {
                        returnVal.set(true);
                    }
                });
            }
            p.legalMoves.clear();
        });

        return returnVal.get();
    }

    public static boolean checkForCheckmate(boolean color){
        List<int[]> totalMoves = new ArrayList<>();
        List<Piece> copied = new ArrayList<>(activePieces);

        copied.forEach(p -> {
            if(p.color == color){
                getPiece(p.rank, p.file).generateLegalMoves();
                getPiece(p.rank, p.file).filterCheckMoves();
                totalMoves.addAll(getPiece(p.rank, p.file).legalMoves);

                getPiece(p.rank, p.file).legalMoves.clear();
            }
        });

        return totalMoves.size() == 0;
    }
}
