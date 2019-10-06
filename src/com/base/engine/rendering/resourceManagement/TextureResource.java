package com.base.engine.rendering.resourceManagement;

import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL15.glGenBuffers;

public class TextureResource
{
    private int id;

    public TextureResource()
    {
        this.id = glGenBuffers();
    }

    @Override
    protected void finalize() throws Throwable
    {
        glDeleteTextures(id);
    }

    public int getId() { return id; }
}
