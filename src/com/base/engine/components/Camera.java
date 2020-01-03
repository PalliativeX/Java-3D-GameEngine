package com.base.engine.components;

import com.base.engine.core.*;
import com.base.engine.core.math.*;

public class Camera extends GameComponent
{
    private Matrix4f projection;

    public Camera(float fov, float aspectRatio, float zNear, float zFar)
    {
        this.projection = new Matrix4f().initPerspective(fov, aspectRatio, zNear, zFar);
    }

    public Camera(float left, float right, float bottom, float top, float near, float far)
    {
        this.projection = new Matrix4f().initOrthographic(left, right, bottom, top, near, far);
    }

    public Matrix4f getViewProjection()
    {
        Matrix4f cameraRotation = getTransform().getTransformedRotation().conjugate().toRotationMatrix();
        Vector3f cameraPos = getTransform().getTransformedPosition().multiply(-1);

        Matrix4f cameraTranslation = new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());

        return projection.multiply(cameraRotation.multiply(cameraTranslation));
    }

    public void setPerspectiveProjection(float fov, float aspectRatio, float zNear, float zFar)
    {
        this.projection = new Matrix4f().initPerspective(fov, aspectRatio, zNear, zFar);
    }

    public void setProjection(Matrix4f projection)
    {
        this.projection = projection;
    }

    public void setOrthgraphicProjection(float left, float right, float bottom, float top, float near, float far)
    {
        this.projection = new Matrix4f().initOrthographic(left, right, bottom, top, near, far);
    }

    @Override
    public void addToEngine(CoreEngine engine)
    {
        engine.getRenderingEngine().addCamera(this);
    }

}
