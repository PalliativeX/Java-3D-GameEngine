package com.base.engine;

import com.base.engine.light.*;
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

    private PointLight pLight1 = new PointLight(new BaseLight(new Vector3f(1,0.5f,0), 0.8f), new Attenuation(0,0,1), new Vector3f(-2,0,5f), 10);
    private PointLight pLight2 = new PointLight(new BaseLight(new Vector3f(0,0.5f,1), 0.8f), new Attenuation(0,0,1), new Vector3f(2,0,7f), 10);

    //SpotLight sLight1 = new SpotLight(new PointLight(new BaseLight(new Vector3f(1,1f,1f), 2.f), new Attenuation(0,0,0.1f), new Vector3f(1,1,1f), 30),
       //     new Vector3f(1,1,1), 0.4f);

    public Game()
    {
        camera = new Camera();
        material = new Material(new Texture("background.jpg"), new Vector3f(1, 1, 1), 1, 8);
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

        mesh = new Mesh(vertices, indices, true);


        Transform.setProjection(50.f, MainComponent.WIDTH, MainComponent.HEIGHT, 0.1f, 500.f);
        Transform.setCamera(camera);

        PhongShader.setAmbientLight(new Vector3f(0.10f, 0.10f, 0.10f));
        PhongShader.setDirectionalLight(new DirectionalLight(new BaseLight(new Vector3f(1, 1, 1), 0.2f), new Vector3f(1, 1, 1)));


        PhongShader.setPointLights(new PointLight[] { pLight1, pLight2} );

        //PhongShader.setSpotLights(new SpotLight[] { sLight1 });
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

        pLight1.setPosition(new Vector3f(3,0,8.0f * (float)(Math.sin(temp) + 1.0/2.0) + 10));
        pLight2.setPosition(new Vector3f(7,0,8.0f * (float)(Math.cos(temp) + 1.0/2.0) + 10));

        //sLight1.getPointLight().setPosition(camera.getPos());
        //sLight1.setDirection(camera.getForward().normalized());
    }

    public void render()
    {
        RenderUtil.setClearColor(new Vector3f(0.1f, 0.1f, 0.1f));
        shader.bind();
        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();
    }


}
