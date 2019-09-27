package com.base.engine.core;

import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.*;
import com.base.engine.rendering.light.*;
import com.base.engine.rendering.shaders.Shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RenderingEngine
{
    private Camera mainCamera;
    private Vector3f ambientLight;
    private DirectionalLight directionalLight;
    private DirectionalLight directionalLight2;
    private PointLight pointLight;
    private SpotLight spotLight;

    private PointLight pointLights[];

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
        directionalLight  = new DirectionalLight(new BaseLight(new Vector3f(0, 0, 1), 0.4f), new Vector3f(1, 1, 1));
        directionalLight2 = new DirectionalLight(new BaseLight(new Vector3f(1, 0, 0), 0.4f), new Vector3f(-1, 1, -1));

        // setting point lights
        int lightFieldWidth = 5;
        int lightFieldDepth = 5;

        float lightFieldStartX = 0;
        float lightFieldStartY = 0;
        float lightFieldStepX = 7;
        float lightFieldStepY = 7;

        pointLights = new PointLight[lightFieldWidth * lightFieldDepth];

        for (int i = 0; i < lightFieldWidth; i++) {
            for (int j = 0; j < lightFieldDepth; j++) {
                pointLights[i * lightFieldDepth + j] = new PointLight(new BaseLight(new Vector3f(0, 1, 0), 0.4f),
                        new Attenuation(0, 0, 1),
                        new Vector3f(lightFieldStartX + lightFieldStepX * i, 0, lightFieldStartY + lightFieldStepY * j), 100);
            }
        }

        pointLight = pointLights[0];

        // creating a spotlight
        spotLight = new SpotLight(new PointLight(new BaseLight(new Vector3f(1, 1, 1), 0.9f),
                new Attenuation(0, 0, 0.1f),
                new Vector3f(5, 0, 5), 100), new Vector3f(1, 0, 0), 0.7f);
    }

    public Vector3f getAmbientLight()
    {
        return ambientLight;
    }

    public DirectionalLight getDirectionalLight()
    {
        return directionalLight;
    }

    public PointLight getPointLight()
    {
        return pointLight;
    }

    public SpotLight getSpotLight()
    {
        return spotLight;
    }

    public void input(float delta)
    {
        mainCamera.input(delta);
    }

    public void render(GameObject object)
    {

        clearScreen();

        // creating shaders for forward lighting
        Shader forwardAmbient  = ForwardAmbient.getInstance();
        Shader forwardDirectional = ForwardDirectional.getInstance();
        Shader forwardPoint = ForwardPoint.getInstance();
        Shader forwardSpot = ForwardSpot.getInstance();
        forwardAmbient.setRenderingEngine(this);
        forwardDirectional.setRenderingEngine(this);
        forwardPoint.setRenderingEngine(this);
        forwardSpot.setRenderingEngine(this);

        object.render(forwardAmbient);

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glDepthMask(false);
        glDepthFunc(GL_EQUAL);

        object.render(forwardDirectional);

        DirectionalLight temp = directionalLight;
        directionalLight = directionalLight2;
        directionalLight2 = temp;

        object.render(forwardDirectional);

        temp = directionalLight;
        directionalLight = directionalLight2;
        directionalLight2 = temp;

        // rendering all point lights
        for (int i = 0; i < pointLights.length; i++) {
           pointLight = pointLights[i];
           object.render(forwardPoint);
        }

        object.render(forwardSpot);

        glDepthFunc(GL_LESS);
        glDepthMask(true);
        glDisable(GL_BLEND);
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