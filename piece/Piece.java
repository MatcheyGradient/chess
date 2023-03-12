package piece;

import resources.ChessBoardUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Piece {

    public String type;
    public boolean color;
    int file;
    public int rank;
    public List<String> tags = new ArrayList<>();
    List<int[]> legalMoves = new ArrayList<>();

    public Piece(String type, boolean color, int rank, int file){
        this.type = type;
        this.color = color;
        this.file = file;
        this.rank = rank;
    }

    public void drawPiece(Graphics g, boolean restrict, int x, int y){
        if(restrict){
            g.drawImage(getImage(), (rank * 100) + 50, (800 - file * 100) + 50, null);
        } else {
            g.drawImage(getImage(), x - 50, y - 50, null);
        }
    }

    BufferedImage getImage(){
        return switch(type){
            case "p" -> PieceImages.images[color ? 1 : 0][0];
            case "r" -> PieceImages.images[color ? 1 : 0][1];
            case "n" -> PieceImages.images[color ? 1 : 0][2];
            case "b" -> PieceImages.images[color ? 1 : 0][3];
            case "q" -> PieceImages.images[color ? 1 : 0][4];
            case "k" -> PieceImages.images[color ? 1 : 0][5];
            default -> null;
        };
    }

    public void generateLegalMoves(){
        switch(type){
            case "p" -> pawnMoves();
            case "n" -> knightMoves();
            case "k" -> kingMoves(false);
            case "r" -> rookMoves();
            case "b" -> bishopMoves();
            case "q" -> queenMoves();
        }
    }

    public void generateNoKingMoves(){
        switch(type){
            case "p" -> pawnMoves();
            case "n" -> knightMoves();
            case "k" -> kingMoves(true);
            case "r" -> rookMoves();
            case "b" -> bishopMoves();
            case "q" -> queenMoves();
        }
    }

    void pawnMoves(){
        if(!PieceManager.occupied(rank, file + (color ? -1 : 1))){
            legalMoves.add(new int[] {rank, file + (color ? -1 : 1)});
        }
        if(!PieceManager.occupied(rank, file + (color ? -2 : 2)) && !tags.contains("pmoved")){
            if(!PieceManager.occupied(rank, file + (color ? -1 : 1))){
                legalMoves.add(new int[] {rank, file + (color ? -2 : 2)});
            }
        }
        if(PieceManager.takeable(rank - 1, file + (color ? -1 : 1), color)){
            legalMoves.add(new int[] {rank - 1, file + (color ? -1 : 1)});
        }
        if(PieceManager.takeable(rank + 1, file + (color ? -1 : 1), color)){
            legalMoves.add(new int[] {rank + 1, file + (color ? -1 : 1)});
        }

        if(PieceManager.takeable(rank - 1, file, color)){
            if (PieceManager.getPiece(rank - 1, file).tags.contains("enpassant")){
                legalMoves.add(new int[] {rank - 1, file + (color ? -1 : 1)});
            }
        }

        if(PieceManager.takeable(rank + 1, file, color)){
            if (PieceManager.getPiece(rank + 1, file).tags.contains("enpassant")){
                legalMoves.add(new int[] {rank + 1, file + (color ? -1 : 1)});
            }
        }
    }

    void knightMoves(){
        int[][] knightMoves = {{-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {-2, -1}, {-2, 1}, {2, -1}, {2, 1}};
        for(int i = 0; i < 8; i++){
            if(PieceManager.freeMove(rank + knightMoves[i][0], file + knightMoves[i][1], color)){
                legalMoves.add(new int[] {rank + knightMoves[i][0], file + knightMoves[i][1]});
            }
        }
    }

    void kingMoves(boolean restricted){
        int[][] kingMoves = {{-1, 1}, {-1, 0}, {-1, -1}, {1, 1}, {1, 0}, {1, -1}, {0, 1}, {0, -1}};
        for(int i = 0; i < 8; i++){
            if(PieceManager.freeMove(rank + kingMoves[i][0], file + kingMoves[i][1], color)){
                legalMoves.add(new int[] {rank + kingMoves[i][0], file + kingMoves[i][1]});
            }
        }

        if(!restricted){
            if(!PieceManager.occupied(rank + 1, 1) && !PieceManager.occupied(rank + 2, 1) && !color){
                if(!(PieceManager.attacked(rank, file, true) || PieceManager.attacked(rank + 1, 1, true) || PieceManager.attacked(rank + 2, 1, true))){
                    Piece rook = PieceManager.getPiece(7, 1);
                    if(rook != null){
                        if(!rook.tags.contains("rmoved") && !tags.contains("kmoved")){
                            legalMoves.add(new int[] {6, file});
                        }
                    }
                }
            }
            if(!PieceManager.occupied(rank - 1, 1) && !PieceManager.occupied(rank - 2, 1) && !PieceManager.occupied(rank - 3, 1) && !color){
                if(!(PieceManager.attacked(rank, 1, true) || PieceManager.attacked(rank - 1, 1, true) || PieceManager.attacked(rank - 2, 1, true))) {
                    Piece rook = PieceManager.getPiece(0, 1);
                    if (rook != null) {
                        if (!rook.tags.contains("rmoved") && !tags.contains("kmoved")) {
                            legalMoves.add(new int[]{2, file});
                        }
                    }
                }
            }
            if(!PieceManager.occupied(rank + 1, 8) && !PieceManager.occupied(rank + 2, 8) && color){
                if(!(PieceManager.attacked(rank, file, false) || PieceManager.attacked(rank + 1, 8, false) || PieceManager.attacked(rank + 2, 8, false))) {
                    Piece rook = PieceManager.getPiece(7, 8);
                    if (rook != null) {
                        if (!rook.tags.contains("rmoved") && !tags.contains("kmoved")) {
                            legalMoves.add(new int[]{6, file});
                        }
                    }
                }
            }
            if(!PieceManager.occupied(rank - 1, 8) && !PieceManager.occupied(rank - 2, 8) && !PieceManager.occupied(rank - 3, 8) && color){
                if(!(PieceManager.attacked(rank, file, false) || PieceManager.attacked(rank - 1, 8, false) || PieceManager.attacked(rank - 2, 8, false))) {
                    Piece rook = PieceManager.getPiece(0, 8);
                    if (rook != null) {
                        if (!rook.tags.contains("rmoved") && !tags.contains("kmoved")) {
                            legalMoves.add(new int[]{2, file});
                        }
                    }
                }
            }
        }
    }

    void castle(int dir){
        boolean direction = (dir > 0);
        PieceManager.getPiece(direction ? 7 : 0, color ? 8 : 1).rank = direction ? 5 : 3;
    }

    void rookMoves(){
        int[][] rookDirections = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        for(int k = 0; k < 4; k++){
            for(int i = 1; i < 9; i++){
                if(PieceManager.freeMove(rank + rookDirections[k][0] * i, file + rookDirections[k][1] * i, color)){
                    legalMoves.add(new int[] {rank + rookDirections[k][0] * i, file + rookDirections[k][1] * i});
                }
                if(PieceManager.occupied(rank + rookDirections[k][0] * i, file + rookDirections[k][1] * i)){
                    break;
                }
            }
        }
    }

    void bishopMoves(){
        int[][] bishopDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for(int k = 0; k < 4; k++){
            for(int i = 1; i < 9; i++){
                if(PieceManager.freeMove(rank + bishopDirections[k][0] * i, file + bishopDirections[k][1] * i, color)){
                    legalMoves.add(new int[] {rank + bishopDirections[k][0] * i, file + bishopDirections[k][1] * i});
                }
                if(PieceManager.occupied(rank + bishopDirections[k][0] * i, file + bishopDirections[k][1] * i)){
                    break;
                }
            }
        }
    }

    void queenMoves(){ // lazy ass
        bishopMoves();
        rookMoves();
    }

    void filterCheckMoves(){
        legalMoves.clear();
        generateLegalMoves();

        List<int[]> valid = new ArrayList<>();
        List<int[]> storeLegal = new ArrayList<>(legalMoves);
        int origRank = rank;
        int origFile = file;

        for (int[] legalMove : storeLegal) {
            Piece p = PieceManager.getPiece(legalMove[0], legalMove[1]);
            if(p != null){
                PieceManager.activePieces.remove(p);
            }
            this.rank = legalMove[0];
            this.file = legalMove[1];

            if(!PieceManager.checkForCheck(!color)){
                valid.add(legalMove);
            }
            if(p != null) PieceManager.activePieces.add(p);
        }

        this.rank = origRank;
        this.file = origFile;

        legalMoves.clear();
        legalMoves.addAll(valid);
    }

    boolean containsMove(int rank, int file) {
        for (int[] legalMove : legalMoves) {
            if(legalMove[0] == rank && legalMove[1] == file) return true;
        }
        return false;
    }

    void promote(){
        ChessBoardUI.promote(this);
    }

    void setTags(){
        switch(type){
            case "p" : tags.add("pmoved");
            case "r" : tags.add("rmoved");
            case "k" : tags.add("kmoved");
        }
    }
}
