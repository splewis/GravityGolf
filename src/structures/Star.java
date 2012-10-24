package structures;

import java.awt.Color;
import java.awt.Graphics2D;

public class Star {
	
	private java.awt.Point point;
	private Color color;
	private int size;
	
	public Star(int x, int y) {
		point = new java.awt.Point(x, y);
		size = CalcHelp.gaussianInteger(1.5, 1.05);
		if(size < 1) size = 1;
			
		int r = CalcHelp.gaussianInteger(240, 30); 
		int g = CalcHelp.gaussianInteger(240, 30); 
		int b = CalcHelp.gaussianInteger(240, 40); 
		color = CalcHelp.correctColor(r, g, b);		
	}
	
	public void draw(int dx, int dy, Graphics2D g) {
		g.setColor(color);
		g.fillOval(point.x + dx, point.y + dy, size, size);		
	}
	
	public void draw(Graphics2D g) {
		draw(0, 0, g);	
	}
	
	public java.awt.Point getPoint() {
		return point;
	}

}
