package com.base.engine.rendering.light;

import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Transform;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.shaders.Shader;

public class ForwardAmbient extends Shader
{
    private static final ForwardAmbient instance = new ForwardAmbient();

    public static ForwardAmbient getInstance()
    {
        return instance;
    }

    private ForwardAmbient()
    {
        super();

        addVertexShaderFromFile("forward-ambient.vert");
        addFragmentShaderFromFile("forward-ambient.frag");
        compileShader();
    }

    public void updateUniforms(Transform transform, Material material)
    {
        Matrix4f worldMatrix = transform.getTransformation();
        Matrix4f projectedMatrix = getRenderingEngine().getMainCamera().getViewProjection().multiply(worldMatrix);

        material.getTexture().bind();

        setUniformMat4("MVP", projectedMatrix);
        setUniformVec3("ambientIntensity", getRenderingEngine().getAmbientLight());
    }
}
