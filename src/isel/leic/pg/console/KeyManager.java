package isel.leic.pg.console;

import isel.leic.pg.concurrent.*;
import java.awt.event.*;

public class KeyManager implements KeyListener {
    static final int KEYS_BUFFER_SIZE = 128;
    private final CharRingBuffer typedChars = new CharRingBuffer(KEYS_BUFFER_SIZE,true);

    static final int PRESSED_KEYS_BUFFER_SIZE = 32;
    private final PositiveIntSet pressedKeys = new PositiveIntSet(PRESSED_KEYS_BUFFER_SIZE);

    private final Frame frame;
    KeyManager(Frame frame) { this.frame = frame; }


    char getChar(long timeout) throws InterruptedException {
        return typedChars.get(timeout);
    }
    boolean isPressed(int code) {
        return pressedKeys.contains(code);
    }
    boolean anyPressed() {
        return !pressedKeys.isEmpty();
    }
    int getAnyPressed(long timeout) throws InterruptedException {
        return pressedKeys.getAny(timeout);
    }


    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (frame.isEcho() && isPrintable(c))
            frame.put(c);
        typedChars.put(c);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    private boolean isPrintable(char c) {
        return Character.isDefined(c);
    }
}
