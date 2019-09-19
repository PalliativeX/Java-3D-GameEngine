package com.base.engine;

import com.base.engine.math.Vector2f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

public class Input
{
    public static final int NUM_KEYCODES = 256;
    public static final int NUM_MOUSEBUTTONS = 5;

    private static ArrayList<Integer> currentKeys = new ArrayList<>();
    private static ArrayList<Integer> downKeys = new ArrayList<>();
    private static ArrayList<Integer> upKeys = new ArrayList<>();

    private static ArrayList<Integer> currentMouse = new ArrayList<>();
    private static ArrayList<Integer> downMouse = new ArrayList<>();
    private static ArrayList<Integer> upMouse = new ArrayList<>();

    public static void update()
    {
        upMouse.clear();
        for (int i = 0; i < NUM_MOUSEBUTTONS; i++) {
            if (!getMouse(i) && currentMouse.contains(i))
                upMouse.add(i);
        }

        downMouse.clear();
        for (int i = 0; i < NUM_MOUSEBUTTONS; i++) {
            if (getMouse(i) && !currentMouse.contains(i))
                downMouse.add(i);
        }

        upKeys.clear();
        for (int i = 0; i < NUM_KEYCODES; i++) {
            // if a key is not pressed but it was in the prev. frame
            if (!getKey(i) && currentKeys.contains(i))
                upKeys.add(i);
        }

        downKeys.clear();
        for (int i = 0; i < NUM_KEYCODES; i++) {
            // if key has been pressed and it hasn't been pressed in the prev. frame
            if (getKey(i) && !currentKeys.contains(i))
                downKeys.add(i);
        }

        currentKeys.clear();
        for (int i = 0; i < NUM_KEYCODES; i++) {
            if (getKey(i))
                currentKeys.add(i);
        }

        currentMouse.clear();
        for (int i = 0; i < NUM_MOUSEBUTTONS; i++) {
            if (getMouse(i))
                currentMouse.add(i);
        }
    }

    public static boolean getKey(int keycode)
    {
        return Keyboard.isKeyDown(keycode);
    }

    public static boolean getKeyDown(int keycode)
    {
        return downKeys.contains(keycode);
    }

    public static boolean getKeyUp(int keycode)
    {
        return upKeys.contains(keycode);
    }

    public static boolean getMouse(int mouseButton)
    {
        return Mouse.isButtonDown(mouseButton);
    }

    public static boolean getMouseDown(int mouseButton)
    {
        return downMouse.contains(mouseButton);
    }

    public static boolean getMouseUp(int mouseButton)
    {
        return upMouse.contains(mouseButton);
    }

    public static Vector2f getMousePosition()
    {
        return new Vector2f(Mouse.getX(), Mouse.getY());
    }


}