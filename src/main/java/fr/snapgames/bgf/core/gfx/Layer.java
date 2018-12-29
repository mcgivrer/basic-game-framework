/**
 * 
 */
package fr.snapgames.bgf.core.gfx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.snapgames.bgf.core.entity.GameEntity;

/**
 * @author Frederic Delorme<frederic.delorme@snapgames.fr>
 *
 */
/**
 * an object to manage some layers into the main rendering pipeline.
 */
class Layer {
	public int id = 0;
	public String name;
	private List<GameEntity> entities = new ArrayList<GameEntity>();

	/**
	 * Create a new layer with a specific id.
	 * 
	 * @param layerId
	 */
	public Layer(int layerId) {
		this.id = layerId;
	}

	/**
	 * Add an entity to the layer.
	 * 
	 * @param e
	 */
	public void addEntity(GameEntity e) {
		if (!entities.contains(e)) {
			entities.add(e);
		}
		Collections.sort(entities, new Comparator<GameEntity>() {
			public int compare(GameEntity e1, GameEntity e2) {
				return (e1.getPriority() < e2.getPriority() ? -1 : 1);
			}
		});
	}

	/**
	 * get the entities from the layer.
	 * 
	 * @return
	 */
	List<GameEntity> getEntities() {
		return entities;
	}

}