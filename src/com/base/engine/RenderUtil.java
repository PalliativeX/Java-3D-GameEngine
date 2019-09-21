package com.base.engine;

import com.base.engine.math.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;


public class RenderUtil
{
    public static void clearScreen()
    {
        // @TODO: stencil buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void setTextures(boolean enabled)
    {
        if (enabled)
            glEnable(GL_TEXTURE_2D);
        else
            glDisable(GL_TEXTURE_2D);
    }

    public static void unbindTextures()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public static void setClearColor(Vector3f color)
    {
        glClearColor(color.getX(), color.getY(), color.getZ(), 1.f);
    }

    public static void initGraphics()
    {
        glClearColor(0.3f, 0.2f, 0.6f, 0.f);

        glFrontFace(GL_CW);
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);

        glEnable(GL_DEPTH_TEST);

        // @TODO: depth clamp

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_FRAMEBUFFER_SRGB); // gamma correction
    }

    public static String getOpenGLVersion()
    {
        return glGetString(GL_VERSION);
    }

}
