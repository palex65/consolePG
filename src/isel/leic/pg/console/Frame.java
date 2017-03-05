package isel.leic.pg.console;

import isel.leic.pg.Console;
import isel.leic.pg.MouseEvent;
import isel.leic.pg.concurrent.*;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class Frame extends JFrame {
    private static boolean limitCpuUsage = true;

	private static final char CURSOR = '|';
    private static final int BLINK_TIME = 500;
	private static int minTime = 50;

	private final int lines, cols;

	private int lin=0, col=0;
    private boolean echo = false;
	private Color bColor = Color.BLACK;
    private Color fColor = Color.WHITE;
    
    private final TextBox txt;
    private final KeyManager keyMgr;
    private MouseManager mouseMgr;

    public static void setLimitTime(boolean limitCpuUsage, int minTime) {
        Frame.limitCpuUsage = limitCpuUsage;
        Frame.minTime = minTime;
    }

    public Frame(String title, int lines, int cols, int fontSize, float heightFactor, float widthFactor) {
		super(title);
		this.lines = lines; this.cols = cols;
		Font f = new Font(Font.MONOSPACED,Font.BOLD, fontSize);
        txt = new TextBox(lines,cols,bColor,fColor,f);
        txt.setPreferredSize(new Dimension((int) (cols * fontSize * widthFactor), (int) (lines * fontSize * heightFactor)));
        getContentPane().add( txt );
        addKeyListener(keyMgr = new KeyManager(this));
    	setLocationByPlatform(true);
		setResizable(false);
    	pack();
		toFront();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}

    public void setEcho(boolean echo) { this.echo = echo; }
    public boolean isEcho() { return echo; }

    public int getLin() { return lin; }
    public int getCol() { return col; }

    public int getCellWidth() { return txt.getWidth()/cols; }
    public int getCellHeight() { return txt.getHeight()/lines; }

    public void setBackColor(Color backColor) { bColor = backColor; }
    public void setForeColor(Color foreColor) { fColor = foreColor; }

	public synchronized void put(char c) {
	  if (c=='\n') {
		  if (cursorOn && blinkOn)
			  txt.setChr(lin,col,cursorChar);
		  if (++lin==lines) lin=0;
		  col = 0;
	  } else {
		  txt.setChr(lin,col,c,bColor,fColor);
		  if (++col==cols) {
			  col=0;
			  if (++lin==lines) lin=0;
		  }
	  }
	  updateCursor();
	}

	public void put(String s) {
		for(int i=0 ; i<s.length() ; ++i) put(s.charAt(i));
	}

    // KEYBOARD INPUT

    public char getChar() throws InterruptedException {
        blinkCursor();
        return keyMgr.getChar(minTime);
    }

    public boolean keyPressed(int code) { return keyMgr.isPressed(code); }
    public boolean anyKeyPressed()      { return keyMgr.anyPressed(); }

    public int getKeyPressed() {
        int key = 0;
        try {
            if (limitCpuUsage) {
                long tm = System.currentTimeMillis();
                key = keyMgr.getAnyPressed(minTime);
                sleepRemainder(tm, minTime);
            } else
                key = keyMgr.getAnyPressed(0);
        } catch (InterruptedException ignored) { }
        blinkCursor();
        return key != PositiveIntSet.NO_ELEM ? key :
                (mouseMgr == null || mouseMgr.isEmpty()) ? Console.NO_KEY : Console.MOUSE_EVENT;
    }

    private static void sleepRemainder(long startTm, long totalTm) throws InterruptedException {
        long elapsed = System.currentTimeMillis() - startTm;
        if (elapsed < totalTm)
           Thread.sleep(totalTm - elapsed);
    }

    // CURSOR MANAGER

    private boolean cursorOn=false;
    private char cursorChar;
    private boolean blinkOn;
    private long blinkTime=0;

    private void updateCursor() {
        if (cursorOn) {
            cursorChar = txt.getChr(lin,col);
            if (blinkOn)
                txt.setChr(lin,col,CURSOR);
        }
    }

    public void cursor(int l, int c) {
		if (l==lin && c==col) return;
	    if (cursorOn && blinkOn)
            txt.setChr(lin,col,cursorChar);
		lin = l;
		if (lin>=lines) lin=lines-1;
		col = c;
		if (col>=cols) col=cols-1;
		updateCursor();
	}
	
	public void setCursorOn(boolean on) {
		if (cursorOn==on) return;
		cursorOn=on;
		if (on) {
		  blinkOn=false;
		  cursorChar=txt.getChr(lin,col);
		  blinkCursor();
		} else
		  if (blinkOn) txt.setChr(lin,col,cursorChar); 
	}
    public boolean isCursorOn() {
        return cursorOn;
    }
	
	private void blinkCursor() {
		if (!cursorOn || System.currentTimeMillis() < blinkTime) return;
		blinkOn = !blinkOn;
		txt.setChr(lin,col,blinkOn ? CURSOR : cursorChar);
		blinkTime = System.currentTimeMillis() + BLINK_TIME;
	}

    public void enableMouseEvents(boolean drag) {
        if (mouseMgr!=null)
            mouseMgr.removeListeners();
        mouseMgr = new MouseManager(this,drag);
    }

    public void disableMouseEvents() {
        if (mouseMgr==null) return;
        mouseMgr.removeListeners();
        mouseMgr = null;
    }

    public MouseEvent getMouseEvent() {
        if (mouseMgr==null) return null;
        MouseEvent ev = null;
        try {
            if (limitCpuUsage) {
                long tm = System.currentTimeMillis();
                ev = mouseMgr.getEvent(minTime);
                sleepRemainder(tm, minTime);
            } else
                ev = mouseMgr.getEvent(0);
        } catch (InterruptedException ignored) { }
        blinkCursor();
        return ev;
    }
}
