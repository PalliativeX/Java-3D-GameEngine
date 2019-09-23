package com.base.engine.core;

import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Camera;
import com.base.engine.rendering.Window;
import com.base.engine.rendering.shaders.BasicShader;
import com.base.engine.rendering.shaders.Shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RenderingEngine
{
    private Camera mainCamera;

    public RenderingEngine()
    {
        glClearColor(0.1f, 0.1f, 0.1f, 0.f);

        glFrontFace(GL_CW);
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);

        glEnable(GL_DEPTH_TEST);

        glEnable(GL_DEPTH_CLAMP);

        glEnable(GL_TEXTURE_2D);

        mainCamera = new Camera((float)Math.toRadians(70.f), (float)Window.getWidth()/(float)Window.getHeight(), 0.01f, 500.f);
    }

    public void input()
    {
        mainCamera.input();
    }

    public void render(GameObject object)
    {
        clearScreen();

        Shader shader  = BasicShader.getInstance();
        shader.setRenderingEngine(this);

        object.render(BasicShader.getInstance());
    }

    private static void clearScreen()
    {
        // @TODO: stencil buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private static void setTextures(boolean enabled)
    {
        if (enabled)
            glEnable(GL_TEXTURE_2D);
        else
            glDisable(GL_TEXTURE_2D);
    }

    private static void unbindTextures()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private static void setClearColor(Vector3f color)
    {
        glClearColor(color.getX(), color.getY(), color.getZ(), 1.f);
    }

    public static String getOpenGLVersion()
    {
        return glGetString(GL_VERSION);
    }

    public Camera getMainCamera()
    {
        return mainCamera;
    }

    public void setMainCamera(Camera mainCamera)
    {
        this.mainCamera = mainCamera;
    }
}
