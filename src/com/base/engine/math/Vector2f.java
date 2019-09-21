package com.base.engine.math;

public class Vector2f
{
    private float x, y;

    public Vector2f(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public float length()
    {
        return (float)Math.sqrt(x * x + y * y);
    }

    public float dot(Vector2f vec)
    {
        return this.x * vec.getX() + this.y * vec.getY();
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
}

