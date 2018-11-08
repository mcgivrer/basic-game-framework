/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf.core.resources;

/**
 * the ResourceUnknownException class is thrown when a resource is not found.
 *
 * @author Frédéric Delorme <frederic.delorme@snapgames.fr>
 */
public class ResourceUnknownException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public ResourceUnknownException(String message) {
		super(message);
	}

}