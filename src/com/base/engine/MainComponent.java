package com.base.engine;

public class MainComponent
{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "3D Engine";
    public static final double FRAME_CAP = 5000.D;

    private boolean isRunning;
    private Game game;

    public MainComponent()
    {
        RenderUtil.initGraphics();
        isRunning = false;
        game = new Game();
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

        final double frameTime = 1.0 / FRAME_CAP;

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
                Input.update();

                game.update();

                if (frameCounter >= Time.SECOND) {
                    System.out.println(frames);
                    frames = 0;
                    frameCounter = 0;
            }
            }

            // rendering if the context was updated
            if (render) {
                render();
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

    private void render()
    {
        RenderUtil.clearScreen();
        game.render();
        Window.render();
    }

    private void cleanUp()
    {
        Window.dispose();
    }

    public static void main(String[] args)
    {
        Window.createWindow(WIDTH, HEIGHT, TITLE);

        MainComponent game = new MainComponent();

        game.start();
    }


}
