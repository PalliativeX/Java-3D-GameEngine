package com.base.engine.rendering;

import com.base.engine.core.Util;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.meshLoading.IndexedModel;
import com.base.engine.rendering.meshLoading.OBJModel;
import com.base.engine.rendering.resourceManagement.MeshResource;
import org.lwjgl.opengl.GL15;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Mesh
{
    // We store MeshResources as Weak references so they get deleted if no object uses them
    private static HashMap<String, WeakReference<MeshResource> > loadedModels = new HashMap<>();
    private MeshResource resource;


    public Mesh(String fileName)
    {
        MeshResource oldResource;
        try {
            oldResource = loadedModels.get(fileName).get();
        } catch (Exception e) {
            oldResource = null;
        }

        if (oldResource != null)
            resource = oldResource;
        else {
            loadMesh(fileName);
            loadedModels.put(fileName, new WeakReference<>(resource));
        }
    }

    public Mesh(Vertex[] vertices, int[] indices)
    {
        this(vertices, indices, false);
    }

    public Mesh(Vertex[] vertices, int[] indices, boolean calcNormals)
    {
        resource = new MeshResource(indices.length);
        addVertices(vertices, indices, calcNormals);
    }

    @Override
    protected void finalize() throws Throwable
    {

    }

    private void addVertices(Vertex[] vertices, int[] indices, boolean calcNormals)
    {
        if (calcNormals) {
            calcNormals(vertices, indices);
        }

        resource = new MeshResource(indices.length);

        glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
        GL15.glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL_STATIC_DRAW);
    }

    public void draw()
    {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20);


        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
        glDrawElements(GL_TRIANGLES, resource.getSize(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
    }

    private void calcNormals(Vertex[] vertices, int[] indices)
    {
        for (int i = 0; i < indices.length; i += 3) {
            int i0 = indices[i];
            int i1 = indices[i + 1];
            int i2 = indices[i + 2];

            Vector3f v1 = vertices[i1].getPos().subtract(vertices[i0].getPos());
            Vector3f v2 = vertices[i2].getPos().subtract(vertices[i0].getPos());

            Vector3f normal = v1.cross(v2).normalized();

            vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
            vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
            vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
        }

        for (int i = 0; i < vertices.length; i++)
            vertices[i].setNormal(vertices[i].getNormal().normalized());
    }

    private void loadMesh(String fileName)
    {
        String[] splitArray = fileName.split("\\.");
        String ext = splitArray[splitArray.length - 1];

        if (!ext.equals("obj")) {
            System.err.println("Error: File format not supported for mesh data: " + ext);
            new Exception().printStackTrace();
            System.exit(1);
        }

        OBJModel test = new OBJModel("./resources/models/" + fileName);
        IndexedModel model = test.toIndexedModel();
        model.calcNormals();

        ArrayList<Vertex> vertices = new ArrayList<>();

        for (int i = 0; i < model.getPositions().size(); i++) {
            vertices.add(new Vertex(model.getPositions().get(i),
                    model.getTexCoords().get(i),
                    model.getNormals().get(i)));
        }

        Vertex[] vertexData = new Vertex[vertices.size()];
        vertices.toArray(vertexData);

        Integer[] indexData = new Integer[model.getIndices().size()];
        model.getIndices().toArray(indexData);

        addVertices(vertexData, Util.toIntArray(indexData), false);
    }


}
