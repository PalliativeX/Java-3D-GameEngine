package com.base.engine.rendering;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

public class DepthmapFramebuffer
{
    public static final int SHADOW_WIDTH = 1024, SHADOW_HEIGHT = 1024;

    private int fbo;
    private int depthMap;

    public DepthmapFramebuffer()
    {
        fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        addDepthAttachment();

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("Failed to create a Framebuffer!");
            System.exit(1);
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void addDepthAttachment()
    {
        depthMap = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depthMap);

        // creating a depthMap
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, SHADOW_WIDTH, SHADOW_HEIGHT, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer)null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        // attaching a depthMap and stating that color attachment isn't needed
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMap, 0);
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);
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

    public int getDepthMap()
    {
        return depthMap;
    }

    public void bindTexture()
    {
        glBindTexture(GL_TEXTURE_2D, depthMap);
    }

    @Override
    protected void finalize() throws Throwable
    {
        glDeleteFramebuffers(fbo);
        glDeleteTextures(depthMap);
    }

}
