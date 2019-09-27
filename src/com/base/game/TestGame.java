package com.base.game;

import com.base.engine.components.*;
import com.base.engine.core.Game;
import com.base.engine.core.GameObject;
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

        Mesh mesh = new Mesh(vertices, indices, true);
        Material material = new Material(new Texture("test.png"), new Vector3f(1, 1, 1), 1, 8);

        MeshRenderer meshRenderer = new MeshRenderer(mesh, material);

        GameObject planeObject = new GameObject();
        planeObject.addComponent(meshRenderer);
        planeObject.getTransform().setPos(0.f, -1.f, 5.f);

        GameObject directionalLightObj = new GameObject();
        DirectionalLight dirLight = new DirectionalLight(new Vector3f(0, 0, 1), 0.4f, new Vector3f(1, 1, 1));
        directionalLightObj.addComponent(dirLight);

        GameObject pointLightObj = new GameObject();
        PointLight pointLight = new PointLight(new Vector3f(0, 1, 0), 0.4f, new Vector3f(0, 0, 1));
        pointLightObj.addComponent(pointLight);

        GameObject spotLightObj = new GameObject();
        SpotLight spotLight = new SpotLight(new Vector3f(1, 1, 1), 0.9f, new Vector3f(0.01f, 0, 0.02f), new Vector3f(1, 0, 0), 0.3f);
        spotLightObj.addComponent(spotLight);

        getRootObject().addChild(planeObject);
        getRootObject().addChild(directionalLightObj);
        getRootObject().addChild(pointLightObj);
        getRootObject().addChild(spotLightObj);
    }

}
