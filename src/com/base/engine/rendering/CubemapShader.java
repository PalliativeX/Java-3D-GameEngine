package com.base.engine.rendering;

import com.base.engine.core.math.Matrix4f;
import com.sun.javafx.geom.Matrix3f;

public class CubemapShader extends Shader
{

    public CubemapShader()
    {
        super();


        addVertexShaderFromFile("cubemap.vert");
        addFragmentShaderFromFile("cubemap.frag");
        compileShader();

        addUniform("viewProjection");
    }

    public void updateUniforms(Cubemap cubemap, RenderingEngine renderingEngine)
    {
        Matrix4f projectedMatrix = renderingEngine.getMainCamera().getViewProjection().removeTranslation();

        cubemap.bind();

        setUniformMat4("viewProjection", projectedMatrix);
    }
}
