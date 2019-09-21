package com.base.engine;

import com.base.engine.math.Vector2f;
import com.base.engine.math.Vector3f;
import com.base.engine.shaders.BasicShader;
import com.base.engine.shaders.PhongShader;

public class Game
{
    private Mesh mesh;
    private PhongShader shader;
    private Transform transform;
    private Camera camera;
    private Material material;

    public Game()
    {
        mesh = new Mesh();
        camera = new Camera();
        material = new Material(ResourceLoader.loadTexture("background.jpg"), new Vector3f(1, 0, 1));
        shader = PhongShader.getInstance();
        transform = new Transform();

        Vertex[] vertices = new Vertex[] { new Vertex(new Vector3f(-1, - 1, 0), new Vector2f(0, 0)),
                                           new Vertex(new Vector3f( 0,   1, 0), new Vector2f(0.5f, 0)),
                                           new Vertex(new Vector3f( 1,  -1, 0), new Vector2f(1.f, 0)),
                                           new Vertex(new Vector3f(0, -1, 1),   new Vector2f(0.5f, 1.f)) };

        int[] indices = new int[] { 3, 1, 0,
                                    2, 1, 3,
                                    0, 1, 2,
                                    0, 2, 3 };

        mesh.addVertices(vertices, indices, true);


        Transform.setProjection(70.f, MainComponent.WIDTH, MainComponent.HEIGHT, 0.1f, 1000.f);
        Transform.setCamera(camera);

        PhongShader.setAmbientLight(new Vector3f(0.1f, 0.1f, 0.1f));
        PhongShader.setDirectionalLight(new DirectionalLight(new BaseLight(new Vector3f(1, 1, 1), 0.8f), new Vector3f(1, 1, 1)));

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
        transform.setScale(2.f, 2.f, 2.f);
    }

    public void render()
    {
        RenderUtil.setClearColor(new Vector3f(0.5f, 0.3f, 0.7f));
        shader.bind();
        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();
    }


}
