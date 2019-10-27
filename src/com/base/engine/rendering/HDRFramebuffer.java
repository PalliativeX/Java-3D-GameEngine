package com.base.engine.rendering;

import com.base.engine.core.Util;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static com.base.engine.core.Util.createIntBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class HDRFramebuffer
{
    private int fbo;

    private int rbo;
    private int colorbufferTexture;
    private int colorbufferTexture2;
    private int quadVAO;

    private HDRShader hdrShader;

    public HDRFramebuffer()
    {
        hdrShader = new HDRShader();

        fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        colorbufferTexture = addFloatColorAttachment(0);
        colorbufferTexture2 = addFloatColorAttachment(1);
        addDepthAttachment();

        IntBuffer attachments = createIntBuffer(2);
        attachments.put(GL_COLOR_ATTACHMENT0);
        attachments.put(GL_COLOR_ATTACHMENT1);
        attachments.flip();
        glDrawBuffers(attachments);
        glGetError();

        setFBVertexArray();

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("Failed to create a Framebuffer!");
            System.exit(1);
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    // we can specify an arbitrary number of color attachments
    public int addFloatColorAttachment(int attachment)
    {
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        int colorbufferAttachment = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, colorbufferAttachment);

        // create an empty texture
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, Window.getWidth(), Window.getHeight(),0, GL_RGB, GL_FLOAT, (ByteBuffer)null);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        // attach a texture to a FB
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + attachment, GL_TEXTURE_2D, colorbufferAttachment, 0);

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        return colorbufferAttachment;
    }

    // prob memory leak
    public void addDepthAttachment()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        rbo = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rbo);

        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, Window.getWidth(), Window.getHeight());
        // attach a renderbuffer to FB
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rbo);

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private void setFBVertexArray()
    {
        float quadVertices[] = {
                // positions       texCoords
                -1.f, -1.f, 0.f,   0.f, 0.f,
                -1.f,  1.f, 0.f,   0.f, 1.f,
                 1.f,  1.f, 0.f,   1.f, 1.f,
                 1.f,  1.f, 0.f,   1.f, 1.f,
                 1.f, -1.f, 0.f,   1.f, 0.f,
                -1.f, -1.f, 0.f,   0.f, 0.f,
        };

        quadVAO = glGenVertexArrays();
        int quadVBO = glGenBuffers();

        glBindVertexArray(quadVAO);
        glBindBuffer(GL_ARRAY_BUFFER, quadVBO);

        glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(quadVertices), GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 4 * 5, 0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * 5, 12);

        glBindBuffer(GL_ARRAY_BUFFER,0);
        glBindVertexArray(0);

        glDeleteBuffers(quadVBO);
    }

    public void bind(boolean bind)
    {
        if (bind) {
            glBindFramebuffer(GL_FRAMEBUFFER, fbo);
        }
        else {
            glBindFramebuffer(GL_FRAMEBUFFER, 0);
        }
    }

    public int getColorbufferTexture()
    {
        return colorbufferTexture;
    }

    public int getColorbufferTexture2() {
        return colorbufferTexture2;
    }

    public int getQuadVAO()
    {
        return quadVAO;
    }

    public void bindQuadVAO(boolean bind)
    {
        if (bind)
            glBindVertexArray(quadVAO);
        else
            glBindVertexArray(0);
    }

    public HDRShader getHdrShader()
    {
        return hdrShader;
    }

//    @Override
//    protected void finalize() throws Throwable
//    {
//        glDeleteFramebuffers(fbo);
//        glDeleteVertexArrays(quadVAO);
//        glDeleteRenderbuffers(rbo);
//        glDeleteTextures(colorbufferTexture);
//
//    }

    public final class HDRShader extends Shader
    {

        public HDRShader()
        {
            super();

            addVertexShaderFromFile("hdr.vert");
            addFragmentShaderFromFile("hdr.frag");
            compileShader();

            addUniform("exposure");
        }

        public void updateUniforms(float exposure)
        {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, getColorbufferTexture());

            setUniformf("exposure", exposure);
        }
    }

}
