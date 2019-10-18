package com.base.game;

import com.base.engine.components.*;
import com.base.engine.core.Game;
import com.base.engine.core.GameObject;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.*;
import com.base.engine.rendering.light.Attenuation;

public class TestGame extends Game
{

    public void init()
    {

        Mesh mesh = new Mesh("plane.obj");
        Material testMaterial = new Material();
        testMaterial.addTexture("diffuse", new Texture("test.png"));
        testMaterial.addTexture("normalMap", new Texture("testNormal.png"));
        testMaterial.addFloat("specularIntensity", 1);
        testMaterial.addFloat("specularPower", 8);

        Material snowMaterial = new Material();
        snowMaterial.addTexture("diffuse", new Texture("snow.jpg"));
        snowMaterial.addTexture("normalMap", new Texture("snowNormal.png"));
        snowMaterial.addFloat("specularIntensity", 1);
        snowMaterial.addFloat("specularPower", 8);

        Material brickMaterial = new Material();
        brickMaterial.addTexture("diffuse", new Texture("brick_wall.jpg"));
        brickMaterial.addTexture("normalMap", new Texture("brick_wallNormal.png"));
        brickMaterial.addFloat("specularIntensity", 0.8f);
        brickMaterial.addFloat("specularPower", 16);

        Material mosaicMaterial = new Material();
        mosaicMaterial.addTexture("diffuse", new Texture("mosaic.jpg"));
        mosaicMaterial.addTexture("normalMap", new Texture("mosaicNormal.png"));
        mosaicMaterial.addFloat("specularIntensity", 1);
        mosaicMaterial.addFloat("specularPower", 8);

        Material grassMaterial = new Material();
        grassMaterial.addTexture("diffuse", new Texture("grass.png"));
        grassMaterial.addTexture("normalMap", new Texture("grassNormal.png"));
        grassMaterial.addFloat("specularIntensity", 0.3f);
        grassMaterial.addFloat("specularPower", 4);

        Material semitransparentMaterial = new Material();
        semitransparentMaterial.addTexture("diffuse", new Texture("transparent_window.png"));
        semitransparentMaterial.addTexture("normalMap", new Texture("transparent_windowNormal.png"));
        semitransparentMaterial.addFloat("specularIntensity", 0.3f);
        semitransparentMaterial.addFloat("specularPower", 4);

        Mesh tempMesh = new Mesh("monkey.obj");

        MeshRenderer meshRenderer = new MeshRenderer(mesh, snowMaterial);
        MeshRenderer meshRenderer2 = new MeshRenderer(mesh, brickMaterial);

        GameObject planeObject = new GameObject();
        planeObject.addComponent(meshRenderer);
        planeObject.getTransform().getPosition().set(0, -1, 5);

        GameObject planeObject2 = new GameObject();
        planeObject2.addComponent(meshRenderer2);
        planeObject2.getTransform().getPosition().set(2, 1, 2);
        planeObject2.getTransform().setRotation(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(90)));
        planeObject2.getTransform().setScale(new Vector3f(0.3f, 0.3f, 0.3f));

        GameObject directionalLightObject = new GameObject();
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(0,0,1), 0.4f);

        directionalLightObject.addComponent(directionalLight);

        GameObject pointLightObject = new GameObject();
        pointLightObject.addComponent(new PointLight(new Vector3f(0,1,0), 0.4f, new Attenuation(0,0,1)));

        SpotLight spotLight = new SpotLight(new Vector3f(0,1,1), 0.4f,
                new Attenuation(0,0,0.1f), 0.7f);

        GameObject spotLightObject = new GameObject();
        spotLightObject.addComponent(spotLight);

        spotLightObject.getTransform().getPosition().set(5, 0, 5);
        spotLightObject.getTransform().setRotation(new Quaternion(new Vector3f(0,1,0), (float)Math.toRadians(90.0f)));

        GameObject spotLightObject2 = new GameObject();
        SpotLight spotLight2 = new SpotLight(new Vector3f(1,1,1), 0.8f,
                new Attenuation(0,0,0.1f), 0.5f);
        spotLightObject2.addComponent(spotLight2);

        GameObject pointLightObject2 = new GameObject();
        pointLightObject2.addComponent(new PointLight(new Vector3f(1.f,0.5f,0.5f), 0.9f, new Attenuation(0,0,0.6f)));
        pointLightObject2.getTransform().setPosition(new Vector3f(0, 3, 1));

        addObject(planeObject);
        addObject(planeObject2);

        addObject(directionalLightObject);
        addObject(pointLightObject);
        addObject(pointLightObject2);
        addObject(spotLightObject);

        GameObject testMesh3 = new GameObject().addComponent(new LookAtComponent()).addComponent(new MeshRenderer(tempMesh, mosaicMaterial));
        addObject(testMesh3);
        testMesh3.getTransform().getPosition().set(5,5,5);
        testMesh3.getTransform().setRotation(new Quaternion(new Vector3f(0,1,0), (float)Math.toRadians(-70.0f)));

        // camera with a freelook and freemove
        GameObject cameraObject = new GameObject().addComponent(new FreeLook(0.5f)).addComponent(new FreeMove(10.0f)).addComponent(new Camera((float) Math.toRadians(70.0f), (float) Window.getWidth() / (float) Window.getHeight(), 0.01f, 1000.0f));
        addObject(cameraObject);

        GameObject grassObj = new GameObject().addComponent(new MeshRenderer(mesh, grassMaterial));
        grassObj.getTransform().setPosition(new Vector3f(3, -0.2f, 6));
        grassObj.getTransform().setScale(new Vector3f(0.1f, 0.1f, 0.1f));
        grassObj.getTransform().setRotation(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(90f)));
        addObject(grassObj);

        addObject(new GameObject().addComponent(new MeshRenderer(new Mesh("monkey.obj"), snowMaterial)));

        directionalLight.getTransform().setRotation(new Quaternion(new Vector3f(1, 0, 0), (float) Math.toRadians(-45)));

        GameObject windowObj = new GameObject().addComponent(new MeshRenderer(mesh, semitransparentMaterial));
        windowObj.getTransform().setPosition(new Vector3f(3, 0, 9));
        windowObj.getTransform().setScale(new Vector3f(0.1f, 0.1f, 0.1f));
        windowObj.getTransform().setRotation(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-90f)));
        addObject(windowObj);

        //Window.setFullScreen();

        getCoreEngine().getRenderingEngine().setAmbientLight(new Vector3f(0.2f, 0.2f, 0.2f));

        String[] faces = {
                "skybox/right.jpg",
                "skybox/left.jpg",
                "skybox/top.jpg",
                "skybox/bottom.jpg",
                "skybox/front.jpg",
                "skybox/back.jpg",
        };
        getCoreEngine().getRenderingEngine().setSkybox(faces);
        getCoreEngine().getRenderingEngine().setBlurEnabled(true);
    }

}
