package com.base.engine.rendering;

import com.base.engine.components.*;
import com.base.engine.core.GameObject;
import com.base.engine.core.Util;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.light.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glBlendEquationSeparate;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RenderingEngine
{
    private Camera mainCamera;
    private Vector3f ambientLight;

    // More permanent Structure
    private ArrayList<BaseLight> lights;
    private BaseLight activeLight;

    private CubemapShader cubemapShader;
    private int skyboxVAO;

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

        glEnable(GL_MULTISAMPLE);

        // set to 0.3f by default
        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
    }

    public Vector3f getAmbientLight()
    {
        return ambientLight;
    }

    public void setAmbientLight(Vector3f ambient)
    {
        ambientLight = ambient;
    }

    public void render(GameObject object)
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // displaying cubemap
        if (cubemapShader != null) {
            glDepthFunc(GL_LEQUAL);
            cubemapShader.bind();
            cubemapShader.updateUniforms(this);
            glBindVertexArray(skyboxVAO);
            glDrawArrays(GL_TRIANGLES, 0, 36);
            glBindVertexArray(0);
            glDepthFunc(GL_LESS);
        }

        // blending all colors from multiple shader passes
        glEnable(GL_BLEND);
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);

        Shader forwardAmbient = ForwardAmbient.getInstance();
        object.renderAll(forwardAmbient, this);

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

    public void setCubemap(String[] faces)
    {
        cubemapShader = new CubemapShader(new Cubemap(faces));

        float skyboxVertices[] = {
                // positions
                -1.0f,  1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f,
                 1.0f, -1.0f, -1.0f,
                 1.0f, -1.0f, -1.0f,
                 1.0f,  1.0f, -1.0f,
                -1.0f,  1.0f, -1.0f,

                -1.0f, -1.0f,  1.0f,
                -1.0f, -1.0f, -1.0f,
                -1.0f,  1.0f, -1.0f,
                -1.0f,  1.0f, -1.0f,
                -1.0f,  1.0f,  1.0f,
                -1.0f, -1.0f,  1.0f,

                 1.0f, -1.0f, -1.0f,
                 1.0f, -1.0f,  1.0f,
                 1.0f,  1.0f,  1.0f,
                 1.0f,  1.0f,  1.0f,
                 1.0f,  1.0f, -1.0f,
                 1.0f, -1.0f, -1.0f,

                -1.0f, -1.0f,  1.0f,
                -1.0f,  1.0f,  1.0f,
                 1.0f,  1.0f,  1.0f,
                 1.0f,  1.0f,  1.0f,
                 1.0f, -1.0f,  1.0f,
                -1.0f, -1.0f,  1.0f,

                -1.0f,  1.0f, -1.0f,
                 1.0f,  1.0f, -1.0f,
                 1.0f,  1.0f,  1.0f,
                 1.0f,  1.0f,  1.0f,
                -1.0f,  1.0f,  1.0f,
                -1.0f,  1.0f, -1.0f,

                -1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f,  1.0f,
                 1.0f, -1.0f, -1.0f,
                 1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f,  1.0f,
                 1.0f, -1.0f,  1.0f
        };
        FloatBuffer buffer = Util.createFlippedBuffer(skyboxVertices);

        int skyboxVAO, skyboxVBO;
        skyboxVAO = glGenVertexArrays();
        skyboxVBO = glGenBuffers();

        glBindVertexArray(skyboxVAO);
        glBindBuffer(GL_ARRAY_BUFFER, skyboxVBO);

        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        this.skyboxVAO = skyboxVAO;
    }

}