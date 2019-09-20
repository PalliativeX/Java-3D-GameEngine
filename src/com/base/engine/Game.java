package com.base.engine;

import com.base.engine.math.Vector3f;
import org.lwjgl.input.Keyboard;

public class Game
{
    private Mesh mesh;
    private Shader shader;
    private Transform transform;
    private Camera camera;

    public Game()
    {
        mesh = ResourceLoader.loadMesh("box.obj");//new Mesh();
        shader = new Shader();
        camera = new Camera();

        /*Vertex[] vertices = new Vertex[] { new Vertex(new Vector3f(-1, - 1, 0)),
                                           new Vertex(new Vector3f( 0,   1, 0)),
                                           new Vertex(new Vector3f( 1,  -1, 0)),
                                           new Vertex(new Vector3f(0, -1, 1)) };

        int[] indices = new int[] { 0, 1, 3,
                                    3, 1, 2,
                                    2, 1, 0,
                                    0, 2, 3 };

        mesh.addVertices(vertices, indices);*/

        transform = new Transform();
        Transform.setProjection(70.f, MainComponent.WIDTH, MainComponent.HEIGHT, 0.1f, 1000.f);
        Transform.setCamera(camera);

        shader.addVertexShader(ResourceLoader.loadShader("basicVertex.vert"));
        shader.addFragmentShader(ResourceLoader.loadShader("basicFragment.frag"));
        shader.compileShader();
    }

    public void input()
    {
        camera.input();
    }

    float temp = 0.f;

    public void update()
    {
        temp += Time.getDelta();

        float sinTemp = (float)Math.sin(temp);

        transform.setTranslation(0.f, 0.f, 5.f);
        transform.setRotation(0.f, sinTemp * 180, 0.f);
        transform.setScale(0.7f * sinTemp, 0.7f * sinTemp, 0.7f * sinTemp);
    }

    public void render()
    {
        shader.bind();
        shader.setUniformMat4("transform", transform.getProjectedTransformation());
        mesh.draw();
    }


}
