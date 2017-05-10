package isel.leic.pg.console.test;

import isel.leic.pg.Console;

public class TestSouds {
    public static void main(String[] args) {
        Console.open("Music",20,40);
        Console.println("playSound : ");
        Console.startMusic("stars");
        for (int i = 0; i < 10; i++) pop();
        Console.stopMusic();
        Console.close();
    }

    private static void pop() {
        Console.color(Console.WHITE,Console.RED);
        Console.println("pop");
        Console.playSound("pop");
        Console.sleep(2000);
    }
}
