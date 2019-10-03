package com.base.game;

import com.base.engine.components.*;
import com.base.engine.core.Game;
import com.base.engine.core.GameObject;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.*;

public class TestGame extends Game
{

    public void init()
    {
        float fieldDepth = 10.0f;
        float fieldWidth = 10.0f;

        Vertex[] vertices = new Vertex[] { 	new Vertex( new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)),
                new Vertex( new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)),
                new Vertex( new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)),
                new Vertex( new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f))};

        int indices[] = { 0, 1, 2,
                          2, 1, 3};

        Vertex[] vertices2 = new Vertex[] { 	new Vertex( new Vector3f(-fieldWidth/ 10, 0.0f, -fieldDepth/ 10), new Vector2f(0.0f, 0.0f)),
                new Vertex( new Vector3f(-fieldWidth/ 10, 0.0f, fieldDepth/ 10 * 3), new Vector2f(0.0f, 1.0f)),
                new Vertex( new Vector3f(fieldWidth/ 10 * 3, 0.0f, -fieldDepth/ 10), new Vector2f(1.0f, 0.0f)),
                new Vertex( new Vector3f(fieldWidth/ 10 * 3, 0.0f, fieldDepth/ 10 * 3), new Vector2f(1.0f, 1.0f))};

        int indices2[] = { 0, 1, 2,
                           2, 1, 3};

        Mesh mesh2 = new Mesh(vertices2, indices2, true);

        Mesh mesh = new Mesh(vertices, indices, true);
        Material material = new Material();
        material.addTexture("diffuse", new Texture("test.png"));
        material.addFloat("specularIntensity", 0.7f);
        material.addFloat("specularPower", 8);

        Material material2 = new Material();
        material2.addTexture("diffuse", new Texture("brick_wall.jpg"));
        material2.addFloat("specularIntensity", 1);
        material2.addFloat("specularPower", 16);

        Material material3 = new Material();
        material3.addTexture("diffuse", new Texture("snow.jpg"));
        material3.addFloat("specularIntensity", 1);
        material3.addFloat("specularPower", 64);

        Material material4 = new Material();
        material4.addTexture("diffuse", new Texture("mosaic.jpg"));
        material4.addFloat("specularIntensity", 0.8f);
        material4.addFloat("specularPower", 16);

        Mesh tempMesh = new Mesh("sphere.obj");

        MeshRenderer meshRenderer = new MeshRenderer(mesh, material4);

        GameObject planeObject = new GameObject();
        planeObject.addComponent(meshRenderer);
        planeObject.getTransform().getPosition().set(0, -1, 5);

        GameObject directionalLightObject = new GameObject();
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1f,1f,1f), 0.2f);

        directionalLightObject.addComponent(directionalLight);

        GameObject pointLightObject = new GameObject();
        pointLightObject.addComponent(new PointLight(new Vector3f(0,1,0), 0.4f, new Vector3f(0,0,1)));

        GameObject pointLightObject2 = new GameObject();
        pointLightObject2.addComponent(new PointLight(new Vector3f(0.8f,0.5f,0.5f), 0.6f, new Vector3f(0,0,0.6f)));
        pointLightObject2.getTransform().setPosition(new Vector3f(6, 6 , 6));

        SpotLight spotLight = new SpotLight(new Vector3f(0,1,1), 0.4f,
                new Vector3f(0,0,0.1f), 0.7f);

        GameObject spotLightObject = new GameObject();
        spotLightObject.addComponent(spotLight);

        spotLightObject.getTransform().getPosition().set(5, 0, 5);
        spotLightObject.getTransform().setRotation(new Quaternion(new Vector3f(0,1,0), (float)Math.toRadians(90.0f)));

        addObject(planeObject);
        addObject(directionalLightObject);
        addObject(pointLightObject);
        addObject(pointLightObject2);
        addObject(spotLightObject);


        GameObject testMesh1 = new GameObject().addComponent(new MeshRenderer(mesh2, material));
        GameObject testMesh2 = new GameObject().addComponent(new MeshRenderer(mesh2, material2));
        GameObject testMesh3 = new GameObject().addComponent(new MeshRenderer(tempMesh, material3));


        testMesh1.getTransform().getPosition().set(0, 2, 0);
        testMesh1.getTransform().setRotation(new Quaternion(new Vector3f(0,1,0), 0.4f));

        testMesh2.getTransform().getPosition().set(0, 0, 5);
        testMesh2.getTransform().setRotation(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(90)));


        testMesh1.addChild(testMesh2);

        addObject(testMesh1);
        addObject(testMesh3);

        GameObject camera = new GameObject().addComponent(new Camera((float)Math.toRadians(70.0f), (float)Window.getWidth()/(float)Window.getHeight(), 0.01f, 1000.0f));

        SpotLight cameraFlashLight = new SpotLight(new Vector3f(1,1,1), 0.9f,
                new Vector3f(0,0,0.2f), 0.8f);
        camera.addComponent(cameraFlashLight);

        addObject(camera);

        testMesh3.getTransform().getPosition().set(5 ,5, 5);

        addObject(new GameObject().addComponent(new MeshRenderer(new Mesh("sphere.obj"), material3)));


        directionalLight.getTransform().setRotation(new Quaternion(new Vector3f(1,0,0), (float)Math.toRadians(-45)));
    }
}
