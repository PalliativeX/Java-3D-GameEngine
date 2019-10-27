package com.base.engine.rendering;


import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.*;

public class GaussianBlur
{
    private int fbo1, fbo2;
    private int colorAttachment1, colorAttachment2;

    private BlurShader blurShader;

    public GaussianBlur()
    {
        blurShader = new BlurShader();

        fbo1 = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbo1);
        glGetError();

        colorAttachment1 = addColorAttachment(fbo1);
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("Failed to create a Framebuffer!");
            System.exit(1);
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        fbo2 = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbo2);
        glGetError();

        colorAttachment2 = addColorAttachment(fbo2);
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("Failed to create a Framebuffer!");
            System.exit(1);
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private int addColorAttachment(int framebuffer)
    {
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);

        int colorAttachment = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, colorAttachment);

        // create an empty texture
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, Window.getWidth(), Window.getHeight(),0, GL_RGB, GL_FLOAT, (ByteBuffer)null);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        // attach a texture to a FB
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorAttachment, 0);

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        return colorAttachment;
    }

    public BlurShader getBlurShader() {
        return blurShader;
    }

    public int getFbo1() {
        return fbo1;
    }

    public int getFbo2() {
        return fbo2;
    }

    public int getColorAttachment1() {
        return colorAttachment1;
    }

    public int getColorAttachment2() {
        return colorAttachment2;
    }

    public int getFBO(boolean fbo)
    {
        return fbo ? getFbo2() : getFbo1();
    }

    public int getColorAttachment(boolean fbo)
    {
        return fbo ? getColorAttachment2() : getColorAttachment1();
    }

    public final class BlurShader extends Shader
    {

        public BlurShader()
        {
            super();

            addVertexShaderFromFile("blur.vert");
            addFragmentShaderFromFile("blur.frag");
            compileShader();

            addUniform("horizontal");
        }

        public void updateUniforms(boolean isHorizontal)
        {
            setUniformi("horizontal", isHorizontal);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        glDeleteFramebuffers(fbo1);
        glDeleteFramebuffers(fbo2);
        glDeleteTextures(colorAttachment1);
        glDeleteTextures(colorAttachment2);
    }


}
