package com.base.engine.core;

import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.*;
import com.base.engine.rendering.shaders.*;
import com.base.engine.rendering.light.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RenderingEngine
{
    private Camera mainCamera;
    private Vector3f ambientLight;


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

        ambientLight = new Vector3f(0.2f, 0.2f, 0.2f);
    }

    public Vector3f getAmbientLight()
    {
        return ambientLight;
    }

    public void input(float delta)
    {
        mainCamera.input(delta);
    }

    public void render(GameObject object)
    {
        clearScreen();

        Shader forwardAmbient  = ForwardAmbient.getInstance();
        forwardAmbient.setRenderingEngine(this);

        object.render(forwardAmbient);

//        Shader shader  = BasicShader.getInstance();
//        shader.setRenderingEngine(this);
//
//        object.render(BasicShader.getInstance());
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
