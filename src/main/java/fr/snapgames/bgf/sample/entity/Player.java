/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf.sample.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import fr.snapgames.bgf.core.entity.GameObject;
import fr.snapgames.bgf.core.entity.TGameObject;

/**
 * The Player Object is a GameObject with some internal specific attributes
 * (more to come).
 */
public class Player extends GameObject {

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
    public Player() {
        super()
        properties.put("life", 3);
        properties.put("energy", 100);
        properties.put("mana", 100);
        properties.put("score", 0);
        properties.put("inventory", new ConcurrentHashMap<String, Object>());
    }

    /**
     * Add an item to the inventory
     */
    public void addInventoryItem(String itemName, Object itemObject) {
        Map<String, Object> items = (Map) properties.get("inventory");
        items.put(itemName, itemObject);
    }

    /**
     * remove an intem from the inventory.
     */
    public void removeInventoryItem(String itemName, Object itemObject) {
        Map<String, Object> items = (Map) properties.get("inventory");
        items.remove(itemName);
    }

    /**
     * Add some bucks to the score.
     * 
     * @param value
     * @return
     */
    public int addToScore(int value) {
        return addValueToItem("score", value);
    }

    /**
     * Add vaue to the energy level.
     * 
     * @param value
     * @return
     */
    public int addToEnergy(int value) {
        return addValueToItem("energy", value);
    }

    /**
     * add a value to the Mana level.
     * 
     * @param value
     * @return
     */
    public int addToMana(int value) {
        return addValueToItem("mana", value);
    }

    /**
     * add a value to the life number.
     * 
     * @param value
     * @return
     */
    public int addToLife(int value) {
        return addValueToItem("life", value);
    }


    /**
     * internal change value to inventory specific item.
     * 
     * @param itemName   Name of the (int) item to be modified
     * @param valueToAdd value to be added to the item.
     * @return
     */
    private int addValueToItem(String itemName, int valueToAdd) {
        int value = (int) (properties.get(itemName));
        value += valueToAdd;
        properties.put(itemName, value);
        return value;
    }

    /**
     * internal get item value from the property <code>itemName</code>.
     * 
     * @param itemName
     * @return
     */
    private int getItem(String itemName) {
        int value = (int) (properties.get(itemName));
        return value;
    }

    /**
     * internal get item value from the property <code>itemName</code>.
     * 
     * @param itemName
     * @return
     */
    private void setItem(String itemName, int itemValue) {
        properties.put(itemName, itemValue);
    }

    public Player setScore(int score) {
        setItem("score", score);
        return this;
    }

    public int getScore() {
        return getItem("score");
    }

    public Player setLife(int life) {
        setItem("life", life);
        return this;
    }

    public int getLife() {
        return getItem("life");
    }

    public Player setEnergy(int energy) {
        setItem("energy", energy);
        return this;
    }

    public int getEnergy() {
        return getItem("mana");
    }

    public int getMana() {
        return getItem("mana");
    }

    public Player setMana(int mana) {
        setItem("mana", mana);
        return this;
    }
}