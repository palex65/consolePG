package isel.leic.pg.console;

import java.awt.*;

/**
 * Character and respective information about the foreground and background colors.
 */
class CharAttr {
    private char c;				// Character code
    private Color bkg, frg;		// Background and foreground colors

    public CharAttr(Color bCol, Color fCol) {
		setChr(' ', bCol, fCol);
	}

	private static final char[] chars = new char[1]; // Shared buffer for efficiency reasons

	private static final int WIDTH_FACTOR = 20; // Correction factors to center the character
	private static final int HEIGHT_FACTOR = 6;

	/**
	 * Draw the character in a rectangle to a particular graphics context.
	 * @param x of the upper left corner
	 * @param y of the upper left corner
	 * @param dx width of the rectangle
	 * @param dy height of the rectangle
	 */
    public void paint(Graphics g, int x, int y, int dx, int dy) {
        g.setColor(bkg);
        g.fillRect(x, y, dx, dy);
        FontMetrics fm = g.getFontMetrics();
        int w = fm.charWidth(c);
        int h = fm.getAscent();
        chars[0] = c;
        g.setColor(frg);
        g.drawChars(chars, 0, 1, x + (dx-w)/2 - w/WIDTH_FACTOR, y + (dy+h)/2 - h/HEIGHT_FACTOR);
    }

    /**
     * Change the character code
     * @param chr
     * @return true if the old code is different
     */
	public boolean setChr(char chr) {
        if (c == chr)
			return false;
        c = chr;
        return true;
    }

    /**
     * Chage the character code and colors
     * @param chr
     * @param backColor
     * @param foreColor
     * @return true if the old code or any color is different
     */
	public boolean setChr(char chr, Color backColor, Color foreColor) {
        if (c == chr && bkg == backColor && frg == foreColor)
			return false;
        bkg = backColor;
        frg = foreColor;
        c = chr;
        return true;
    }

    /**
     * Gets the charecter code
     * @return the character code
     */
	public char getChr() {
		return c;
	}
}
