package com.base.engine.rendering;

import com.base.engine.core.Util;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Framebuffer
{
    private int fbo;

    private int rbo;
    private int colorbufferTexture;
    private int quadVAO;
    private FramebufferShader framebufferShader;

    public Framebuffer()
    {
        framebufferShader = new FramebufferShader();

        fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        addColorAttachment(Window.getWidth(), Window.getHeight());
        addDepthAttachment(Window.getWidth(), Window.getHeight());

        setFBVertexArray();

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("Failed to create a Framebuffer!");
            System.exit(1);
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void addColorAttachment(int width, int height)
    {
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        colorbufferTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, colorbufferTexture);

        // create an empty texture
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // attach a texture to a FB
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorbufferTexture, 0);

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    // prob memory leak
    public void addDepthAttachment(int width, int height)
    {
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        rbo = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rbo);

        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, width, height);
        // attach a renderbuffer to FB
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rbo);

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private void setFBVertexArray()
    {
        float quadVertices[] = {
                  // positions    texCoords
                    -1.f, -1.f,   0.f, 0.f,
                    -1.f,  1.f,   0.f, 1.f,
                     1.f,  1.f,   1.f, 1.f,
                     1.f,  1.f,   1.f, 1.f,
                     1.f, -1.f,   1.f, 0.f,
                    -1.f, -1.f,   0.f, 0.f,
        };

        quadVAO = glGenVertexArrays();
        int quadVBO = glGenBuffers();

        glBindVertexArray(quadVAO);
        glBindBuffer(GL_ARRAY_BUFFER, quadVBO);

        glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(quadVertices), GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * 4, 0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * 4, 8);

        glBindBuffer(GL_ARRAY_BUFFER,0);
        glBindVertexArray(0);

        glDeleteBuffers(quadVBO);
    }

    public void bind(boolean bind)
    {
        if (bind) {
            glBindFramebuffer(GL_FRAMEBUFFER, fbo);
        }
        else {
            glBindFramebuffer(GL_FRAMEBUFFER, 0);
        }
    }

    public FramebufferShader getFramebufferShader()
    {
        return framebufferShader;
    }

    public int getColorbufferTexture()
    {
        return colorbufferTexture;
    }

    public int getQuadVAO()
    {
        return quadVAO;
    }

    @Override
    protected void finalize() throws Throwable
    {
        glDeleteFramebuffers(fbo);
        glDeleteVertexArrays(quadVAO);
        glDeleteRenderbuffers(rbo);
        glDeleteTextures(colorbufferTexture);

    }

    class FramebufferShader extends Shader
    {
        FramebufferShader()
        {
            super();

            addVertexShaderFromFile("finalShader.vert");
            addFragmentShaderFromFile("finalShader.frag");
            compileShader();

            addUniform("screenTexture");
            addUniform("blurEnabled");
        }

        void updateUniforms(boolean isBlurEnabled)
        {
            setUniformi("blurEnabled", isBlurEnabled ? 1 : 0);

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, getColorbufferTexture());
        }

    }

}
