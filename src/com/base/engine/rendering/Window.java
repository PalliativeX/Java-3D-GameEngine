package com.base.engine.rendering;

import com.base.engine.core.math.Vector2f;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.glViewport;

public class Window
{
    public static void createWindow(int width, int height, String title) {
        Display.setTitle(title);
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.create();
            Keyboard.create();
            Mouse.create();
        }
        catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public static void render()
    {
        Display.update();
    }

    public static void dispose()
    {
        Display.destroy();
        Keyboard.destroy();
        Mouse.destroy();
    }

    public static void setFullScreen()
    {
        try {
            Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
            Display.setVSyncEnabled(true);
            glViewport(0, 0, Display.getWidth(), Display.getHeight());
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.err.println("Cannot set fullscreen mode!");
            System.exit(1);
        }
    }

    public static boolean isCloseRequested()
    {
        return Display.isCloseRequested();
    }

    public static int getWidth()
    {
        return Display.getDisplayMode().getWidth();
    }

    public static int getHeight()
    {
        return Display.getDisplayMode().getHeight();
    }

    public static String getTitle()
    {
        return Display.getTitle();
    }

    public Vector2f getCenter()
    {
        return new Vector2f(getWidth()/2.f, getHeight()/2.f);
    }
}
