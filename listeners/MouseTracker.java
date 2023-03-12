package listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseTracker implements MouseMotionListener {
    public static int mx;
    public static int my;

    @Override
    public void mouseDragged(MouseEvent e) {
        mx = e.getX();
        my = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
