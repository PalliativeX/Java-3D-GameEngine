package com.base.engine.rendering;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;


final class PostprocessingShader extends Shader
{
    PostprocessingShader()
    {
        super();

        addVertexShaderFromFile("postprocessing.vert");
        addFragmentShaderFromFile("postprocessing.frag");
        compileShader();

        addUniform("screenTexture");
        addUniform("blurEnabled");
    }

    void updateUniforms(boolean isBlurEnabled, int colorbufferTexture)
    {
        setUniformi("blurEnabled", isBlurEnabled ? 1 : 0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, colorbufferTexture);
    }

}