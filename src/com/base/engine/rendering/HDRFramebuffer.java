package com.base.engine.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.*;


public class HDRFramebuffer
{
    private Framebuffer framebuffer;
    private HDRShader hdrShader;

    public HDRFramebuffer()
    {
        hdrShader = new HDRShader();

        framebuffer = new Framebuffer();
    }

    public void bind(boolean bind)
    {
        if (bind)
            framebuffer.bind(true);
        else
            glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getColorbufferTexture()
    {
        return framebuffer.getColorbufferTexture();
    }

    public HDRShader getHdrShader()
    {
        return hdrShader;
    }

    @Override
    protected void finalize() throws Throwable
    {
        framebuffer.finalize();
    }

    public final class HDRShader extends Shader
    {

        public HDRShader()
        {
            super();

            addVertexShaderFromFile("hdr.vert");
            addFragmentShaderFromFile("hdr.frag");
            compileShader();

            addUniform("exposure");
        }

        public void updateUniforms(float exposure)
        {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, framebuffer.getColorbufferTexture());

            setUniformf("exposure", exposure);
        }
    }

}
