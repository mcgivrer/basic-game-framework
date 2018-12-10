package fr.snapgames.bgf.core.math.physic;

import fr.snapgames.bgf.core.entity.BoundingBox;
import fr.snapgames.bgf.core.entity.GameObject;

public class Collidable{
    public boolean top, bottom,left,right;
    public boolean constraintsToViewPort;
    public BoundingBox boundingBox;

    public GameObject parent;

}