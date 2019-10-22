package com.base.engine.components;

import com.base.engine.rendering.EnvironmentalMapper;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

public class Mirror extends GameComponent
{
    private Mesh mesh;

    public Mirror(Mesh mesh)
    {
        this.mesh = mesh;
    }

    @Override
    public void render(Shader shader, RenderingEngine renderingEngine)
    {
        EnvironmentalMapper.getInstance().bind();
        EnvironmentalMapper.getInstance().updateUniforms(getTransform(), null, renderingEngine);
        mesh.draw();
    }
}
