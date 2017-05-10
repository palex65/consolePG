package isel.leic.pg.console.test;

import isel.leic.pg.*;
import static isel.leic.pg.Console.*;
import static java.awt.event.KeyEvent.*;

public class Paint {
    private static final int LINES = 20;
    private static final int COLS = 20;
    private static int currColor;   // Cor selecionada

    public static void main(String[] args) {
        open("Paint", LINES + 1, COLS);
        enableMouseEvents(false);
        updatePallete(GREEN);
        draw();
        close();
    }

    private static void draw() {
        int key;
        do {
            key = waitKeyPressed(0);
            if (key==MOUSE_EVENT)
                processClick(getMouseEvent(MouseEvent.DOWN));
            else if (key>0) {
                processKey(key);
                waitKeyReleased(key);
            }
        } while(key!= VK_ESCAPE);
    }

    private static void processClick(Location l) {
        if (l==null) return;  // O evento não é DOWN
        if (l.line>0) {
            cursor(l.line, l.col);
            setBackground(currColor);
            print(' ');
        } else
        if (l.col<MAX_COLORS) updatePallete(l.col);
    }

    private static void processKey(int key) {
        if (key==VK_RIGHT)
            updatePallete(currColor+1);
        else if (key==VK_LEFT)
            updatePallete(currColor-1);
    }

    private static void updatePallete(int color) {
        cursor(0,0);
        currColor = color % MAX_COLORS;
        for (int i = 0; i < MAX_COLORS; i++) {
            color(i==WHITE || i==YELLOW ? BLACK : WHITE, i);
            print(currColor==i?'*':' ');
        }
    }
}
