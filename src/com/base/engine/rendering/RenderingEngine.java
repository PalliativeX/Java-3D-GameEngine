package com.base.engine.rendering;

import com.base.engine.components.*;
import com.base.engine.core.GameObject;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.light.*;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RenderingEngine
{
    private Camera mainCamera;
    private Vector3f ambientLight;

    // More permanent Structure
    private ArrayList<BaseLight> lights;
    private BaseLight activeLight;

    public RenderingEngine()
    {
        lights = new ArrayList<>();

        glClearColor(0.1f, 0.1f, 0.1f, 0.f);

        glFrontFace(GL_CW);
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_DEPTH_CLAMP);
        glEnable(GL_TEXTURE_2D);


        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
    }

    public Vector3f getAmbientLight()
    {
        return ambientLight;
    }

    public void render(GameObject object)
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Shader forwardAmbient = ForwardAmbient.getInstance();
        object.renderAll(forwardAmbient, this);

        // blending all colors from multiple shader passes
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glDepthMask(false);
        glDepthFunc(GL_EQUAL);

        for (BaseLight light : lights) {
            activeLight = light;
            object.renderAll(light.getShader(), this);
        }

        glDepthFunc(GL_LESS);
        glDepthMask(true);
        glDisable(GL_BLEND);
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

    public void addLight(BaseLight light)
    {
        lights.add(light);
    }

    public void addCamera(Camera camera)
    {
        mainCamera = camera;
    }

    public BaseLight getActiveLight()
    {
        return activeLight;
    }

}