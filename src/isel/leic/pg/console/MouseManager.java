package isel.leic.pg.console;

import isel.leic.pg.MouseEvent;
import isel.leic.pg.concurrent.*;

import java.awt.*;
import java.awt.event.*;

public class MouseManager implements MouseListener, MouseMotionListener {
    private static final int EVENTS_BUFFER_SIZE = 64;
    private final RingBuffer<MouseEvent> events = new RingBuffer<>(EVENTS_BUFFER_SIZE,false);

    private Frame frame;

    MouseManager(Frame frame, boolean motion) {
        this.frame = frame;
        frame.addMouseListener(this);
        if (motion)
            frame.addMouseMotionListener(this);
    }

    void removeListeners() {
        frame.removeMouseListener(this);
        frame.removeMouseMotionListener(this);
    }

    MouseEvent getEvent(long timeout) throws InterruptedException {
        return events.get(timeout);
    }

    boolean isEmpty() { return events.isEmpty(); }

    private int yToLine(int y) { return (y - frame.getInsets().top) / frame.getCellHeight();	}
    private int xToCol(int x) { return (x - frame.getInsets().left) / frame.getCellWidth(); }

    private MouseEvent translateEvent(int type, java.awt.event.MouseEvent e) {
        Point point = e.getPoint();
        return new MouseEvent(type, yToLine(point.y), xToCol(point.x) );
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        if (events==null) return;
        events.put( translateEvent(MouseEvent.CLICK,e) );
    }
    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        if (events==null) return;
        events.put( translateEvent(MouseEvent.DOWN,e) );
    }
    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        if (events==null) return;
        events.put( translateEvent(MouseEvent.UP,e) );
    }

    private MouseEvent last = null;

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        if (events==null) return;
        Point point = e.getPoint();
        int line = yToLine(point.y);
        int col = xToCol(point.x);
        if (last!=null && line==last.line && col==last.col)
            return;
        last = new MouseEvent(MouseEvent.DRAG,line,col);
        events.put( last );
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) { }
    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) { }
    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {	}
}
