/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf.sample.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.snapgames.bgf.core.Game;
import fr.snapgames.bgf.core.entity.BoundingBox.BoundingBoxType;
import fr.snapgames.bgf.core.entity.Camera;
import fr.snapgames.bgf.core.entity.GameEntity;
import fr.snapgames.bgf.core.entity.GameObject;
import fr.snapgames.bgf.core.gfx.Render;
import fr.snapgames.bgf.core.gfx.ui.UIText;
import fr.snapgames.bgf.core.io.InputListener;
import fr.snapgames.bgf.core.io.InputListener.KeyBinding;
import fr.snapgames.bgf.core.math.Vector2D;
import fr.snapgames.bgf.core.resources.ResourceUnknownException;
import fr.snapgames.bgf.core.states.GameState;
import fr.snapgames.bgf.core.states.GameStateDefault;

/**
 * This is a Sample GameState implementation to illustrate the usage of such
 * class. It provides basic object like blue and red balls. Where the **blue
 * ball** simulates the player, and the red balls enemies.
 * 
 * @author Frédéric Delorme.
 */
public class SampleGameState extends GameStateDefault implements GameState {

	public static final String NAME = "SampleGameState";

	private static final Logger logger = LoggerFactory.getLogger(SampleGameState.class);

	/**
	 * Some score variable.
	 */
	private int score = 0;

	/**
	 * some resources needed for the demonstration.
	 */
	private Font scoreFont;
	private UIText scoreUiText;
	private GameObject player;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.snapgames.bgf.core.states.GameStateDefault#initialize(fr.snapgames.bgf.
	 * core.Game)
	 */
	@Override
	public void initialize(Game app) {
		this.app = app;

		app.resManager.addResource("images/playerBall", "res/images/blue-bouncing-ball-64x64.png");
		app.resManager.addResource("images/enemyBall", "res/images/red-bouncing-ball-64x64.png");
		app.soundCtrl.load("sounds/boing", "res/audio/sounds/boing1.wav");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.snapgames.bgf.core.states.GameState#create(fr.snapgames.bgf.core.Game,
	 * long)
	 */
	@Override
	public void create(Game app, long uid) {
		this.uid = uid;

		scoreFont = app.getRender().getGraphics().getFont().deriveFont(16.0f);

		scoreUiText = (UIText) UIText.builder("score").setFont(scoreFont).setText("00000").setThickness(2)
				.moveTo(12, 24).setLayer(20).setPriority(100).setFixed(true);
		add(scoreUiText);

		try {
			// create a simple blue ball as a player
			player = GameObject.builder("player").setSize(32, 32).setImage(app.resManager.getImage("images/playerBall"))
					.setScale(1f).moveTo(0, 0).setColor(Color.GREEN).setVelocity(0.0f, 0.0f).setLayer(10)
					.setPriority(100).setElasticity(0.0f).setFriction(0.45f).setBoundingType(BoundingBoxType.CIRCLE);
			add(player);
			// add a camera to follow the player object in a centered cam viewport.
			Camera cam1 = ((Camera) Camera.builder("cam1").setDebugInfoOffset(new Vector2D("offset"))).setTarget(player)
					.setTween(0.98f).setView(app.getRender().getViewport());
			add(cam1);

		} catch (ResourceUnknownException rue) {
			logger.error("Unable to load the resource", rue);
		}

		createGameObjects(app, "enemy_", 10);

	}

	/**
	 * Create nbEnemies in the playground.
	 * 
	 * @param nbEnemies
	 */
	private void createGameObjects(Game app, String baseName, int nbEnemies) {
		app.suspendRendering(true);
		Rectangle vp = app.getRender().getViewport();
		try {
			for (int i = 0; i < nbEnemies; i++) {
				GameObject enemy = GameObject.builder(baseName + objects.size() + 1).setSize(16, 16)
						.setImage(app.resManager.getImage("images/enemyBall"))
						.moveTo((int) (Math.random() * vp.width), (int) (Math.random() * vp.height))
						.setVelocity((float) (Math.random() * 0.4f) - 0.2f, (float) (Math.random() * 0.4f) - 0.2f)
						.setColor(randomColor()).setPriority(100 - i).setLayer(1).setElasticity(0.98f)
						.setFriction(0.96f).setBoundingType(BoundingBoxType.CIRCLE);
				add(enemy);
			}
		} catch (ResourceUnknownException rue) {
			logger.error("Unable to load the resource", rue);
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
	 * Remove {@link GameObject} from the {@link Game} where name contains with
	 * nameFilter if nbToRemove equals -1, all corresponding
	 * <code>nameFilter</code>ed object will be removed.
	 * 
	 * @param nameFilter
	 * @param i
	 */
	private void removeGameObjects(Game app, String nameFilter, int nbToRemove) {
		app.suspendRendering(true);

		// parse Object map and remove matching object with filtering string.
		// Map -> Stream -> Filter -> MAP
		Map<String, GameEntity> collect = objects.entrySet().stream()
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
	public void dispose(Game app) {
		// TODO next time some release of resource will implemented here.
	}

	@Override
	public void input(Game app, InputListener inputListener) {
		GameObject goPlayer = (GameObject) objects.get("player");

		float factor = 0.05f;
		if (inputListener.getKey(KeyEvent.VK_SHIFT)) {
			factor = 0.075f;
		} else if (inputListener.getKey(KeyEvent.VK_CONTROL)) {
			factor = 0.1f;
		}

		if (inputListener.getKey(KeyEvent.VK_LEFT)) {
			goPlayer.speed.x = -0.01f * factor;
		}
		if (inputListener.getKey(KeyEvent.VK_RIGHT)) {
			goPlayer.speed.x = 0.01f * factor;
		}
		if (inputListener.getKey(KeyEvent.VK_UP)) {
			goPlayer.speed.y = -0.01f * factor;
		}
		if (inputListener.getKey(KeyEvent.VK_DOWN)) {
			goPlayer.speed.y = 0.01f * factor;
		}
		goPlayer.speed.y *= goPlayer.friction;
		goPlayer.speed.x *= goPlayer.friction;

	}

	@Override
	public void update(Game app, long dt) {
		super.update(app, dt);
		for (Entry<String, GameEntity> entry : objects.entrySet()) {
			constrains(app, entry.getValue());
		}

		scoreUiText.setText(String.format("%05d", score++));
	}

	/**
	 * Contained object to Game viewport display.
	 * 
	 * @param o
	 */
	private void constrains(Game app, GameEntity ge) {

		boolean colliding = false;

		GameObject o = (GameObject) ge;

		// detect is there are some collision with out viewport border

		if (o.position.x + o.size.x > app.getRender().getViewport().width || o.position.x < 0.0f) {
			o.speed.x = -o.speed.x * o.elasticity;

			colliding = true;
		}
		if (o.position.y + o.size.y > app.getRender().getViewport().height || o.position.y < 0.0f) {

			o.speed.y = -o.speed.y * o.elasticity;
			colliding = true;
		}

		/*
		 * speed threshold constraints if (o.friction > 0.0f || o.elasticity > 0.0f) {
		 * if (Math.abs(o.dx) < 0.0005f) { o.dx = 0.0f; } if (Math.abs(o.dy) < 0.0005f)
		 * { o.dy = 0.0f; } }
		 */

		// if colliding and the object is the player, play boing.
		if (colliding && o.getName().equals(player.getName()) && !app.soundCtrl.isPlaying("sounds/boing")) {

			// app.soundCtrl.play("sounds/boing");
		}

		o.speed.x = boxingValue(o.speed.x, -0.3f, 0.3f);
		o.speed.y = boxingValue(o.speed.y, -0.3f, 0.3f);
		o.acceleration.x = boxingValue(o.acceleration.x, -1.0f, 1.0f);
		o.acceleration.y = boxingValue(o.acceleration.y, -1.0f, 1.0f);

		o.position.x = boxingValue(o.position.x, 0.0f, app.getRender().getViewport().width - o.size.x);
		o.position.y = boxingValue(o.position.y, 0.0f, app.getRender().getViewport().height - o.size.y);
	}

	/**
	 * compare value to a threshold. if value greater than threshold return
	 * threshold else return value.
	 * 
	 * @param value
	 * @param threshold
	 * @return
	 */
	private float maxThresholdValue(float value, float threshold) {
		return (value > threshold ? threshold : value);
	}

	private float minThresholdValue(float value, float threshold) {
		return (value < threshold ? threshold : value);
	}

	private float boxingValue(float value, float min, float max) {
		return maxThresholdValue(minThresholdValue(value, min), max);
	}

	/**
	 * manage actions for this state.
	 * 
	 * @param keyBind the key bound to action.
	 */
	public void action(KeyBinding keyBind) {
		switch (keyBind) {
		case UP:
			break;
		case DOWN:
			break;
		case LEFT:
			break;
		case RIGHT:
			break;
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
			logger.debug("Add 10 enemies");
			break;

		case FIRE2:
			removeGameObjects(app, "enemy_", 10);
			logger.debug("Remove 10 enemies");
			break;

		case FIRE3:
			createGameObjects(app, "enemy_", 100);
			logger.debug("Add 100 enemies");
			break;

		case FIRE4:
			removeGameObjects(app, "enemy_", 100);
			logger.debug("Remove 100 enemies");
			break;

		/**
		 * remove all enemies
		 */
		case RESET:
			removeGameObjects(app, "enemy_", -1);
			logger.debug("Add ALL enemies");
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
		return NAME;
	}
}