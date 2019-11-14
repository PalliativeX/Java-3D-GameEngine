package com.base.engine.components;

import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.light.Attenuation;
import com.base.engine.rendering.light.ForwardPoint;

public class PointLight extends BaseLight
{
    private static final int COLOR_DEPTH = 256;

    private Attenuation attenuation;
    private float range;

    public PointLight(Vector3f color, float intensity, Attenuation attenuation)
    {
        super(color, intensity);
        this.attenuation = attenuation;

        // calculating the possible range
        float a = attenuation.getExponent();
        float b = attenuation.getLinear();
        float c = attenuation.getConstant() - COLOR_DEPTH * getIntensity() * getColor().maxColorComponent();

        this.range = (float)(-b + Math.sqrt(b * b - 4 * a * c))/(2 * a);

        setShader(ForwardPoint.getInstance());
    }

    public float getRange()
    {
        return range;
    }

    public void setRange(float range)
    {
        this.range = range;
    }

    public Attenuation getAttenuation()
    {
        return attenuation;
    }


}
