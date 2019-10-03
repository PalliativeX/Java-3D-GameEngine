package com.base.engine.rendering.meshLoading;

public class OBJIndex
{
    public int vertexIndex;
    public int texCoordIndex;
    public int normalIndex;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OBJIndex objIndex = (OBJIndex) o;

        if (vertexIndex != objIndex.vertexIndex) return false;
        if (texCoordIndex != objIndex.texCoordIndex) return false;
        return normalIndex == objIndex.normalIndex;
    }

    @Override
    public int hashCode() {
        int result = vertexIndex;
        result = 31 * result + texCoordIndex;
        result = 31 * result + normalIndex;
        return result;
    }

}
