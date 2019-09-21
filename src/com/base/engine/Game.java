package com.base.engine;

import com.base.engine.math.Vector2f;
import com.base.engine.math.Vector3f;

public class Game
{
    private Mesh mesh;
    private BasicShader shader;
    private Transform transform;
    private Camera camera;
    private Material material;

    public Game()
    {
        mesh = new Mesh();
        camera = new Camera();
        material = new Material(ResourceLoader.loadTexture("background.jpg"), new Vector3f(1, 0, 1));
        shader = new BasicShader();

        Vertex[] vertices = new Vertex[] { new Vertex(new Vector3f(-1, - 1, 0), new Vector2f(0, 0)),
                                           new Vertex(new Vector3f( 0,   1, 0), new Vector2f(0.5f, 0)),
                                           new Vertex(new Vector3f( 1,  -1, 0), new Vector2f(1.f, 0)),
                                           new Vertex(new Vector3f(0, -1, 1),   new Vector2f(0.f, 0.5f)) };

        int[] indices = new int[] { 0, 1, 3,
                                    3, 1, 2,
                                    2, 1, 0,
                                    0, 2, 3 };

        mesh.addVertices(vertices, indices);

        transform = new Transform();
        Transform.setProjection(70.f, MainComponent.WIDTH, MainComponent.HEIGHT, 0.1f, 1000.f);
        Transform.setCamera(camera);

    }

    public void input()
    {
        camera.input();
    }

    private float temp = 0.f;

    public void update()
    {
        temp += Time.getDelta();

        float sinTemp = (float)Math.sin(temp);

        transform.setTranslation(0.f, 0.f, 5.f);
        transform.setRotation(0.f, sinTemp * 180, 0.f);
        transform.setScale(1.5f * sinTemp, 1.5f * sinTemp, 1.5f * sinTemp);
    }

    public void render()
    {
        RenderUtil.setClearColor(Transform.getCamera().getPos().divide(2048f).abs());
        shader.bind();
        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();
    }


}
