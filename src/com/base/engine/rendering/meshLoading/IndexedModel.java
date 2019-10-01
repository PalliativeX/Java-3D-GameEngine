package com.base.engine.rendering.meshLoading;

import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;

import java.util.ArrayList;

public class IndexedModel
{
    private ArrayList<Vector3f> positions;
    private ArrayList<Vector2f> texCoords;
    private ArrayList<Vector3f> normals;
    private ArrayList<Integer>  indices;

    public IndexedModel()
    {
        this.positions = new ArrayList<>();
        this.texCoords = new ArrayList<>();
        this.normals   = new ArrayList<>();
        this.indices   = new ArrayList<>();
    }

    public ArrayList<Vector3f> getPositions() { return positions; }
    public ArrayList<Vector2f> getTexCoords() { return texCoords; }
    public ArrayList<Vector3f> getNormals()   { return normals; }
    public ArrayList<Integer>  getIndices()   { return indices; }


}
