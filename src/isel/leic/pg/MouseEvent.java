package isel.leic.pg;

/**
 * Represents a mouse event occurred in Console area.<br>
 * To be used only in POO-Object Oriented Programming<br>
 * Returned by {@link Console#getMouseEvent()}.
 */
public class MouseEvent extends Location {
    /**
     * Type of mouse event.
     */
    public static final int CLICK = 1, DOWN = 2, UP = 3, DRAG = 4;

    /**
     * The type of event occurred.
     */
    public final int type;

    public MouseEvent(int type, int line, int col) {
        super(line,col);
        this.type = type;
    }

    public boolean equals(MouseEvent e) {
        return super.equals(e) && e.type==type;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MouseEvent && equals((MouseEvent)obj);
    }

    private static final String[] typeTxt = { "Click", "Down", "Up", "Drag" };
    @Override
    public String toString() {
        return typeTxt[type-1]+":"+super.toString();
    }
}
