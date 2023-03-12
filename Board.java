import piece.Piece;
import piece.PieceManager;

import java.util.Objects;

public class Board {

    static String[][] board = {{"r", "n", "b", "q", "k", "b", "n", "r"},
                              {"p", "p", "p", "p", "p", "p", "p", "p"},
                              {"e", "e", "e", "e", "e", "e", "e", "e"},
                              {"e", "e", "e", "e", "e", "e", "e", "e"},
                              {"e", "e", "e", "e", "e", "e", "e", "e"},
                              {"e", "e", "e", "e", "e", "e", "e", "e"},
                              {"p", "p", "p", "p", "p", "p", "p", "p"},
                              {"r", "n", "b", "q", "k", "b", "n", "r"},};

    static void init(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(!Objects.equals(board[i][j], "e")){
                    PieceManager.activePieces.add(new Piece(board[i][j], i > 4, j ,i + 1));
                }
            }
        }
    }
}
