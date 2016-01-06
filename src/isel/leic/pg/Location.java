package isel.leic.pg;

/**
 * Represents a position in Console area.<br>
 * Returned by {@link Console#getMouseEvent(int)}.
 */
public class Location {
    /**
     * Line number.
     */
	public int line;
    /**
     * Column number.
     */
    public int col;

    /**
     * Constructs the location (0,0)
     */
    public Location() { }

    /**
     * Constructs the location (l,c)
     * @param l Line number
     * @param c Column number
     */
    public Location(int l, int c) { line=l; col=c; }

    /**
     * Constructs a copy of the location indicated location.
     * @param loc Location to copy
     */
    public Location(Location loc) { line=loc.line; col=loc.col; }

    /**
     * Compare to the indicated location.
     * @param l Location to compare
     * @return true if the locations are equal
     */
    public boolean equals(Location l) { return this==l || l!=null && l.line == line && l.col==col; }

    /**
     * Compare to the indicated object.
     * @param obj Object to compare
     * @return true if the object indicated is a location equal to this
     */
    @Override
    public boolean equals(Object obj) { return obj instanceof Location && equals((Location)obj); }

    /**
     * The string to describe the location
     * @return String in format: <code>(line,col)</code>
     */
    @Override
    public String toString() { return "("+line+","+col+")"; }
}
