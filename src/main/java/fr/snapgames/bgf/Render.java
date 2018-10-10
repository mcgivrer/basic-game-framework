/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * basic-game-framework
 * 
 * @year 2018
 */
package fr.snapgames.bgf;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * 
 * @author Frédéric Delorme
 *
 */
public class Render {

	/**
	 * Draw <code>text</code> with <code>foreground</code> color adding outlines
	 * width a <code>shadow</code> color, of <code>thickness</code> draw.
	 * 
	 * @param g          Graphics interface to be use.
	 * @param x          the X position of the text.
	 * @param y          the Y position of the text.
	 * @param text       the text to be drawn.
	 * @param thickness  the thickness of the outline.
	 * @param foreground the foreground color.
	 * @param shadow     the shadow color.
	 */
	public static void drawOutlinedString(Graphics2D g, int x, int y, String text, int thickness, Color foreground,
			Color shadow) {
		g.setColor(shadow);
		for (int i = -thickness; i <= thickness; i++) {
			g.drawString(text, x + i, y);
		}
		for (int i = -thickness; i <= thickness; i++) {
			g.drawString(text, x, y + i);
		}
		g.setColor(foreground);
		g.drawString(text, x, y);

	}
}
