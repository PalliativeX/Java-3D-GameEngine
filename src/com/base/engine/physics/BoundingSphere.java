package com.base.engine.physics;

import com.base.engine.core.math.Vector3f;

public class BoundingSphere
{
    private Vector3f center;
    private float radius;

    public BoundingSphere(Vector3f center, float radius)
    {
        this.center = center;
        this.radius = radius;
    }

    IntersectData intersectBoundingSphere(BoundingSphere other)
    {
        float radiusDistance = radius + other.getRadius();
        float centerDistance = (other.getCenter().subtract(center)).length();
        float distance = centerDistance - radiusDistance;

        return new IntersectData(centerDistance < radiusDistance, distance);
    }

    public Vector3f getCenter()
    {
        return center;
    }

    public float getRadius()
    {
        return radius;
    }

}
