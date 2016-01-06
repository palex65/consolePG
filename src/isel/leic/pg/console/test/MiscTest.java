package isel.leic.pg.console.test;

import isel.leic.pg.Console;
import isel.leic.pg.MouseEvent;

import java.util.Random;

import static isel.leic.pg.Console.*;

public class MiscTest {
    /**
     * Program for demonstration and test of Console.
     * @param args not used
     */
    public static void main1(String[] args) {
        Console.open(20, 40);
        Console.exit(true);
        Console.print("Hello Console\n");
        Console.cursor(19, 15);	color(GREEN, BLACK); print("PG 2015");
        Console.cursor(10, 0); setForeground(BLUE);	print("Text:");
        color(BLACK, LIGHT_GRAY);
        Console.cursor(true);
        Console.echo(true);
        String txt = Console.nextLine(20);
        Console.cursor(true);
        Console.println();
        color(RED, BLACK); println("Text="+txt);
        int k;
        Console.enableMouseEvents(false);
        while ((k= Console.waitKeyPressed(5000))!=NO_KEY) {
            if (k==MOUSE_EVENT) {
                MouseEvent ev = Console.getMouseEvent();
                if (ev!=null && ev.type==MouseEvent.CLICK) cursor(ev.line,ev.col);
            } else {
                switch (k) {
                    case 'C': clear(); break;
                    case 'E': echo(true); break;
                    case 'N': echo(false); break;
                    case 'R': color(RED, BLACK); break;
                    case 'Y': color(GREEN,YELLOW); break;
                    case 'O': cursor(true); break;
                    case 'F': cursor(false); break;
                }
                while ( Console.isKeyPressed(k)) ;
            }
        }
        Console.echo(false);
        Console.cursor(15, 15);
        String end="The end";
        for(int i=0 ; i<end.length() ; ++i) {
            Console.print(end.charAt(i));
            Console.waitChar(500);
        }
        Console.close();
    }

    public static void main(String[] args) {
        Console.open("Demo Stars",20,40);
        Random rnd = new Random();
        //Console.startMusic("stars");
        while (!Console.isKeyPressed()) {
            Console.cursor(rnd.nextInt(20),rnd.nextInt(40));
            Console.setForeground(rnd.nextInt(Console.MAX_COLORS));
            if (rnd.nextInt(20)==0) {
                Console.print('*');
                Console.sleep(100);
            }
            else Console.print(' ');
        }
        Console.stopMusic();
        Console.close();
    }
}
