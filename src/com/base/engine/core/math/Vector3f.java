package com.base.engine.core.math;

public class Vector3f
{
    private float x, y, z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float length()
    {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    public float dot(Vector3f r)
    {
        return x * r.getX() + y * r.getY() + z * r.getZ();
    }

    public Vector3f cross(Vector3f r)
    {
        float x_ = y * r.getZ() - z * r.getY();
        float y_ = z * r.getX() - x * r.getZ();
        float z_ = x * r.getY() - y * r.getX();

        return new Vector3f(x_, y_, z_);
    }

    public Vector3f normalized()
    {
        float length = length();

        return new Vector3f(x / length, y / length, z / length);
    }

    public Vector3f rotate(float angle, Vector3f axis)
    {
        float sinHalfAngle = (float)Math.sin(Math.toRadians(angle / 2));
        float cosHalfAngle = (float)Math.cos(Math.toRadians(angle / 2));

        float rX = axis.getX() * sinHalfAngle;
        float rY = axis.getY() * sinHalfAngle;
        float rZ = axis.getZ() * sinHalfAngle;
        float rW = cosHalfAngle;

        Quaternion rotation = new Quaternion(rX, rY, rZ, rW);
        Quaternion conjugate = rotation.conjugate();

        Quaternion w = rotation.multiply(this).multiply(conjugate);

        return new Vector3f(w.getX(), w.getY(), w.getZ());
    }

    // linearly interpolate between 2 points to find a point along
    // the line between 2 endpoints
    public Vector3f lerp(Vector3f dest, float lerpFactor)
    {
        return dest.subtract(this).multiply(lerpFactor).add(this);
    }

    public Vector3f abs()
    {
        return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public Vector3f add(Vector3f r)
    {
        return new Vector3f(x + r.getX(), y + r.getY(), z + r.getZ());
    }

    public Vector3f add(float r)
    {
        return new Vector3f(x + r, y + r, z + r);
    }

    public Vector3f subtract(Vector3f r)
    {
        return new Vector3f(x - r.getX(), y - r.getY(), z - r.getZ());
    }

    public Vector3f subtract(float r)
    {
        return new Vector3f(x - r, y - r, z - r);
    }

    public Vector3f multiply(float r)
    {
        return new Vector3f(x * r, y * r, z * r);
    }

    public Vector3f divide(float r)
    {
        return new Vector3f(x / r, y / r, z / r);
    }

    @Override
    public String toString() {

        return "Vector3f{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }

    // vector swizzling
    public Vector2f getXY() { return new Vector2f(x, y); }
    public Vector2f getYZ() { return new Vector2f(y, z); }
    public Vector2f getZX() { return new Vector2f(z, x); }

    public Vector2f getYX() { return new Vector2f(y, x); }
    public Vector2f getZY() { return new Vector2f(z, y); }
    public Vector2f getXZ() { return new Vector2f(x, z); }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object r) {
        if (this == r) return true;
        if (r == null || getClass() != r.getClass()) return false;

        Vector3f vector3f = (Vector3f) r;

        if (Float.compare(vector3f.x, x) != 0) return false;
        if (Float.compare(vector3f.y, y) != 0) return false;
        return Float.compare(vector3f.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }


}
