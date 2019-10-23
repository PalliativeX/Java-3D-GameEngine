package com.base.engine.rendering;

import com.base.engine.components.*;
import com.base.engine.core.GameObject;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.light.*;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RenderingEngine
{
    private Camera mainCamera;
    private Vector3f ambientLight;

    private Framebuffer postprocessingFB;

    private HDRFramebuffer hdrFramebuffer;

    private ArrayList<BaseLight> lights;
    private BaseLight activeLight;

    private Skybox skybox;

    // different post-processing effects
    private boolean isBlurEnabled = false;

    public RenderingEngine()
    {
        lights = new ArrayList<>();

        glClearColor(0.1f, 0.1f, 0.1f, 0.f);

        // setting clockwise order for face culling
        glFrontFace(GL_CW);
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_DEPTH_CLAMP);

        ambientLight = new Vector3f(0.15f, 0.15f, 0.15f);

        postprocessingFB = new Framebuffer();

        hdrFramebuffer = new HDRFramebuffer();
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
        // first render to HDR framebuffer
        hdrFramebuffer.bind(true);

        // whole rendering stage
        {
            glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // displaying skybox
            if (skybox != null) {
                glDepthFunc(GL_LEQUAL);
                skybox.getSkyboxShader().bind();
                skybox.getSkyboxShader().updateUniforms(skybox, this);
                glBindVertexArray(skybox.getSkyboxVAO());
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

        // unbind HDR framebuffer and render to postprocessing FB
        hdrFramebuffer.bind(false);

        glDisable(GL_DEPTH_CLAMP);
        glDisable(GL_DEPTH_TEST);

        postprocessingFB.bind(true);
        glClear(GL_COLOR_BUFFER_BIT);

        hdrFramebuffer.getHdrShader().bind();
        hdrFramebuffer.getHdrShader().updateUniforms(5.f);
        hdrFramebuffer.bindQuadVAO(true);
        glDrawArrays(GL_TRIANGLES, 0, 6);

        // unbind postprocessing FB and render to default FB
        postprocessingFB.bind(false);

        glClear(GL_COLOR_BUFFER_BIT);

        postprocessingFB.getFramebufferShader().bind();
        postprocessingFB.getFramebufferShader().updateUniforms(isBlurEnabled);
        postprocessingFB.bindQuadVAO(true);
        glDrawArrays(GL_TRIANGLES, 0, 6);

        glEnable(GL_DEPTH_CLAMP);
        glEnable(GL_DEPTH_TEST);
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

    public void setSkybox(String[] faces)
    {
        this.skybox = new Skybox(faces);
        EnvironmentalMapper.getInstance();
        EnvironmentalMapper.setSkybox(skybox);
    }

    public Framebuffer getPostprocessingFB()
    {
        return postprocessingFB;
    }

    public boolean isBlurEnabled()
    {
        return isBlurEnabled;
    }

    public void setBlurEnabled(boolean blurEnabled)
    {
        isBlurEnabled = blurEnabled;
    }

}