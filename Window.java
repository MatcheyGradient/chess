import listeners.ClickListener;
import listeners.MouseTracker;
import piece.PieceManager;
import resources.ChessBoardUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.Color.*;

public class Window extends JPanel implements ActionListener {

    Timer timer;

    public Window(){
        this.setPreferredSize(new Dimension(1200, 900));
        this.setBackground(new Color(77, 48, 28));
        this.setFocusable(true);
        this.addMouseListener(new ClickListener());
        this.addMouseMotionListener(new MouseTracker());
        timer = new Timer(0, this);
        start();
    }

    public void start() {
        timer.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.setColor(white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(8));

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                g.setColor((i % 2 == 0) ^ (j % 2 == 1)? new Color(255, 189, 138) : new Color(220, 132, 67));
                g.fillRect(i * 100 + 50, j * 100 + 50, 100, 100);
            }
        }

        for(int k = 0; k < 8; k++){
            g.setColor((k % 2 == 0)? new Color(255, 189, 138) : new Color(220, 132, 67));
            g.drawString(String.valueOf((char) (97 + k)), k * 100 + 55, 845);
            g.setColor((k % 2 == 1)? new Color(255, 189, 138) : new Color(220, 132, 67));
            g.drawString(String.valueOf(8 - k), 55, k * 100 + 70);
        }

        g.setColor(black);
        g.drawRect(45, 45, 810, 810);

        g.setColor(new Color(255, 0, 0, 148));
        ClickListener.highlights.forEach(h -> {
            g.fillRect(h[0] * 100 + 50, h[1] * 100 + 50, 100, 100);
        });

        PieceManager.activePieces.forEach(p -> {
            if(p != PieceManager.selectedPiece) p.drawPiece(g, true, 0, 0);
        });

        if(PieceManager.selected) PieceManager.drawSelected(g);

        ChessBoardUI.render(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}