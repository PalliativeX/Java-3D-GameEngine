package com.base.engine.rendering;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24;
import static org.lwjgl.opengl.GL30.*;

public class Framebuffer
{
    private int fbo;

    private int colorbufferTexture;
    private int rbo;
    
    public Framebuffer()
    {
        fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        addColorAttachment(0);
        addDepthAttachment();

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("Failed to create a Framebuffer!");
            System.exit(1);
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void addColorAttachment(int attachment)
    {
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        colorbufferTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, colorbufferTexture);

        // create an empty texture
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, Window.getWidth(), Window.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // attach a texture to a FB
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + attachment, GL_TEXTURE_2D, colorbufferTexture, 0);

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    // prob memory leak
    public void addDepthAttachment()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        rbo = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rbo);

        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, Window.getWidth(), Window.getHeight());
        // attach a renderbuffer to FB
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rbo);

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
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

    public int getColorbufferTexture()
    {
        return colorbufferTexture;
    }

    @Override
    protected void finalize() throws Throwable
    {
        glDeleteFramebuffers(fbo);
        glDeleteRenderbuffers(rbo);
        glDeleteTextures(colorbufferTexture);

    }
}
