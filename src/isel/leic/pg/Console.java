package isel.leic.pg;

import isel.leic.pg.console.Frame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.security.InvalidParameterException;

/**
 * Console for writing and reading text.<p>
 * Allow cursor positioning and modify colors.<br>
 * For use in teaching programming courses at ISEL.<br>
 * <br>
 * <b>Example:</b> <br>
 * <code>
 * Console.open("Demo Stars",20,40);<br>
 * Random rnd = new Random();<br>
 * while (!Console.isKeyPressed()) {<br>
 * &nbsp; Console.cursor(rnd.nextInt(20),rnd.nextInt(40));<br>
 * &nbsp; Console.setForeground(rnd.nextInt(Console.MAX_COLORS));<br>
 * &nbsp; if (rnd.nextInt(20)==0) {<br>
 * &nbsp; &nbsp; Console.print('*');<br>
 * &nbsp; &nbsp; Console.sleep(50);<br>
 * &nbsp; } else Console.print(' ');<br>
 * }<br>
 * Console.close();<br>
 * </code>
 * @author Pedro Pereira
 * @since Inverno 2013
 */
public abstract class Console {
	private static Color[] colors = {
			Color.BLACK, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE,
			Color.YELLOW, Color.MAGENTA, Color.ORANGE, Color.CYAN, Color.PINK,
			new Color(145,88,44),
			Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY };
	/**
	 * Color to use calling {@link #color(int, int)}, {@link #setBackground(int)}, {@link #setForeground(int)} 
	 */
	public static final int BLACK=0, WHITE=1, RED=2, GREEN=3, BLUE=4, YELLOW=5, MAGENTA=6, ORANGE=7, CYAN=8, 
	    PINK=9, BROWN=10, DARK_GRAY=11, GRAY=12, LIGHT_GRAY=13, MAX_COLORS=14;
	
	private static final int DEFAULT_LINES = 25, DEFAULT_COLS = 40, MAX_LINES=60, MAX_COLS=100;
	private static final int DEFAULT_FONTSIZE = 18;

    /**
     * Value returned when no char is typed by {@link #getChar()}, {@link #waitChar(long)}
     */
	public static final char NO_CHAR = 0;
    /**
     * Value returned when no key is pressed by {@link #getKeyPressed()}, {@link #waitKeyPressed(long)}, {@link #getKeyPressedUntil(long)}
     */
	public static final int NO_KEY = -1;
	//public static final int MOUSE_CLICKED = -2;
    /**
     * Value returned when no key is pressed, but the mouse was clicked
     */
	public static final int MOUSE_EVENT = -3;

	private static int lines, cols;
	private static int fontSize = DEFAULT_FONTSIZE;
	private static float widthFactor = 1.0F;
	private static float heightFactor = 1.0F;
	private static Frame frame = null;

    /**
     * Construction of <code>Console</code> objects is not allowed.<br>
     * All methods of this class are static.
     */
    private Console() {}
	/**
	 * Create the console window.
	 * <p>
	 * In the end, close the console by calling close() method.
	 * @param lines Number of lines
	 * @param cols  Number of columns
	 * @see #close() 
	 * @see #open(String, int, int)
	 */
	public static void open(int lines, int cols) { open(null,lines,cols); }
	
	/**
	 * Create the console window.
	 * <p>
	 * In the end, close the console by calling close() method.
	 * @param title Title of window
	 * @param lines Number of lines [1..40]
	 * @param cols  Number of columns [1..80]
	 * @see #close() 
	 */
	public static void open(String title, int lines, int cols) {
		if (frame!=null) close();
		if (title==null) title = "Console PG";
		if (lines<=0 || lines>MAX_LINES || cols<=0 || cols>MAX_COLS)
			throw new InvalidParameterException();
		Console.lines = lines;
		Console.cols = cols;
	    frame = new Frame(title, lines, cols, fontSize, heightFactor, widthFactor);
	}
	private static void check() { if (frame==null) open(DEFAULT_LINES, DEFAULT_COLS); }
	
	/**
	 * Write a string into the console at the current position of the cursor and the current colors.
	 * @param s Text to write
	 * @see #color(int, int)
	 * @see #cursor(int, int)
	 */
	public static void print(String s) { check(); frame.put(s); }
	
	/**
	 * Write a char into the console at the current position of the cursor and the current colors.
	 * @param c Char to write
	 * @see #color(int, int)
	 * @see #cursor(int, int)
	 */
	public static void print(char c)   { check(); frame.put(c); }
	
	/**
	 * Write a integer value in base 10 <p>
	 * Call <code>print(n)</code> is the same as <code>print(""+n)</code>
	 * @param n Value to write
	 * @see #print(char)
	 */
	public static void print(int n) { print("" + n); }
	
	/**
	 * Writes the line break character.<p>
	 * Calling <code>println()</code> is the same as calling <code>print('\n')</code>
	 * @see #print(char)
	 */
	public static void println()    { print('\n'); }
	
	/**
	 * Calling <code>println(txt)</code> is the same as calling <code>print(txt+'\n')</code>
	 * @see #print(String)
	 * @param s String to write
	 */
	public static void println(String s) { print(s+"\n"); }
	
	/**
	 * Calling <code>println(c)</code> is the same as calling <code>print(c+"\n")</code>
	 * @see #print(String)
	 * @param c Char to write
	 */
	public static void println(char c)   { print(c+"\n"); }
	
	/**
	 * Calling <code>println(n)</code> is the same as calling <code>print(n+"\n")</code>
	 * @see #print(String)
	 * @param n Value to write
	 */
	public static void println(int n)    { print(n+"\n"); }

	/**
	 * Changes the current cursor position for next writes.
	 * @param lin Line where the cursor changes [0..lines-1]
	 * @param col Column where the cursor change [0..cols-1] 
	 * @see #open(int lines, int cols)
	 */
	public static void cursor(int lin, int col) {
		if (lin<0 || lin>=lines || col<0 || col>=cols)
			throw new InvalidParameterException(); 
		frame.cursor(lin, col);
	}

	/**
	 * Enables or disables the cursor.
	 * When the console opens the cursor is disabled.
	 * @param on true to enable or false to disable
	 */
	public static void cursor(boolean on) { frame.setCursorOn(on); }
	
	/**
	 * Change the colors of text and background for next writes
	 * @param foreground Writing color 
	 * @param background Background color
	 * @see #setBackground(int)
	 * @see #setForeground(int)
	 * @see #BLACK
	 * @see #WHITE
	 */
	public static void color(int foreground, int background) {
		setBackground(background);
		setForeground(foreground);		
	}
	/**
	 * Change the colors of background for next writes
	 * @param color Background color
	 * @see #color(int, int)
	 */
	public static void setBackground(int color) {
		frame.setBackColor( colors[color  % colors.length] );
	}
	/**
	 * Change the colors of text for next writes
	 * @param color Foreground color
	 * @see #color(int, int)
	 */
	public static void setForeground(int color) {
		frame.setForeColor(colors[color % colors.length]);
	}
	
	/**
	 * Sets the size of each letter. Ex: 12, 18, 22.
	 * This method must be called before open (...)
	 * @param size Fonte size
	 */
	public static void fontSize(int size) {
		if (frame!=null)
			throw new InvalidParameterException();
		fontSize = size;
	}

    /**
     * Sets the scale factor of each cell.
     * By default is height=1.0 and widht=1.0
     * Factor too small (factor<0.7) may cut characters.
     * This method must be called before open (...)
     * @param heigth scale factor for heigth of each cell
     * @param widht scale factor for widht of each cell
     */
    public static void scaleFactor(double heigth, double widht) {
		if (frame!=null)
			throw new InvalidParameterException();
        heightFactor = (float) heigth;
        widthFactor = (float) widht;
    }
	
	/**
	 * Clears the content of the console, writing spaces with current colors.
	 */
	public static void clear() {
		frame.cursor(0,0);
		for(int i=0 ; i<lines*cols ; ++i)
			frame.put(' ');
	}

	/**
	 * Close the console
	 * @see #open(int lines, int cols)
	 * @see #open(String, int, int)
	 */
	public static void close() {
	   frame.dispose();
	   frame = null;
	}
	
	/**
	 * Turns on or off the echo of next keys read from the keyboard with getKey() or waitKey().
	 * When the console opens the echo is on.
	 * @param on true to on; false to off
	 * @see #getChar()
	 * @see #waitChar(long)
	 */
	public static void echo(boolean on) {
		frame.setEcho(on);
	}
	
	/**
	 * Reads a key which has been pressed.
	 * This method reads only keys that are used to edit text (letters, digits, space, enter, etc.).
	 * @return The key pressed or NO_CHAR ((char) 0) if no key was pressed
	 * @see #echo(boolean)
	 * @see #waitChar(long)
	 */
	public static char getChar() {
		try {
			char c = frame.getChar();
			return c==0 ? NO_CHAR : c ;
		} catch (InterruptedException e) {
			return NO_CHAR;
		}
	}
	
	/**
	 * Wait until a key is pressed within the time indicated 
	 * This method reads only keys that are used to edit text (letters, digits, space, enter, etc.).
	 * @param timeout Maximum time to wait in milliseconds. If is 0 wait forever
	 * @return The key pressed or NO_CHAR ((char) 0) if no key was pressed in the timeout
	 */
	public static char waitChar(long timeout) {
        //while( getChar()!=NO_CHAR ) ;
  		if (timeout<0) timeout = 0;
		if (timeout>0) timeout += System.currentTimeMillis();
		char c;
		while( (c=getChar())==NO_CHAR && (timeout<=0 || System.currentTimeMillis() < timeout ))
			;
		return c;
	}
	
	/**
	 * Checks whether a specific key is pressed.
	 * It may be more than one key pressed at the same time.
	 * This method also checks action keys (Cursor arrows, Home, etc.).
	 * @param code Code of key checked
	 * @return True if key is pressed
	 * @see #getKeyPressed()
	 */
	public static boolean isKeyPressed(int code) { return frame.keyPressed(code); }
	
	/**
	 * Checks if any key is pressed.
	 * This method also checks action keys (Cursor arrows, Home, etc.).
	 * @return True if any key is pressed
	 */
	public static boolean isKeyPressed() { return frame.anyKeyPressed(); }
	
	/**
	 * Returns the code of a key that is pressed, probably the last pressed.
	 * It may be more than one key pressed at the same time.
	 * This method also checks action keys (Cursor arrows, Home, etc.).
	 * @return The code of the key pressed or a negative value if no key was pressed.<p>
	 * - NO_KEY  (-1) if no key was pressed<p> 
	 * - If <code>mouseClick(true)</code> and there was a mouse click and no key was pressed return MOUSE_CLICK (-2)  
	 * @see #isKeyPressed(int)
	 * @see #waitKeyPressed(long)
	 * @see #enableMouseEvents(boolean))
	 * @see #getMouseEvent()
	 */
	public static int getKeyPressed() { return frame.getKeyPressed(); }

	/**
	 * Wait until any key is pressed within the time indicated
	 * This method also checks action keys (Cursor arrows, Home, etc.).
	 * @param timeout Maximum time to wait in milliseconds. If is 0 wait forever
	 * @return The key pressed or a negative value if no key was pressed (NO_KEY or MOUSE_CLICK).
     * @see #getKeyPressedUntil(long)
	 * @see #getKeyPressed()
	 * @see #getMouseEvent()
	 */
	public static int waitKeyPressed(long timeout) {
		if (timeout<0) timeout = 0;
		if (timeout>0) timeout += System.currentTimeMillis();
		int k;
		while( (k=frame.getKeyPressed())==NO_KEY && (timeout<=0 || System.currentTimeMillis() < timeout ))
            ;
		return k;
	}

    /**
     * Waits while a key keeps pressed.
     * @param key The regarded key
     * @see #getKeyPressed()
     * @see #getMouseEvent()
     */
    public static void waitKeyReleased(int key) {
        while( isKeyPressed(key) )
            sleep(50);
    }

    /**
     * Waits while any key keep pressed and clear all char typed and mouse events.
     * @see #waitKeyReleased(int)
     * @see #getChar()
     */
    public static void clearAllChars() {
        while ( isKeyPressed() )
            sleep(50);
        while ( getChar()!=NO_CHAR )
            ;
        while ( getMouseEvent() != null )
			;
    }

	/**
	 * Turn on the mechanism to read mouse events.<p>
	 * This mechanism is normally turned off when the console is open.<p>
	 * To read mouse events should be called the getMouseEvent method.
	 * @param drag True to enable also the mouse drags
	 * @see #getMouseEvent()
	 * @see #disableMouseEvents()
	 * @see #waitKeyPressed(long)
	 * @see #getKeyPressed()
	 */
	public static void enableMouseEvents(boolean drag) { frame.enableMouseEvents(drag); }

	/**
	 * Turn off the mechanism to read mouse events.<p>
	 * @see #enableMouseEvents(boolean)
	 * @see #getMouseEvent()
	 */
	public static void disableMouseEvents() { frame.disableMouseEvents(); }

	/**
	 * Returns the event information of the last mouse event.<p>
	 * Always returns null if the mouse event mechanism is off.<p>
	 * The <code>waitKeyPressed()</code> and <code>getKeyPressed()</code> return <code>MOUSE_EVENT</code> (-3)
	 * when there is no key but there is a mouse event.
	 * @return The event information of the last mouse event or <code>null</code> if no events.
	 * @see #enableMouseEvents(boolean)
	 * @see #disableMouseEvents()
     * @see #getMouseEvent(int)
	 * @see #waitKeyPressed(long)
	 * @see #getKeyPressed()
	 */
	public static MouseEvent getMouseEvent() { return frame.getMouseEvent(); }

    /**
     * Returns the position of the last mouse click.<p>
     * Always returns null if the mouse event mechanism is off.<p>
     * The <code>waitKeyPressed()</code> and <code>getKeyPressed()</code> return <code>MOUSE_EVENT</code> (-3)
     * when there is no key but there is a mouse event.
     * @param type Mouse event type to filter
     * @return The mouse position of the last event of type indicated or <code>null</code> if no events of that type.
     * @see #enableMouseEvents(boolean)
     * @see #disableMouseEvents()
     * @see #getMouseEvent()
     */
    public static Location getMouseEvent(int type) {
        MouseEvent ev = getMouseEvent();
        return (ev!=null && ev.type==type) ? ev : null;
    }

	/**
	 * Reads an integer value (base 10) edited by the user.
	 * @param field Maximum number of digits
	 * @return The value read
	 * @see #nextLine(int)
	 */
	public static int nextInt(int field) {
	  	String s = nextLine(field);
	  	if (s==null) return -1;
	  	int val = -1;
	  	try{ val = Integer.parseInt(s); }
	  	catch(NumberFormatException e) {}
	  	return val;
	}
	
	/**
	 * Reads a line of text edited by the user, terminated by <code>ENTER</code>.
	 * Editing is performed at the current cursor position.
	 * @param field Maximum length of the text
	 * @return The text read. <code>null</code> if it was pressed the <code>Esc</code> key. 
	 */
	public static String nextLine(int field) {
		boolean oldCursorOn = frame.isCursorOn();
		boolean oldEcho = frame.isEcho();
		cursor(false);
		int lin = frame.getLin(), col = frame.getCol();
		for(int i=0 ; i<field ; ++i) print(' ');
		cursor(lin,col);
		cursor(true);
		echo(false);
		StringBuffer res=new StringBuffer(field);
		char c;
		while((c=waitChar(0))!='\n') {
		  if (c== KeyEvent.VK_ESCAPE) { res=null; break; } // Esc (27)
		  else if (c==KeyEvent.VK_BACK_SPACE ) {  // Backspace (8)
		    if (res.length()==0) continue; 
			res.deleteCharAt(res.length()-1);
			cursor(false);
			cursor(frame.getLin(),frame.getCol()-1);
			print(' ');
			cursor(frame.getLin(),frame.getCol()-1);
			cursor(true);
 		  }
		  else if (res.length() < field) {
			  print(c);
			  res.append(c);
		  }
		}
		cursor(oldCursorOn);
		echo(oldEcho);
		return res==null ? null : res.toString();
	}
	
	/**
	 * Defines the behavior of the close button of the window in console.
	 * This method must be called after open()
	 * @param on <p><code>true</code> to terminates the application if the window is closed.<p>
	 *                <code>false</code> to ignore the close button. 
	 */
	public static void exit(boolean on) {
		frame.setDefaultCloseOperation(on ? JFrame.EXIT_ON_CLOSE : JFrame.DO_NOTHING_ON_CLOSE);
	}

    /**
     * Returns after expiration of the specified time
     * @param time Time to wait in milliseconds.
     */
    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {	}
    }

    /**
     * Waits until the absolute time indicated is reached.
     * <p>Returns immediately if the time indicated was passed.</p>
	 * @param time The absolute time to be reached. (in milliseconds)
	 */
	public static void sleepUntil(long time) {
		long delta = time - System.currentTimeMillis();
		if (delta<=0) return;
		try {
			Thread.sleep(delta);
		} catch (InterruptedException e) { }
	}

    /**
     * Waits until the absolute time indicated is reached and returns the key that is pressed during this interval.
     * <p>Returns immediately if the time indicated was passed.</p>
     * <p>This method also checks action keys (Cursor arrows, Home, etc.).</p>
     * @param time The absolute time to be reached. (in milliseconds)
     * @return The key pressed or a negative value if no key was pressed during this time (NO_KEY or MOUSE_CLICK).
     * @see #waitKeyPressed(long)
     * @see #getKeyPressed()
     * @see #getMouseEvent()
     */
	public static int getKeyPressedUntil(long time) {
		long delta = time - System.currentTimeMillis();
		if (delta <=0) return getKeyPressed();
		int key = waitKeyPressed(delta);
		if (key<0) return key;
        sleepUntil(time);
        return key;
	}

    private static Clip getClip(String wavFile) {
        String soundName = "sound\\"+wavFile;
		if (soundName.indexOf('.')==-1)
			soundName += ".wav";
        InputStream sound = null;
        Clip clip = null;
        try {
            sound = new BufferedInputStream(new FileInputStream(soundName));
        } catch (FileNotFoundException ex) {
            sound = ClassLoader.getSystemResourceAsStream(soundName);
            if (sound==null) {
                System.out.println("Cant open file " + soundName);
                return null;
            }
        }
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(sound);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (Exception ex) {
            System.out.println("Error in sound " + wavFile);
            System.out.println(ex.getMessage());
            if (clip!=null) {
                clip.close();
                return null;
            }
            try { sound.close(); } catch (IOException e) {}
        }
        return clip;
    }

    /**
     * Play a sound stored in a ".wav" file.
     * The file should be in a folder named "sound" placed at the base folder of execution.
     * @param wavFile File name without the extension ".wav"
     */
    public static void playSound(String wavFile) {
        Clip clip = getClip(wavFile);
        if (clip!=null)
            clip.start();
    }

    private static Clip music = null;

    /**
     * Starts to play the music stored in a ".wav" file.
     * The file should be in a folder named "sound" placed at the base folder of execution.
     * @param wavFile File name without the extension ".wav"
     * @see #stopMusic()
     */
    public static void startMusic(String wavFile) {
        stopMusic();
        music = getClip(wavFile);
        if (music!=null) {
            music.loop(Clip.LOOP_CONTINUOUSLY);
            music.start();
        }
    }

    /**
     * Stops to play the music started by <code>startMusic()</code>
     * @see #startMusic(String)
     */
    public static void stopMusic() {
        if (music==null) return;
        music.stop();
        music.close();
        music = null;
    }

}
