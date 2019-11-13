package com.base.engine.rendering;

import com.base.engine.core.math.Matrix4f;

public class DepthShader extends Shader
{
    public DepthShader()
    {
        super();

        addVertexShaderFromFile("simpleDepthShader.vert");
        addFragmentShaderFromFile("simpleDepthShader.frag");
        compileShader();

        addUniform("model");
        addUniform("lightSpaceMatrix");
    }

    public void updateUniforms(Matrix4f lightSpaceMatrix, Matrix4f model)
    {
        setUniformMat4("model", model);
        setUniformMat4("lightSpaceMatrix", lightSpaceMatrix);
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }

}
