package com.base.engine.rendering.resourceManagement;

import static org.lwjgl.opengl.GL15.*;

public class MeshResource
{
    private int vbo;
    private int ibo;
    private int size;


    public MeshResource(int size)
    {
        vbo = glGenBuffers();
        ibo = glGenBuffers();
        this.size = size;
    }

    @Override
    protected void finalize() throws Throwable
    {
        glDeleteBuffers(vbo);
        glDeleteBuffers(ibo);
    }

    public int getVbo()
    {
        return vbo;
    }

    public int getIbo()
    {
        return ibo;
    }

    public int getSize()
    {
        return size;
    }


}
