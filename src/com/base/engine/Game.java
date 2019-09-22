package com.base.engine;

import com.base.engine.light.Attenuation;
import com.base.engine.light.BaseLight;
import com.base.engine.light.DirectionalLight;
import com.base.engine.light.PointLight;
import com.base.engine.math.Vector2f;
import com.base.engine.math.Vector3f;
import com.base.engine.shaders.PhongShader;

public class Game
{
    private Mesh mesh;
    private PhongShader shader;
    private Transform transform;
    private Camera camera;
    private Material material;

    PointLight pLight1 = new PointLight(new BaseLight(new Vector3f(1, 0.5f, 0), 0.8f), new Attenuation(1, 0, 0), new Vector3f(-2, 3, 5f), 6);
    PointLight pLight2 = new PointLight(new BaseLight(new Vector3f(0, 0f, 1), 0.8f), new Attenuation(1, 0, 0), new Vector3f(2,0,7f), 6);

    public Game()
    {
        mesh = new Mesh();
        camera = new Camera();
        material = new Material(ResourceLoader.loadTexture("test.png"), new Vector3f(1, 1, 1), 1, 8);
        shader = PhongShader.getInstance();
        transform = new Transform();

        float fieldDepth = 10.0f;
        float fieldWidth = 10.0f;

        Vertex[] vertices = new Vertex[] { 	new Vertex( new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)),
                new Vertex( new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)),
                new Vertex( new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)),
                new Vertex( new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f))};

        int indices[] = { 0, 1, 2,
                2, 1, 3};

        mesh.addVertices(vertices, indices, true);


        Transform.setProjection(70.f, MainComponent.WIDTH, MainComponent.HEIGHT, 0.1f, 1000.f);
        Transform.setCamera(camera);

        PhongShader.setAmbientLight(new Vector3f(0.10f, 0.10f, 0.10f));
        PhongShader.setDirectionalLight(new DirectionalLight(new BaseLight(new Vector3f(1, 1, 1), 0.6f), new Vector3f(1, 1, 1)));


        PhongShader.setPointLights(new PointLight[] { pLight1, pLight2} );
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

        transform.setTranslation(0.f, -1.f, 5.f);
        //transform.setRotation(0.f, sinTemp * 180, 0.f);
        //transform.setScale(2.f, 2.f, 2.f);
    }

    public void render()
    {
        RenderUtil.setClearColor(new Vector3f(0.1f, 0.1f, 0.1f));
        shader.bind();
        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();
    }


}
