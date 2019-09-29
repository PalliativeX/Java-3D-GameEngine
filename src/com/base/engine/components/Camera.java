package com.base.engine.components;

import com.base.engine.core.Input;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Window;

public class Camera extends GameComponent
{
    public static final Vector3f yAxis = new Vector3f(0, 1, 0);

    private Matrix4f projection;

    public Camera(float fov, float aspectRatio, float zNear, float zFar)
    {
        this.projection = new Matrix4f().initPerspective(fov, aspectRatio, zNear, zFar);
    }

    public Matrix4f getViewProjection()
    {
        Matrix4f cameraRotation = getTransform().getTransformedRotation().conjugate().toRotationMatrix();
        Vector3f cameraPos = getTransform().getTransformedPosition().multiply(-1);

        Matrix4f cameraTranslation = new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());

        return projection.multiply(cameraRotation.multiply(cameraTranslation));
    }

    @Override
    public void addToRenderingEngine(RenderingEngine renderingEngine)
    {
        renderingEngine.addCamera(this);
    }


    private boolean mouseLocked = false;
    private Vector2f centerPosition = new Vector2f(Window.getWidth()/2, Window.getHeight()/2);

    public void input(float delta)
    {
        float sensitivity = 0.4f;
        float moveAmount = 10 * delta;

        if (Input.getKey(Input.KEY_ESCAPE)) {
            Input.setCursor(true);
            mouseLocked = false;
        }
        if (Input.getMouseDown(0)) {
            Input.setMousePosition(centerPosition);
            Input.setCursor(false);
            mouseLocked = true;
        }

        if (Input.getKey(Input.KEY_W))
            move(getTransform().getRotation().getForward(), moveAmount);
        if (Input.getKey(Input.KEY_S))
            move(getTransform().getRotation().getForward(), -moveAmount);
        if (Input.getKey(Input.KEY_A))
            move(getTransform().getRotation().getLeft(), moveAmount);
        if (Input.getKey(Input.KEY_D))
            move(getTransform().getRotation().getRight(), moveAmount);

        if (mouseLocked) {
            Vector2f deltaPos = Input.getMousePosition().subtract(centerPosition);

            boolean rotY = deltaPos.getX() != 0;
            boolean rotX = deltaPos.getY() != 0;

            if (rotY)
                getTransform().rotate(yAxis, (float)Math.toRadians(deltaPos.getX() * sensitivity));
            if (rotX)
                getTransform().rotate(getTransform().getRotation().getRight(), (float) Math.toRadians(-deltaPos.getY() * sensitivity));

            if (rotY || rotX)
                Input.setMousePosition(new Vector2f(Window.getWidth()/2, Window.getHeight()/2));
        }

        // rotating the camera with keys
        /*if (Input.getKey(Input.KEY_UP))
            rotateX(-rotationAmount);
        if (Input.getKey(Input.KEY_DOWN))
            rotateX(rotationAmount);
        if (Input.getKey(Input.KEY_LEFT))
            rotateY(-rotationAmount);
        if (Input.getKey(Input.KEY_RIGHT))
            rotateY(rotationAmount);*/
    }

    public void move(Vector3f dir, float amount)
    {
        getTransform().setPosition(getTransform().getPosition().add(dir.multiply(amount)));
    }

}
