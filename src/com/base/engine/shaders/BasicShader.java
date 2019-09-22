package com.base.engine.shaders;

import com.base.engine.Material;
import com.base.engine.RenderUtil;
import com.base.engine.math.Matrix4f;

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
