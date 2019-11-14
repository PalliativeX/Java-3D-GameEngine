package com.base.engine.components;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Color;
import com.base.engine.rendering.Shader;

public class BaseLight extends GameComponent
{
    private Color color;
    private float intensity;
    private Shader shader;

    private boolean shadowsApplied = false;

    public BaseLight(Vector3f color, float intensity)
    {
        this.color = new Color(color.getX(), color.getY(), color.getZ());
        this.intensity = intensity;
    }

    public BaseLight(Color color, float intensity)
    {
        this.color = color;
        this.intensity = intensity;
    }

    @Override
    public void addToEngine(CoreEngine engine)
    {
        engine.getRenderingEngine().addLight(this);
    }

    public void setShader(Shader shader)
    {
        this.shader = shader;
    }

    public Shader getShader()
    {
        return shader;
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public void setColor(Vector3f color)
    {
        this.color = new Color(color.getX(), color.getY(), color.getZ());
    }

    public float getIntensity()
    {
        return intensity;
    }

    public void setIntensity(float intensity)
    {
        this.intensity = intensity;
    }

    public boolean areShadowsApplied()
    {
        return shadowsApplied;
    }

    protected void setShadowsApplied(boolean newShadowsApplied)
    {
        this.shadowsApplied = newShadowsApplied;
    }
}
