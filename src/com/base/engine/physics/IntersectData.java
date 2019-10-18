package com.base.engine.physics;

public class IntersectData
{
    private boolean doesIntersect;
    private float distance;

    public IntersectData(boolean doesIntersect, float distance)
    {
        this.doesIntersect = doesIntersect;
        this.distance = distance;
    }

    public boolean doesIntersect()
    {
        return doesIntersect;
    }

    public float getDistance()
    {
        return distance;
    }

}
