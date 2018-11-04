/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */

package fr.snapgames.bgf;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.snapgames.bgf.InputListener.KeyBinding;

public class SampleGameState extends GameStateDefault implements GameState {

	private static final Logger logger = LoggerFactory.getLogger(SampleGameState.class);

	private int score = 0;

	private UIText scoreUiText;
	private GameObject player;

	@Override
	public void initialize(App app) {
		this.app = app;
	}

	@Override
	public void create(App app, long uid) {
		this.uid = uid;
		Font scoreFont = app.getRender().getGraphics().getFont().deriveFont(16.0f);

		scoreUiText = (UIText) UIText.builder("score").setFont(scoreFont).setText("00000").setThickness(1)
				.setPosition(12, 24).setLayer(10).setElasticity(0.98f).setFriction(0.98f).setLayer(20);
		add(scoreUiText);

		player = GameObject.builder("player").setSize(24, 24).setPosition(0, 0).setColor(Color.GREEN)
				.setVelocity(0.0f, 0.0f).setLayer(10).setPriority(100).setElasticity(0.98f).setFriction(0.98f);
		add(player);

		createGameObjects(app, "enemy_", 10);
	}

	/**
	 * Create nbEnemies in the playground.
	 * 
	 * @param nbEnemies
	 */
	private void createGameObjects(App app, String baseName, int nbEnemies) {
		app.suspendRendering(true);
		Rectangle vp = app.getRender().getViewport();
		for (int i = 0; i < nbEnemies; i++) {
			GameObject enemy = GameObject.builder(baseName + objects.size() + 1).setSize(16, 16)
					.setPosition((int) (Math.random() * vp.width), (int) (Math.random() * vp.height))
					.setVelocity((float) (Math.random() * 0.4f) - 0.2f, (float) (Math.random() * 0.4f) - 0.2f)
					.setColor(randomColor()).setPriority(i).setLayer(1).setElasticity(1.0f).setFriction(1.0f);
			add(enemy);
		}
		app.suspendRendering(false);

	}

	/**
	 * Generate a random Color.
	 * 
	 * @return
	 */
	private Color randomColor() {
		return new Color((float) Math.random(), 0.0f, (float) Math.random());
	}

	/**
	 * Remove {@link GameObject} from the {@link App} where name contains with
	 * nameFilter if nbToRemove equals -1, all corresponding
	 * <code>nameFilter</code>ed object will be removed.
	 * 
	 * @param nameFilter
	 * @param i
	 */
	private void removeGameObjects(App app, String nameFilter, int nbToRemove) {
		app.suspendRendering(true);

		// parse Object map and remove matching object with filtering string.
		// Map -> Stream -> Filter -> MAP
		Map<String, GameObject> collect = objects.entrySet().stream()
				// filter object on their name
				.filter(x -> x.getKey().contains(nameFilter))
				// add a limit if nbToRemove different of -1.
				.limit((nbToRemove == -1 ? objects.size() : nbToRemove))
				// remap result to a new map.
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

		// remove all matching objects from objects buffer.
		objects.entrySet().removeAll(collect.entrySet());

		// re-fulfill the rendering buffer.
		app.getRender().clearRenderingList();
		app.getRender().addAllObjects(objects.values());
		app.suspendRendering(false);
	}

	@Override
	public void dispose(App app) {
		// TODO next time some release of resource will implemented here.
	}

	@Override
	public void input(App app, InputListener inputListener) {
		GameObject goPlayer = objects.get("player");

		goPlayer.dy *= goPlayer.friction;
		goPlayer.dx *= goPlayer.friction;

		if (inputListener.getKey(KeyEvent.VK_LEFT)) {
			goPlayer.dx = -0.1f;
		}
		if (inputListener.getKey(KeyEvent.VK_RIGHT)) {
			goPlayer.dx = 0.1f;
		}
		if (inputListener.getKey(KeyEvent.VK_UP)) {
			goPlayer.dy = -0.1f;
		}
		if (inputListener.getKey(KeyEvent.VK_DOWN)) {
			goPlayer.dy = 0.1f;
		}

	}

	@Override
	public void update(App app, long dt) {
		for (Entry<String, GameObject> entry : objects.entrySet()) {
			entry.getValue().update(dt);
			constrains(entry.getValue());
		}
		score++;
		scoreUiText.setText(String.format("%05d", score));

	}

	/**
	 * Contained object to App viewport display.
	 * 
	 * @param o
	 */
	private void constrains(GameObject o) {
		if ((int) (o.x + o.width) > app.getRender().getViewport().width || o.x < 0.0f) {
			o.dx = -o.dx * o.friction * o.elasticity;
		}
		if ((int) (o.y + o.height) > app.getRender().getViewport().height || o.y < 0.0f) {
			o.dy = -o.dy * o.friction * o.elasticity;
		}
		// speed threshold constraints
		if (o.friction > 0.0f || o.elasticity > 0.0f) {
			if (Math.abs(o.dx) < 0.005f) {
				o.dx = 0.0f;
			}
			if (Math.abs(o.dy) < 0.005f) {
				o.dy = 0.0f;
			}
		}
	}

	/**
	 * manage actions for this state.
	 * 
	 * @param keyBind the key bound to action.
	 */
	public void action(KeyBinding keyBind) {
		switch (keyBind) {
		/**
		 * process the exit request.
		 */
		case QUIT:
			app.requestExit();
			logger.debug("Request exiting");
			break;
		/**
		 * process the pause request.
		 */
		case PAUSE:
			app.switchPause();

			logger.debug(String.format("Pause reuqest %b", app.isPause()));
			break;
		/**
		 * Manage Enemies set.
		 */
		case FIRE1:
			createGameObjects(app, "enemy_", 10);
			break;

		case FIRE2:
			removeGameObjects(app, "enemy_", 10);
			break;

		case FIRE3:
			createGameObjects(app, "enemy_", 100);
			break;

		case FIRE4:
			removeGameObjects(app, "enemy_", 100);
			break;

		/**
		 * remove all enemies
		 */
		case RESET:
			removeGameObjects(app, "enemy_", -1);
			break;
		/**
		 * 
		 * Write a screenshot to User home folder.
		 */
		case SCREENSHOT:
			app.setPause(true);
			Render.screenshot(app);
			app.setPause(false);
			break;

		/**
		 * Manage Debug level.
		 */
		case DEBUG:
			app.switchDebugMode();
			break;

		/**
		 * Switch between window and fullscreen mode.
		 */
		case FULLSCREEN:
			app.switchFullScreen();
			break;

		default:
			break;
		}
	}


	public String getName() {
		return "SampleGameState";
	}

}