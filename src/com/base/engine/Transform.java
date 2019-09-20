package com.base.engine;

import com.base.engine.math.Matrix4f;
import com.base.engine.math.Vector3f;

public class Transform
{
    private static class Projection
    {
        // clip space on Z plane
        private static float zNear;
        private static float zFar;

        private static float width;
        private static float height;
        private static float fov;
    }

    private Vector3f translation;
    private Vector3f rotation;
    private Vector3f scale;

    public Transform()
    {
        translation = new Vector3f(0.f, 0.f, 0.f);
        rotation    = new Vector3f(0.f, 0.f, 0.f);
        scale = new Vector3f(1.f, 1.f, 1.f);
    }

    public static void setProjection(float fov, float width, float height, float zNear, float zFar)
    {
        Transform.Projection.width = width;
        Transform.Projection.height = height;
        Transform.Projection.fov = fov;
        Transform.Projection.zNear = zNear;
        Transform.Projection.zFar = zFar;

    }

    public Matrix4f getTransformation()
    {
        Matrix4f translationMatrix = new Matrix4f().initTranslation(translation.getX(), translation.getY(), translation.getZ());
        Matrix4f rotationMatrix = new Matrix4f().initRotation(rotation.getX(), rotation.getY(), rotation.getZ());
        Matrix4f scaleMatrix = new Matrix4f().initScale(scale.getX(), scale.getY(), scale.getZ());

        return translationMatrix.multiply(rotationMatrix.multiply(scaleMatrix));
    }

    public Matrix4f getProjectedTransformation()
    {
        Matrix4f transformationMatrix = getTransformation();
        Matrix4f projectionMatrix = new Matrix4f().initProjection(Projection.fov, Projection.width, Projection.height, Projection.zNear, Projection.zFar);


        return projectionMatrix.multiply(transformationMatrix);
    }

    public Vector3f getTranslation() {
        return translation;
    }

    public void setTranslation(Vector3f translation) {
        this.translation = translation;
    }

    public void setTranslation(float x, float y, float z) {
        this.translation = new Vector3f(x, y, z);
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation = new Vector3f(x, y, z);
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public void setScale(float x, float y, float z) {
        this.scale = new Vector3f(x, y, z);
    }
}
