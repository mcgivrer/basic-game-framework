/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf.core.resources;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import fr.snapgames.bgf.core.Game;

/**
 * the ResourceManager class intends to load and cache some objects like image,
 * sounds, font, etc... any resources.
 *
 * @author Frédéric Delorme <frederic.delorme@snapgames.fr>
 */
public class ResourceManager {
	/**
	 * internal instance.
	 */
	private static ResourceManager instance;
	/**
	 * list of resources manage by this service.
	 */
	private Map<String, Object> objects;

	private Game game;

	/**
	 * Create the resource manager and initialize resource map.
	 */
	private ResourceManager() {
		objects = new ConcurrentHashMap<>();
	}

	/**
	 * Add a resource to the set.
	 *
	 * @param name name for this resource
	 * @param path path to the rsource.
	 */
	public void addResource(String name, String path) {
		// Manage image (PNG or JPG)
		if (path.toLowerCase().endsWith(".png") || path.toLowerCase().endsWith(".png")) {
			try {
				BufferedImage image = ImageIO.read(this.getClass().getResourceAsStream("/" + path));
				objects.put(name, image);
			} catch (Exception e) {
				System.err.println(String.format("Unable to find %s and store resource as %s.", path, name));
				System.exit(-1);
			}
		}
	}

	/**
	 * retrieve an image from the resource set.
	 *
	 * @param name the name of the resource to retrieve.
	 * @return the BufferedImage extracted from the resource set.
	 * @throws ResourceUnknownException
	 */
	public BufferedImage getImage(String name) throws ResourceUnknownException {
		if (objects.containsKey(name)) {
			return (BufferedImage) objects.get(name);
		} else {
			throw new ResourceUnknownException(String.format("Unknown resource named %s", name));
		}
	}

	/**
	 * get the instance of the resource manager.
	 * 
	 * @return
	 */
	public static ResourceManager getInstance(Game game) {
		if (instance == null) {
			instance = new ResourceManager();
		}
		instance.game = game;
		return instance;
	}
}