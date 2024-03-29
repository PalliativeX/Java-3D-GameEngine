package com.base.engine.core;

import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Window;

public class CoreEngine
{
    private boolean isRunning;
    private Game game;
    private RenderingEngine renderingEngine;

    private int width;
    private int height;
    private double frameTime;


    public CoreEngine(int width, int height, double framerate, Game game)
    {
        isRunning = false;
        this.game = game;
        this.width = width;
        this.height = height;
        this.frameTime = 1.0 / framerate;
        game.setCoreEngine(this);
    }

    public void createWindow(String title)
    {
        Window.createWindow(width, height, title);
        renderingEngine = new RenderingEngine();
    }

    public void start()
    {
        // if it already runs we don't have to start it
        if (isRunning)
            return;

        run();
    }

    public void stop()
    {
        if (!isRunning)
            return;

        isRunning = false;
    }

    private void run()
    {
        isRunning = true;

        int frames = 0;
        long frameCounter = 0;

        game.init();

        double lastTime = Time.getTime();
        double unprocessedTime = 0;

        while(isRunning) {
            this.width = Window.getWidth();
            this.height = Window.getHeight();

            boolean render = false;

            double startTime = Time.getTime();
            double passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime;

            // updating the context
            while (unprocessedTime > frameTime) {
                render = true;

                unprocessedTime -= frameTime;
                frameCounter += passedTime;

                if (Window.isCloseRequested())
                    stop();


                game.input((float)frameTime);
                Input.update();

                game.update((float)frameTime);

                if (frameCounter >= 1.0) {
                    frames = 0;
                    frameCounter = 0;
            }
            }

            // rendering if the context was updated
            if (render) {
                game.render(renderingEngine);
                Window.render();
                frames++;
            }
            else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        cleanUp();
    }

    private void cleanUp()
    {
        Window.dispose();
    }

    public RenderingEngine getRenderingEngine()
    {
        return renderingEngine;
    }
}
