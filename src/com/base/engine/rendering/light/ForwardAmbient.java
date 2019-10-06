package com.base.engine.rendering.light;

import com.base.engine.core.math.*;
import com.base.engine.rendering.*;

public class ForwardAmbient extends Shader
{
    private static ForwardAmbient instance = new ForwardAmbient();

    public static ForwardAmbient getInstance()
    {
        if (instance == null)
            instance = new ForwardAmbient();
        return instance;
    }

    private ForwardAmbient()
    {
        super();

        addVertexShaderFromFile("forward-ambient.vert");
        addFragmentShaderFromFile("forward-ambient.frag");
        compileShader();

        addUniform("MVP");
        addUniform("ambientIntensity");
    }

    public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine)
    {
        Matrix4f worldMatrix = transform.getTransformation();
        Matrix4f projectedMatrix = renderingEngine.getMainCamera().getViewProjection().multiply(worldMatrix);

        material.getTexture("diffuse").bind(0);

        setUniformMat4("MVP", projectedMatrix);
        setUniformVec3("ambientIntensity", renderingEngine.getAmbientLight());
    }

}
