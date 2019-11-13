package com.base.engine.rendering;

import com.base.engine.components.*;
import com.base.engine.core.GameObject;
import com.base.engine.core.Util;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.light.*;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RenderingEngine
{
    private Camera mainCamera;

    // trial
    private Camera alternativeCamera;
    private GameObject alternativeCameraObject;

    private Vector3f ambientLight;

    private Framebuffer postprocessingFB;
    private PostprocessingShader postprocessingShader;

    private HDRFramebuffer hdrFramebuffer;

    private ArrayList<BaseLight> lights;
    private BaseLight activeLight;

    private Skybox skybox;

    private DepthmapFramebuffer depthmapFramebuffer;
    private DepthShader depthShader = new DepthShader();
    private Matrix4f lightProjection;
    private Matrix4f lightSpaceMatrix;

    TestShader testShader = new TestShader();

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

        // VAO for rendering the whole screen
        setQuadVAO();

        ambientLight = new Vector3f(0.15f, 0.15f, 0.15f);

        postprocessingFB = new Framebuffer();
        postprocessingShader = new PostprocessingShader();

        hdrFramebuffer = new HDRFramebuffer();

        depthmapFramebuffer = new DepthmapFramebuffer();
        depthShader = new DepthShader();
        lightProjection = new Matrix4f().initOrthographic(-10.f, 10.f, -10.f, 10.f, 1.f, 10.f);

        alternativeCamera = new Camera(45, 16/9f, 0.5f, 30f);
        alternativeCameraObject = new GameObject().addComponent(alternativeCamera);
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

                if (activeLight.areShadowsApplied()) {
                    glViewport(0, 0, DepthmapFramebuffer.SHADOW_WIDTH, DepthmapFramebuffer.SHADOW_HEIGHT);
                    depthmapFramebuffer.bind(true);
                    glClear(GL_DEPTH_BUFFER_BIT);
                    // configure shader
                    depthShader.bind();

                    // alternative camera
                    alternativeCamera.getTransform().setPosition(activeLight.getTransform().getTransformedPosition());
                    alternativeCamera.getTransform().setRotation(activeLight.getTransform().getTransformedRotation());
                    // set projection
                    //alternativeCamera.getTransform().(lightProjection);

                    Matrix4f lightSpaceMatrix = alternativeCamera.getViewProjection();
                    this.lightSpaceMatrix = lightSpaceMatrix;

                    depthShader.updateUniforms(activeLight.getTransform().getTransformation(), lightSpaceMatrix);
                    // render
                    object.renderAll(depthShader, this);

                    depthmapFramebuffer.bind(false);
                    testShader.bind();
                    glBindTexture(GL_TEXTURE_2D, depthmapFramebuffer.getDepthMap());
                    renderQuad();


                    // render as normal with shadow map
                    depthmapFramebuffer.bind(false);
                    hdrFramebuffer.bind(true);
                    glViewport(0, 0, Window.getWidth(), Window.getHeight());
               }
                object.renderAll(activeLight.getShader(), this);
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
        hdrFramebuffer.getHdrShader().updateUniforms(1.f);
        renderQuad();

        // unbind postprocessing FB and render to default FB
        postprocessingFB.bind(false);

        postprocessingShader.bind();
        postprocessingShader.updateUniforms(isBlurEnabled, postprocessingFB.getColorbufferTexture());
        renderQuad();

        glEnable(GL_DEPTH_CLAMP);
        glEnable(GL_DEPTH_TEST);
    }


    private int quadVAO;
    private void setQuadVAO()
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

    // rendering whole screen texture as a quad
    private void renderQuad()
    {
        glBindVertexArray(quadVAO);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);
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

    // @TODO: change, now is a crutch
    public int getShadowMap()
    {
        return depthmapFramebuffer.getDepthMap();
    }
    public Matrix4f getLightSpaceMatrix()
    {
        return lightSpaceMatrix;
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