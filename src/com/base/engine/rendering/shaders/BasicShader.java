package com.base.engine.rendering.shaders;

import com.base.engine.core.math.Transform;
import com.base.engine.rendering.Material;
import com.base.engine.core.math.Matrix4f;

public class BasicShader extends Shader
{
    private static final BasicShader instance = new BasicShader();

    public static BasicShader getInstance()
    {
        return instance;
    }

    private BasicShader()
    {
        super();

        addVertexShaderFromFile("basicVertex.vert");
        addFragmentShaderFromFile("basicFragment.frag");
        compileShader();

        addUniform("transform");
        addUniform("color");
    }

    public void updateUniforms(Transform transform, Material material)
    {
        Matrix4f worldMatrix = transform.getTransformation();
        Matrix4f projectedMatrix = getRenderingEngine().getMainCamera().getViewProjection().multiply(worldMatrix);

        material.getTexture().bind();

        setUniformMat4("transform", projectedMatrix);
        setUniformVec3("color", material.getColor());
    }


}
