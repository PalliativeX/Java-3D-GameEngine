package com.base.engine.rendering.resourceManagement;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

public class TextureResource
{
    private int id;

    public TextureResource(int id)
    {
        this.id = id;
    }

    @Override
    protected void finalize() throws Throwable
    {
        glDeleteBuffers(id);
    }

    public int getId() { return id; }
}
