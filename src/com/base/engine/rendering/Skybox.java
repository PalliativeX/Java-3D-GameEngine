package com.base.engine.rendering;

import com.base.engine.core.Util;
import com.base.engine.core.math.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Skybox
{
    private static final int FACES_NUM = 6;

    private int skyboxID;
    private int skyboxVAO;
    private SkyboxShader skyboxShader;

    public Skybox(String[] faces)
    {
        skyboxShader = new SkyboxShader();

        if (faces.length != FACES_NUM) {
            System.out.println("Wrong number of faces supplied, must be 6!");
            System.exit(1);
        }

        skyboxID = loadCubemap(faces);

        skyboxVAO = createSkyboxVAO();
    }

    class SkyboxShader extends Shader
    {
        SkyboxShader()
        {
            super();

            addVertexShaderFromFile("skybox.vert");
            addFragmentShaderFromFile("skybox.frag");
            compileShader();

            addUniform("viewProjection");
        }

        void updateUniforms(Skybox skybox, RenderingEngine renderingEngine)
        {
            Matrix4f projectedMatrix = renderingEngine.getMainCamera().getViewProjection().removeTranslation();

            skybox.bind();

            setUniformMat4("viewProjection", projectedMatrix);
        }
    }


    private int createSkyboxVAO()
    {
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
        int skyboxVAO, skyboxVBO;
        skyboxVAO = glGenVertexArrays();
        skyboxVBO = glGenBuffers();

        glBindVertexArray(skyboxVAO);
        glBindBuffer(GL_ARRAY_BUFFER, skyboxVBO);

        glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(skyboxVertices), GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        return skyboxVAO;
    }

    public void unbind()
    {
        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
    }

    public void bind()
    {
        glBindTexture(GL_TEXTURE_CUBE_MAP, skyboxID);
    }

    public void bind(boolean doBind)
    {
        if (doBind)
            glBindTexture(GL_TEXTURE_CUBE_MAP, skyboxID);
        else
            glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
    }

    public int getSkyboxID()
    {
        return skyboxID;
    }

    public int getSkyboxVAO()
    {
        return skyboxVAO;
    }

    public SkyboxShader getSkyboxShader()
    {
        return skyboxShader;
    }

    private int loadCubemap(String[] faces)
    {
        int cubemapID = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, cubemapID);

        for (int i = 0; i < FACES_NUM; i++) {

            Util.ImageData imageData = Util.loadImage(faces[i]);
            if (imageData.pixelBuffer != null) {
                glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGB8, imageData.width, imageData.height, 0, GL_RGB, GL_UNSIGNED_BYTE, imageData.pixelBuffer);
            }
            else {
                System.out.println("Skybox texture failed to load at path: ");
            }
        }
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);

        return cubemapID;
    }

    @Override
    protected void finalize() throws Throwable
    {
        glDeleteTextures(skyboxID);
        glDeleteVertexArrays(skyboxVAO);
    }
}
