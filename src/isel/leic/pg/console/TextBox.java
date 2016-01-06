package isel.leic.pg.console;

import isel.leic.pg.console.CharAttr;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
class TextBox extends JComponent {

	private final int lines, cols;
    private final CharAttr[][] grid;
	
	TextBox(int lines, int cols, Color bCol, Color fCol, Font f) {
		this.lines = lines;
		this.cols = cols;
    	grid = new CharAttr[lines][cols];
    	for(int l=0 ; l<lines ; ++l) {
    		for(int j=0 ; j<cols ; ++j)
    		  	grid[l][j]= new CharAttr(bCol,fCol);
    	}
    	setFont(f);
 	}

	@Override
	public void paint(Graphics g) {
		Rectangle clip = g.getClipBounds();
		int dx = getWidth() / cols;
		int dy = getHeight() / lines;
		int x, y = 0;
		for(int l=0; l<lines ; ++l, y+=dy) {
			x = 0;
			for(int c=0; c<cols ; ++c , x+=dx)  
				if (clip.intersects(x, y, dx, dy)) 
					grid[l][c].paint(g,x,y,dx,dy);
		}
	}

	void setChr(int lin, int col, char c) {
		if (grid[lin][col].setChr(c))
			repaintChar(lin, col);
	}

	private void repaintChar(int lin, int col) {
		int dx = getWidth() / cols;
		int dy = getHeight() / lines;
		repaint(col*dx,lin*dy,dx,dy);
	}

	void setChr(int lin, int col, char c, Color b, Color f) {
		if (grid[lin][col].setChr(c,b,f)) 
			repaintChar(lin,col);
	}

	char getChr(int lin, int col) {
		return grid[lin][col].getChr();
	}
}  
