package com.base.engine.components;

import com.base.engine.core.Input;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Window;

public class FreeLook extends GameComponent
{
    private static final Vector3f yAxis = new Vector3f(0, 1, 0);

    private boolean mouseLocked;
    private float sensitivity;

    public FreeLook(float sensitivity)
    {
        this.sensitivity = sensitivity;
        mouseLocked = true;

        Input.setMousePosition(new Vector2f(Window.getWidth() / 2.f, Window.getHeight() / 2.f));
        Input.setCursor(false);
    }

    @Override
    public void input(float delta)
    {
        Vector2f centerPosition = new Vector2f(Window.getWidth() / 2.f, Window.getHeight() / 2.f);

        if (Input.getKey(Input.KEY_ESCAPE)) {
            Input.setCursor(true);
            mouseLocked = false;
        }
        if(Input.getMouseDown(0))
        {
            Input.setMousePosition(centerPosition);
            Input.setCursor(false);
            mouseLocked = true;
        }

        if (mouseLocked) {
            Vector2f deltaPos = Input.getMousePosition().subtract(centerPosition);

            boolean rotY = deltaPos.getX() != 0;
            boolean rotX = deltaPos.getY() != 0;

            if (rotY)
                getTransform().rotate(yAxis, (float) Math.toRadians(deltaPos.getX() * sensitivity));
            if (rotX)
                getTransform().rotate(getTransform().getRotation().getRight(), (float) Math.toRadians(-deltaPos.getY() * sensitivity));

            if (rotY || rotX)
                Input.setMousePosition(centerPosition);
        }

    }


}