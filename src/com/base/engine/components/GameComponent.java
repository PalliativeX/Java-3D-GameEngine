package com.base.engine.components;

import com.base.engine.core.*;
import com.base.engine.rendering.*;
import com.base.engine.core.math.Transform;

public abstract class GameComponent
{
    private GameObject parent;

    public void input(float delta) {}
    public void update(float delta) {}
    public void render(Shader shader, RenderingEngine renderingEngine) {}

    public void setParent(GameObject parent)
    {
        this.parent = parent;
    }

    public Transform getTransform()
    {
        return parent.getTransform();
    }

    public void addToEngine(CoreEngine engine) {}
}
