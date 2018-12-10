/**
 * SnapGames
 * 
 * @year 2018
 * 
 */
package fr.snapgames.bgf.core.math.physic;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.snapgames.bgf.core.Game;
import fr.snapgames.bgf.core.entity.GameObject;
import fr.snapgames.bgf.core.math.Vector2D;

/**
 * The CollisionSystem will provide a Collision detection algorithm and a
 * Response algorithm.
 * 
 * @author Frédéric Delorme
 *
 */
public class CollisionSystem implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(CollisionSystem.class);
    private static final float STICKY_THRESHOLD = 0.004f;
    private boolean stop = false;
    private Game game;

    /**
     * 
     * <p>
     * Physic behavior of computation for any game element.
     * <p>
     * A Physic behavior can be on of the following :
     * <ul>
     * <li><code>KINEMATIC</code> for any object where physic must be computed
     * without gravity effect.</li>
     * <li><code>DYNAMIC</code> for any object where physic must be computed WITH
     * integrating gravity effect</li>
     * <li><code>STATIC</code> for any object that does not move.</li>
     * </ul>
     * 
     * @author Frédéric Delorme
     *
     */
    public enum Physic {
        /**
         * any element not on gravity influence.
         */
        KINEMATIC,
        /**
         * any element moving in scene with gravity.
         */
        DYNAMIC,
        /**
         * any element static in scene.
         */
        STATIC
    }

    public CollisionSystem(Game game) {
        super();
        this.game = game;
    }

    /**
     * <p>
     * Dynamic object list.
     * <p>
     * Here is the list of moving object in the scene.
     */
    private List<GameObject> dynamicObjects = new ArrayList<>();
    /**
     * <p>
     * Static Object List
     * <p>
     * Here is the list of static object whose can be blocker for dynamic object
     * movement.
     */
    private List<GameObject> staticObjects = new ArrayList<>();

    /**
     * <p>
     * Add an any GameObject <code>obj</code> to the computing stack.
     * <ul>
     * <li>If {@link GameObject#type} is {@link Physic#DYNAMIC}, it will be added to
     * Dynamic List object.</li>
     * <li>If {@link GameObject#type} is {@link Physic#KINEMATIC}, the object will
     * be added to the static object list.</li>
     * </ul>
     * 
     * @param obj the GameObject to be added to collision System object set.
     */
    public void addCollider(GameObject obj) {
        switch (obj.type) {
        case STATIC:
            staticObjects.add(obj);
            break;
        case DYNAMIC:
        case KINEMATIC:
            dynamicObjects.add(obj);
            break;
        }
    }

    /**
     * <p>
     * Compute collision between object and create response if collision.
     * <p>
     * Test collision between all {@link Physic#DYNAMIC} object list items against
     * all {@link Physic#KINEMATIC} and {@link Physic#STATIC} object list items.
     * <p>
     * If a {@link Physic#DYNAMIC} object collide a {@link Physic#KINEMATIC} or
     * {@link Physic#STATIC} object, a collision response is computed.
     * <p>
     * TODO must take in account friction and elasticity ASAP.
     */
    public void collide(float elapsed) {
        // updateObjects(dynamicObjects, elapsed);
        for (GameObject dyna : dynamicObjects) {
            // if object need to be constraint into Viewport.
            if (dyna.constraintsToViewport) {
                constrainsInViewPort(dyna);
            }
            // compute collision with Kinematic Objects
            for (GameObject kinema : staticObjects) {
                if (!dyna.equals(kinema)) {
                    dyna.top = false;
                    dyna.bottom = false;
                    dyna.left = false;
                    dyna.right = false;
                    if (dyna.boundingBox.intersects(kinema.boundingBox)) {
                        // A collision exist, compute response.
                        computeRestitution(dyna, kinema);
                        logger.debug(String.format("%s collides %s at (%f,%f) on %s", dyna.getName(), kinema.getName(),
                                dyna.position.x, dyna.position.y, dyna.getCollidingSide()));
                        if (GamePanel.debug) {
                            dyna.color = Color.RED;
                            kinema.color = Color.RED;
                        }

                    } else {
                        if (game.getDebugMode() > 2) {
                            dyna.color = Color.GREEN;
                            kinema.color = Color.DARK_GRAY;
                        }
                    }
                }
            }

        }
    }

    /**
     * Apply ViewPort constraints on dynamic object only.
     * 
     * @param o
     */
    private void constrainsInViewPort(Collaidable c) {
        Rectangle viewport = game.getRender().getViewport();
        GameObject o = c.parent;
        if (o.newPosition.x < viewport.x) {
            o.newPosition.x = viewport.x;
        }
        if (o.newPosition.y < viewport.y) {
            o.newPosition.y = viewport.y;
            o.speed.y = 0;
        }
        if (o.newPosition.x > viewport.width - o.width) {
            o.newPosition.x = viewport.width - o.width;
        }
        if (o.newPosition.y >= GamePanel.viewport.height - o.height) {
            o.newPosition.y = viewport.height - o.height;
            o.speed.y = 0;
        }
    }

    /**
     * <p>
     * Compute restitution for colliding between 2 {@link GameObject}
     * <code>entity1</code> on <code>entity2</code>. The resulting behavior for
     * <code>entity1</code> will take in account <code>entity2.restitution</code>
     * and <code>entity1.friction</code>.
     * </p>
     * 
     * @param entity1 the {@link GameObject} to test collision over
     *                <code>entity2</code>
     * @param entity2 the {@link GameObject} to test colliding with.
     */
    private void computeRestitution(GameObject entity1, GameObject entity2) {
        // Find the mid points of the entity and player
        float pMidX = entity1.getMidX();
        float pMidY = entity1.getMidY();
        float aMidX = entity2.getMidX();
        float aMidY = entity2.getMidY();

        // To find the side of entry calculate based on
        // the normalized sides
        float dx = (aMidX - pMidX) / entity2.midWidth;
        float dy = (aMidY - pMidY) / entity2.midHeight;

        // Calculate the absolute change in x and y
        float absDX = Math.abs(dx);
        float absDY = Math.abs(dy);

        // compute restitution.
        float restitution = Math.min(entity1.restitution, entity2.restitution);

        // If the distance between the normalized x and y
        // position is less than a small threshold (.1 in this case)
        // then this object is approaching from a corner
        if (Math.abs(absDX - absDY) < .1) {

            // If the player is approaching from positive X
            if (dx < 0) {

                // Set the player x to the right side
                entity1.newPosition.x = entity2.getRight();
                entity1.right = true;
                entity1.speed.x = 0;
                entity1.acceleration.x = 0;

                // If the player is approaching from negative X
            } else {

                // Set the player x to the left side
                entity1.newPosition.x = entity2.getLeft() - entity1.width;
                entity1.left = true;
                entity1.speed.x = 0;
                entity1.acceleration.x = 0;

            }

            // If the player is approaching from positive Y
            if (dy < 0) {

                // Set the player y to the bottom
                entity1.newPosition.y = entity2.getBottom();
                entity1.top = true;
                entity1.speed.y = 0;
                entity1.acceleration.y = 0;

                // If the player is approaching from negative Y
            } else {

                // Set the player y to the top
                entity1.newPosition.y = entity2.getTop() - entity1.height;
                entity1.bottom = true;
                entity1.speed.y = 0;
                entity1.acceleration.y = 0;
            }

            // Randomly select a x/y direction to reflect velocity on
            if (Math.random() < .5) {

                // Reflect the velocity at a reduced rate
                entity1.speed.x = -entity1.speed.x * restitution;

                // If the object's velocity is nearing 0, set it to 0
                // STICKY_THRESHOLD is set to .0004
                if (Math.abs(entity1.speed.x) < STICKY_THRESHOLD) {
                    entity1.speed.x = 0;
                    entity1.acceleration.x = 0;

                }
            } else {

                entity1.speed.y = -entity1.speed.y * restitution;
                if (Math.abs(entity1.speed.y) < STICKY_THRESHOLD) {
                    entity1.speed.y = 0;
                    entity1.acceleration.y = 0;

                }
            }

            // If the object is approaching from the sides
        } else if (absDX > absDY) {

            // If the player is approaching from positive X
            if (dx < 0) {
                entity1.newPosition.x = entity2.getRight();
                entity1.right = true;

            } else {
                // If the player is approaching from negative X
                entity1.newPosition.x = entity2.getLeft() - entity1.width;
                entity1.left = true;
            }

            // Velocity component
            entity1.speed.x = -entity1.speed.x * entity2.restitution;

            if (Math.abs(entity1.speed.x) < STICKY_THRESHOLD) {
                entity1.speed.x = 0;
                entity1.acceleration.x = 0;
            }

            // If this collision is coming from the top or bottom more
        } else {

            // If the player is approaching from positive Y
            if (dy < 0) {
                entity1.newPosition.y = entity2.getBottom();
                entity1.top = true;

            } else {
                // If the player is approaching from negative Y
                entity1.newPosition.y = entity2.getTop() - entity1.height;
                entity1.bottom = true;
            }

            // Velocity component
            entity1.speed.y = -entity1.speed.y * entity2.restitution;
            if (Math.abs(entity1.speed.y) < STICKY_THRESHOLD) {
                entity1.speed.y = 0;
                entity1.acceleration.y = 0;
            }
        }
    }

    /**
     * Compute Physics for objects.
     * 
     * @param objects
     * @param elapsed
     */
    public void updateObjects(Collection<GameObject> objects, float elapsed) {
        for (GameObject object : objects) {
            update(object, elapsed);
        }
    }

    /**
     * Update one particular object upon its physic type of behavior.
     * 
     * @param go
     * @param elapsed
     */
    public void update(GameObject go, float elapsed) {

        switch (go.type) {
        case DYNAMIC:

            go.acceleration = constraint(go.acceleration, 0.5f);
            go.speed.x += (go.acceleration.x + go.gravity.x) * (elapsed) * go.mass;
            go.speed.y += (go.acceleration.y + go.gravity.y) * (elapsed) * go.mass;
            go.speed = constraint(go.speed, 0.5f);
            go.speed = go.speed.multiply(go.friction);

            go.newPosition.x += go.speed.x * elapsed;
            go.newPosition.y += go.speed.y * elapsed;
            break;

        case KINEMATIC:
            go.acceleration = constraint(go.acceleration, 0.5f);
            go.speed.x += go.acceleration.x * elapsed;
            go.speed.y += go.acceleration.y * elapsed;
            go.speed = constraint(go.speed, 0.5f);
            go.speed = go.speed.multiply(go.friction);

            go.newPosition.x += go.speed.x * elapsed;
            go.newPosition.y += go.speed.y * elapsed;
            break;

        case STATIC:
            go.newPosition.x = go.position.x;
            go.newPosition.y = go.position.y;
            break;
        }

        go.boundingBox.x = (int) go.position.x;
        go.boundingBox.y = (int) go.position.y;
        go.boundingBox.width = (int) go.size.x;
        go.boundingBox.height = (int) go.size.y;
    }

    private Vector2D constraint(Vector2D vector, float f) {
        vector.x = Math.min(vector.x, f);
        vector.y = Math.min(vector.y, f);
        return vector;
    }

    @Override
    public void run() {
        long elapsed = System.nanoTime();
        while (!stop) {
            collide(elapsed);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                logger.error("Unable to wait 1ms until next physic computation cycle.", e);
            }
        }

    }

    /**
     * return running status of the CollisionSystem.
     * 
     * @return
     */
    public boolean isStopped() {
        return stop;
    }

    /**
     * request Collision System to stop.
     * 
     * @param bStop
     */
    public void stop(boolean bStop) {
        this.stop = bStop;
    }
}
