/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf.core.math.physic;

import fr.snapgames.bgf.core.entity.BoundingBox;
import fr.snapgames.bgf.core.entity.GameObject;

public class Collidable {
    public Collidable(GameObject obj) {
        this.boundingBox = obj.getBoundingBox();
        this.parent = obj;
    }

    public boolean top, bottom, left, right;
    public boolean constraintsToViewport;
    public BoundingBox boundingBox;

    public GameObject parent;

    float getMidX() {
        return parent.position.multiply(0.5f).x;
    }

    float getMidY() {
        return parent.position.multiply(0.5f).y;
    }

    float getMidWidth() {
        return parent.size.multiply(0.5f).x;
    }

    float getMidHeight() {
        return parent.size.multiply(0.5f).y;
    }

    float getRight(){
        return parent.position.x+parent.size.x;
    }
    float getLeft(){
        return parent.position.x;
    }
    float getTop(){
        return parent.position.y;
    }
    float getBottom(){
        return parent.position.y+parent.size.y;
    }


    String getCollidingSide(){
        String collidingSide=",";
        if(left) collidingSide+="LEFT,";
        if(right) collidingSide+="RIGHT,";
        if(top) collidingSide+="TOP,";
        if(bottom) collidingSide+="BOTTOM,";
        collidingSide=collidingSide.substring(0,collidingSide.lastIndexOf(","));
        return collidingSide;
    }

}