package com.base.engine.components;

import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.light.*;

public class SpotLight extends PointLight
{
    private float cutOff;

    public SpotLight(Vector3f color, float intensity, Attenuation attenuation, float cutOff)
    {
        super(color, intensity, attenuation);
        this.cutOff = cutOff;

        setShader(ForwardSpot.getInstance());
    }


    public Vector3f getDirection() {
        return getTransform().getTransformedRotation().getForward();
    }

    public float getCutOff() {
        return cutOff;
    }

    public void setCutOff(float cutOff) {
        this.cutOff = cutOff;
    }


}
