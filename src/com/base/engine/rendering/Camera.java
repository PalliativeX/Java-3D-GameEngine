package com.base.engine.rendering;

import com.base.engine.core.Input;
import com.base.engine.core.Time;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;

public class Camera
{
    public static final Vector3f yAxis = new Vector3f(0, 1, 0);

    private Vector3f pos;
    private Vector3f forward;
    private Vector3f up;
    private Matrix4f projection;

    public Camera(float fov, float aspectRatio, float zNear, float zFar)
    {
        this.pos     = new Vector3f(0, 0, 0);
        this.forward = new Vector3f(0, 0, 1).normalized();
        this.up      = new Vector3f(0, 1, 0).normalized();
        this.projection = new Matrix4f().initPerspective(fov, aspectRatio, zNear, zFar);
    }

    public Matrix4f getViewProjection()
    {
        Matrix4f cameraRotation = new Matrix4f().initRotation(forward, up);
        Matrix4f cameraTranslation = new Matrix4f().initTranslation(-pos.getX(), -pos.getY(), -pos.getZ());

        return projection.multiply(cameraRotation.multiply(cameraTranslation));
    }


    private boolean mouseLocked = false;
    private Vector2f centerPosition = new Vector2f(Window.getWidth()/2, Window.getHeight()/2);

    public void input(float delta)
    {
        float sensitivity = .4f;
        float moveAmount = 10 * delta;
        //float rotationAmount = (float)(100 * Time.getDelta());

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
            move(getForward(), moveAmount);
        if (Input.getKey(Input.KEY_S))
            move(getForward(), -moveAmount);
        if (Input.getKey(Input.KEY_A))
            move(getLeft(), moveAmount);
        if (Input.getKey(Input.KEY_D))
            move(getRight(), moveAmount);

        if (mouseLocked) {
            Vector2f deltaPos = Input.getMousePosition().subtract(centerPosition);

            boolean rotY = deltaPos.getX() != 0;
            boolean rotX = deltaPos.getY() != 0;

            if (rotY)
                rotateY(deltaPos.getX() * sensitivity);
            if (rotX)
                rotateX(-deltaPos.getY() * sensitivity);

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
        pos = pos.add(dir.multiply(amount));
    }

    public void rotateY(float angle)
    {
        Vector3f hAxis = yAxis.cross(forward).normalized();

        forward = forward.rotate(angle, yAxis).normalized();

        up = forward.cross(hAxis).normalized();
    }

    public void rotateX(float angle)
    {
        Vector3f hAxis = yAxis.cross(forward).normalized();

        forward = forward.rotate(angle, hAxis).normalized();

        up = forward.cross(hAxis).normalized();
    }

    public Vector3f getLeft()
    {
        return forward.cross(up).normalized();
    }

    public Vector3f getRight()
    {
        return up.cross(forward).normalized();
    }

    public Vector3f getPos()
    {
        return pos;
    }

    public void setPos(Vector3f pos)
    {
        this.pos = pos;
    }

    public Vector3f getForward()
    {
        return forward;
    }

    public void setForward(Vector3f forward)
    {
        this.forward = forward;
    }

    public Vector3f getUp()
    {
        return up;
    }

    public void setUp(Vector3f up)
    {
        this.up = up;
    }



}
