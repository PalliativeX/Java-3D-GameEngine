package com.base.engine;

import com.base.engine.math.Matrix4f;

public class BasicShader extends Shader
{
    public BasicShader()
    {
        super();

        addVertexShader(ResourceLoader.loadShader("basicVertex.vert"));
        addFragmentShader(ResourceLoader.loadShader("basicFragment.frag"));
        compileShader();

        addUniform("transform");
        addUniform("color");
    }

    public void updateUniforms(Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material)
    {
        if (material.getTexture() != null)
            material.getTexture().bind();
        else
            RenderUtil.unbindTextures();

        setUniformMat4("transform", projectedMatrix);
        setUniformVec3("color", material.getColor());
    }


}
