package fr.snapgames.bgf.core.math;

/**
 * A 2D Vector class to compute next gen things..
 *
 * @author Frédéric Delorme<frederic.delorme@snapgames.fr>
 */
public class Vector2D {

	private String name;

    /**
     * X axe factor.
     */
    public float x = 0.0f;
    /**
     * Y axe factor.
     */
    public float y = 0.0f;

    /**
     * Create a Vector2D
     */
    public Vector2D(String name) {
        this.x = 0.0f;
        this.y = 0.0f;
        name = "v_noname";
    }

    /**
     * Set the default gravity.
     *
     * @param x
     * @param y
     */
    public Vector2D(String name, float x, float y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    /**
     * add the v vector.
     *
     * @param v
     */
    public Vector2D add(Vector2D v) {
        if (v != null) {
            this.x += v.x;
            this.y += v.y;
        }
        return this;
    }

    /**
     * substract the v vector.
     *
     * @param v
     */
    public Vector2D sub(Vector2D v) {
        return new Vector2D(this.name, x - v.x, y - v.y);
    }

    /**
     * multiply the vector with f.
     *
     * @param f
     */
    public Vector2D multiply(float f) {
        this.x *= f;
        this.y *= f;
        return this;
    }

    /**
     * Compute distance between this vector and the vector <code>v</code>.
     *
     * @param v the vector to compute distance with.
     * @return
     */
    public float distance(Vector2D v) {
        float v0 = x - v.x;
        float v1 = y - v.y;
        return (float) Math.sqrt(v0 * v0 + v1 * v1);
    }

    /**
     * Normalization of this vector.
     */
    public Vector2D normalize() {
        // sets length to 1
        //
        double length = Math.sqrt(x * x + y * y);

        if (length != 0.0) {
            float s = 1.0f / (float) length;
            x = x * s;
            y = y * s;
        }

        return new Vector2D(this.name, x, y);
    }

    /**
     * Dot product for current instance {@link Vector2D} and the <code>v1</code>
     * vector.
     *
     * @param v1
     * @return
     */
    public double dot(Vector2D v1) {
        return this.x * v1.x + this.y * v1.y;
    }

    public String toString() {
        return String.format("(%03.4f,%03.4f)", x, y);
    }

}