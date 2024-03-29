package com.base.engine.rendering.light;

import com.base.engine.core.math.Vector3f;

public class Attenuation extends Vector3f
{
    public Attenuation(float constant, float linear, float exponent)
    {
        super(constant, linear, exponent);
    }

    public float getConstant()
    {
        return getX();
    }


    public float getLinear()
    {
        return getY();
    }

    public float getExponent()
    {
        return getZ();
    }
}
