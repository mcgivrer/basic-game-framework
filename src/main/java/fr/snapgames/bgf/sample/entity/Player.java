/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf.sample.entity;

import java.util.HashMap;
import java.util.Map;

import fr.snapgames.bgf.core.entity.GameEntity;
import fr.snapgames.bgf.core.entity.GameObject;
import fr.snapgames.bgf.core.entity.TGameObject;

/**
 * The Player Object is a GameObject with some internal specific attributes (more to
 * come).
 */
public class Player extends TGameObject<Player> {

    /**
     * create a new Player with a <code>name</code> and initiaze the mandatory
     * object properties:
     * <ul>
     * <li><code>life</code> the number of remaining lifes for this player,</li>
     * <li><code>energy</code> the energy level of this player,</li>
     * <li><code>mana</code> the mana level for this player,</li>
     * <li><code>score</code> this is the player game score,</li>
     * <li><code>inventory</code> the object inventory for this player object.</li>
     * </ul>
     * 
     * @param name
     */
    public Player(String name) {
        super(name);
        properties.put("life", 3);
        properties.put("energy", 100);
        properties.put("mana", 100);
        properties.put("score", 0);
        properties.put("inventory", new HashMap<String, GameEntity>());
    }


    /**
     * Add an item to the inventory
     */
    public void addItem(GameObject o) {
        Map<String, GameEntity> items = (Map) properties.get("inventory");
        items.put(o.getName(), o);
    }

    /**
     * remove an intem from the inventory.
     */
    public void removeItem(GameObject o) {
        Map<String, GameEntity> items = (Map) properties.get("inventory");
        items.remove(o.getName());
    }
}