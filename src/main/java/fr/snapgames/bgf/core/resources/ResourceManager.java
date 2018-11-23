/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf.core.resources;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * the ResourceManager class intends to load and cache some objects like image,
 * sounds, font, etc... any resources.
 *
 * @author Frédéric Delorme <frederic.delorme@snapgames.fr>
 */
public class ResourceManager {
	private Map<String, Object> objects = new HashMap<>();

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
}