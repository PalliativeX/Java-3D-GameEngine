package com.base.engine.core.math;

public class Transform
{
    private Vector3f position;
    private Quaternion rotation;
    private Vector3f scale;

    public Transform()
    {
        position = new Vector3f(0,0,0);
        rotation = new Quaternion(0,0,0, 1);
        scale = new Vector3f(1,1,1);
    }

    public Matrix4f getTransformation()
    {
        Matrix4f translationMatrix = new Matrix4f().initTranslation(position.getX(), position.getY(), position.getZ());
        Matrix4f rotationMatrix = rotation.toRotationMatrix();
        Matrix4f scaleMatrix = new Matrix4f().initScale(scale.getX(), scale.getY(), scale.getZ());

        return translationMatrix.multiply(rotationMatrix.multiply(scaleMatrix));
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public Quaternion getRotation()
    {
        return rotation;
    }

    public void setRotation(Quaternion rotation)
    {
        this.rotation = rotation;
    }

    public Vector3f getScale()
    {
        return scale;
    }

    public void setScale(Vector3f scale)
    {
        this.scale = scale;
    }

}