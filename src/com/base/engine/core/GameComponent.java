package com.base.engine.core;

import com.base.engine.core.math.Transform;
import com.base.engine.rendering.shaders.Shader;

public abstract class GameComponent
{
    public abstract void input(Transform transform);

    public abstract void update(Transform transform);

    public abstract void render(Transform transform, Shader shader);

}
