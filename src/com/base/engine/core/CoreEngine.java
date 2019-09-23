package com.base.engine.core;

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

        long lastTime = Time.getTime();
        double unprocessedTime = 0;

        while(isRunning) {
            boolean render = false;

            long startTime = Time.getTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double)Time.SECOND;

            // updating the context
            while (unprocessedTime > frameTime) {
                render = true;

                unprocessedTime -= frameTime;
                frameCounter += passedTime;

                if (Window.isCloseRequested())
                    stop();

                Time.setDelta(frameTime);

                game.input();
                renderingEngine.input();
                Input.update();

                game.update();

                if (frameCounter >= Time.SECOND) {
                    //System.out.println(frames);
                    frames = 0;
                    frameCounter = 0;
            }
            }

            // rendering if the context was updated
            if (render) {
                renderingEngine.render(game.getRootObject());
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


}
