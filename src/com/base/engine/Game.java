package com.base.engine;

import com.base.engine.math.Vector3f;
import org.lwjgl.input.Keyboard;

public class Game
{
    private Mesh mesh;
    private Shader shader;

    public Game()
    {
        mesh = new Mesh();
        shader = new Shader();

        Vertex[] data = new Vertex[] { new Vertex(new Vector3f(-1, - 1, 0)),
                                       new Vertex(new Vector3f( 0,   1, 0)),
                                       new Vertex(new Vector3f( 1,  -1, 0))
        };

        mesh.addVertices(data);

        shader.addVertexShader(ResourceLoader.loadShader("basicVertex.vert"));
        shader.addFragmentShader(ResourceLoader.loadShader("basicFragment.frag"));
        shader.compileShader();
    }

    public void input()
    {
        // just for testing Input
        if (Input.getKeyDown(Keyboard.KEY_UP))
            System.out.println("We've just pressed up!");
        if (Input.getKeyUp(Keyboard.KEY_UP))
            System.out.println("We've just released up!");

        if (Input.getMouseDown(1))
            System.out.println("We've just right clicked at: " + Input.getMousePosition().toString());
        if (Input.getMouseUp(1))
            System.out.println("We've just released up the mouse!");
    }

    float temp = 0.f;

    public void update()
    {
        temp += Time.getDelta();

        shader.setUniformf("uniformFloat", (float)Math.sin(temp));
    }

    public void render()
    {
        shader.bind();
        mesh.draw();
    }


}
