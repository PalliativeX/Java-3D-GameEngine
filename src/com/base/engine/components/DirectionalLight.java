package com.base.engine.components;

import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.light.ForwardDirectional;

public class DirectionalLight extends BaseLight
{
    public DirectionalLight(Vector3f color, float intensity)
    {
        super(color, intensity);

        setShadowsApplied(true);

        setShader(ForwardDirectional.getInstance());
    }

    public Vector3f getDirection()
    {
        return getTransform().getTransformedRotation().getForward();
    }

}

