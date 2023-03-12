package listeners;

import piece.PieceManager;
import resources.ChessBoardUI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class ClickListener implements MouseListener {

    public static List<int[]> highlights = new ArrayList<>();


    @Override
    public void mouseClicked(MouseEvent e) {
        if(ChessBoardUI.showPromote && e.getButton() == 1){
            if(ChessBoardUI.color){
                if(((e.getX() - 50) / 100) == ChessBoardUI.rank){
                    if(((e.getY() - 50) / 100) < 4 && ((e.getY() - 50) / 100) > -1){
                        ChessBoardUI.pi.type = switch((e.getY() - 50) / 100){
                            case 1 -> "b";
                            case 2 -> "n";
                            case 3 -> "r";

                            default -> "q";
                        };
                        ChessBoardUI.showPromote = false;
                        ChessBoardUI.pi.tags.clear();
                    }
                }
            } else {
                if(((e.getX() - 50) / 100) == ChessBoardUI.rank){
                    if(((e.getY() - 50) / 100) < 8 && ((e.getY() - 50) / 100) > 3){
                        ChessBoardUI.pi.type = switch((e.getY() - 50) / 100){
                            case 6 -> "b";
                            case 5 -> "n";
                            case 4 -> "r";

                            default -> "q";
                        };
                        ChessBoardUI.showPromote = false;
                        ChessBoardUI.pi.tags.clear();
                    }
                }
            }
            ChessBoardUI.updateChecks();
        } else if (e.getButton() == 3){
            int x = (e.getX() - 50) / 100;
            int y = (e.getY() - 50) / 100;

            if(x > -1 && x < 8 && y > -1 && y < 8){
                if(contains(new int[]{x, y}) != null){
                    highlights.remove(contains(new int[]{x, y}));
                } else {
                    highlights.add(new int[]{x, y});
                }
            }
        }
    }

    public static int[] contains(int[] pair){
        for (int[] h : highlights) {
            if(h[0] == pair[0] && h[1] == pair[1]){
                return h;
            }
        }

        return null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        MouseTracker.mx = e.getX();
        MouseTracker.my = e.getY();

        if(!ChessBoardUI.showPromote && e.getButton() == 1) PieceManager.selectPiece(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        PieceManager.selected = false;
        if(e.getX() < 850 && e.getY() < 850 && !ChessBoardUI.showPromote && e.getButton() == 1) PieceManager.placePiece(e.getX(), e.getY());
        PieceManager.selectedPiece = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
