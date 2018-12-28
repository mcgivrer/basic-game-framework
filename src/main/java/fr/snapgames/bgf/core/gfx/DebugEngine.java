/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf.core.gfx;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import fr.snapgames.bgf.core.entity.GameEntity;
import fr.snapgames.bgf.core.entity.GameObject;

/**
 * DebugEngine
 * 
 * 
 * 
 * @author Frédéric Delorme<frederic.delorme@snapgames.fr>
 *
 */
public class DebugEngine {

	private Font dbgFont;

	DebugEngine(Graphics2D g) {
		dbgFont = g.getFont().deriveFont(9.0f);
	}

	/**
	 * build Debug Info for this GameObject.
	 * 
	 * @param go
	 * @return
	 */
	public List<String> getDebugInfo(GameObject go) {
		List<String> debugStrings = new ArrayList<>();

		debugStrings.add(String.format("Name:%s", go.getName()));
		debugStrings.add(String.format("Pos:%03.2f,%03.2f", go.position.x, go.position.y));
		debugStrings.add(String.format("Size:%03.2f,%03.2f", go.size.x, go.size.y));
		debugStrings.add(String.format("Vel:%03.2f,%03.2f", go.speed.x, go.speed.y));
		debugStrings.add(String.format("L/P:%d/%d", go.layer, go.priority));
		if (go.getCustomDebugInfo() != null) {
			debugStrings.addAll(go.getCustomDebugInfo());
		}
		return debugStrings;
	}

	/**
	 * Render debug information for the object <code>o</code> to the Graphics2D
	 * <code>g</code>.
	 * 
	 * @param g the Graphics2D to render things.
	 * @param o the object to be debugged.
	 */
	public void drawObjectDebugInfo(Render r, Graphics2D g, GameEntity ge) {
		g.setFont(dbgFont);
		GameObject o = (GameObject) ge;
		g.setColor(new Color(0.1f, 0.1f, 0.1f, 0.80f));
		g.fillRect((int) (o.position.x + o.size.x + 2), (int) o.position.y, 80, 60);

		g.setColor(Color.DARK_GRAY);
		g.drawRect((int) (o.position.x + o.size.x + 2), (int) o.position.y, 80, 60);

		g.setColor(Color.GREEN);
		int index = 1;
		for (String item : getDebugInfo(o)) {
			g.drawString(item, o.position.x + o.size.x + 4, o.position.y + (12 * index));
			index += 1;
		}
	}

	/**
	 * Draw debug information.
	 * 
	 * @param g the Graphics2D to render things.
	 */
	public void drawGlobalDebugInformation(Render r, Graphics2D g) {
		g.setFont(dbgFont);
		int debug = r.getDebugMode();
		String debugString = String.format("dbg:%s | FPS:%d | Objects:%d | Rendered:%d",
				(debug == 0 ? "off" : "" + debug), 
				r.app.getRealFPS(), 
				r.app.getObjects().size(), 
				r.renderingList.size());
		int dbgStringHeight = g.getFontMetrics().getHeight() + 8;
		g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.8f));
		g.fillRect(0, r.getBuffer().getHeight() - (dbgStringHeight + 8), r.getBuffer().getWidth(), (dbgStringHeight));
		g.setColor(Color.ORANGE);
		g.drawString(debugString, 4, r.getBuffer().getHeight() - dbgStringHeight);
	}
}
