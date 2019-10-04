package com.base.game;

import com.base.engine.components.GameComponent;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

public class LookAtComponent extends GameComponent
{
    RenderingEngine renderingEngine;

    @Override
    public void update(float delta)
    {
        if (renderingEngine != null) {
            Quaternion newRotation = getTransform().getLookAtRotation(renderingEngine.getMainCamera().getTransform().getTransformedPosition(), new Vector3f(0, 1, 0));

            //getTransform().setRotation(getTransform().getRotation().nlerp(newRotation, delta * 5.f, true));
            getTransform().setRotation(getTransform().getRotation().slerp(newRotation, delta * 5.f, true));
        }
    }

    @Override
    public void render(Shader shader, RenderingEngine renderingEngine)
    {
        this.renderingEngine = renderingEngine;
    }
}
