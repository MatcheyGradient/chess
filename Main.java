import piece.PieceImages;

import javax.swing.*;

public class Main extends JFrame {

    public Main(){
        this.add(new Window());
        this.setTitle("chess");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args){
        Board.init();
        PieceImages.init();

        new Main();
    }
}
