package com.base.engine.rendering;

import com.base.engine.core.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Cubemap
{
    private static final int FACES_NUM = 6;

    private int id;
    private int skyboxVAO;
    private CubemapShader cubemapShader;

    public Cubemap(String[] faces, CubemapShader cubemapShader)
    {
        if (faces.length != FACES_NUM) {
            System.out.println("Wrong number of faces supplied, must be 6!");
            System.exit(1);
        }

        id = loadCubemap(faces);

        this.cubemapShader = cubemapShader;

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

        this.skyboxVAO = skyboxVAO;
    }

    public void unbind()
    {
        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
    }

    public void bind()
    {
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);
    }

    public int getId()
    {
        return id;
    }

    public int getSkyboxVAO()
    {
        return skyboxVAO;
    }

    public CubemapShader getCubemapShader()
    {
        return cubemapShader;
    }

    private int imageWidth, imageHeight;
    int loadCubemap(String[] faces)
    {
        int cubemapID = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, cubemapID);

        int width, height, nrChannels;
        for (int i = 0; i < FACES_NUM; i++) {
            ByteBuffer buffer = loadImage(faces[i]);
            if (buffer != null) {
                glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGB8, imageWidth, imageHeight, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);
            }
            else {
                System.out.println("Cubemap texture failed to load at path: ");
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

    ByteBuffer loadImage(String fileName)
    {
        try {
            BufferedImage image = ImageIO.read(new File("./resources/textures/" + fileName));
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();

            int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

            ByteBuffer buffer = Util.createByteBuffer(image.getHeight() * image.getWidth() * 3);

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int pixel = pixels[y * image.getWidth() + x];

                    buffer.put((byte) ((pixel >> 16) & 0xFF));
                    buffer.put((byte) ((pixel >> 8) & 0xFF));
                    buffer.put((byte) ((pixel) & 0xFF));
                }
            }
            buffer.flip();
            return buffer;
        }
        catch (IOException e) {
            System.out.println("Cubemap texture failed to load!");
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }



}
