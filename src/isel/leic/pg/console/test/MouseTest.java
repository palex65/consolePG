package isel.leic.pg.console.test;

import isel.leic.pg.Console;
import isel.leic.pg.Location;
import isel.leic.pg.MouseEvent;

import java.awt.event.KeyEvent;

public class MouseTest {
    public static void main(String[] args) {
        Console.open("Mouse Test",30,30);
        Console.exit(true);
        Console.cursor(28,0);
        Console.color(Console.YELLOW,Console.BLACK);
        Console.print("Closes after 5 seconds without any key press or click");
        mouseClicks(true, false);
        int counter=0,i=0;
        for(;;) {
            int key = Console.waitKeyPressed(5000);
            if (key==Console.NO_KEY) break;
            if (key==Console.MOUSE_EVENT) {
                MouseEvent ev = Console.getMouseEvent();
                Console.cursor(ev.line, ev.col);
                switch (ev.type) {
                    case MouseEvent.CLICK:
                        print(Console.BLACK, Console.WHITE, "*");
                        break;
                    case MouseEvent.DOWN:
                        print(Console.BLACK, Console.RED, "*");
                        i=0;
                        break;
                    case MouseEvent.UP:
                        print(Console.YELLOW, Console.RED,"*");
                        break;
                    case MouseEvent.DRAG:
                        print(Console.WHITE, Console.BLACK,""+i);
                        ++i;
                        break;
                }
            } else {
                Console.cursor(0,0);
                Console.color(Console.WHITE, Console.BLACK);
                Console.print(++counter);
                switch (key) {
                    case KeyEvent.VK_ESCAPE:
                        mouseClicks(false,false); break;
                    case KeyEvent.VK_SPACE:
                        mouseClicks(true,false); break;
                    case KeyEvent.VK_D:
                        mouseClicks(true,true); break;
                }
            }
        }
        Console.close();
    }
    private static void mouseClicks(boolean on, boolean drag) {
        if (on) Console.enableMouseEvents(drag);
        else Console.disableMouseEvents();
        Console.cursor(10,0);
        print(Console.WHITE, Console.BLACK, "Mouse cliks:");
        if (on)
            print(Console.BLACK,Console.GREEN,drag ? "DRAG":" ON ");
        else
            print(Console.BLACK, Console.RED, "OFF ");
    }
    private static void print(int fore, int back, String txt) {
        Console.color(fore,back);
        Console.print(txt);
    }
}
