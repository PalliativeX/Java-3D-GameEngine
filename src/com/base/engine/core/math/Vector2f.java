package com.base.engine.core.math;

public class Vector2f
{
    private float x, y;

    public Vector2f(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f r)
    {
        this.x = r.x;
        this.y = r.y;
    }

    public float length()
    {
        return (float)Math.sqrt(x * x + y * y);
    }

    public float dot(Vector2f vec)
    {
        return this.x * vec.getX() + this.y * vec.getY();
    }

    public float max()
    {
        return Math.max(x, y);
    }

    public Vector2f normalized()
    {
        float length = length();

        return new Vector2f(x / length, y / length);
    }

    public Vector2f rotate(float angle)
    {
        double radians = Math.toRadians(angle);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        return new Vector2f((float)(x * cos - y * sin),(float)( x * sin + y * cos));
    }

    public Vector2f set(Vector2f r) { this.x = r.x; this.y = r.y; return this; }


    public Vector2f lerp(Vector2f dest, float lerpFactor)
    {
        return dest.subtract(this).multiply(lerpFactor).add(this);
    }

    public Vector2f abs()
    {
        return new Vector2f(Math.abs(x), Math.abs(y));
    }

    public Vector2f add(Vector2f r)
    {
        return new Vector2f(x + r.getX(), y + r.getY());
    }

    public Vector2f add(float r)
    {
        return new Vector2f(x + r, y + r);
    }

    public Vector2f subtract(Vector2f r)
    {
        return new Vector2f(x - r.getX(), y - r.getY());
    }

    public Vector2f subtract(float r)
    {
        return new Vector2f(x - r, y - r);
    }

    public Vector2f multiply(float r)
    {
        return new Vector2f(x * r, y * r);
    }

    public Vector2f divide(float r)
    {
        if (r == 0)
            return this;

        return new Vector2f(x / r, y / r);
    }

    public String toString()
    {
        return "(" + x + " " + y + ")";
    }

    public float getX() {
        return x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2f vector2f = (Vector2f) o;

        if (Float.compare(vector2f.x, x) != 0) return false;
        return Float.compare(vector2f.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }


}

