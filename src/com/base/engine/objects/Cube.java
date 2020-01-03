package com.base.engine.objects;

import com.base.engine.components.MeshRenderer;
import com.base.engine.core.GameObject;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Texture;

public class Cube extends GameObject
{
    private MeshRenderer meshRenderer;

//    public Cube()
//    {
//        super();
//
//        Material defaultMat = new Material();
//        defaultMat.addTexture("diffuse", new Texture("snow.jpg"));
//        defaultMat.addTexture("normalMap", new Texture("snowNormal.png"));
//        defaultMat.addFloat("specularIntensity", 0.7f);
//        defaultMat.addFloat("specularPower", 8);
//    }

    public Cube(Material material)
    {
        super();

        Mesh cubeMesh = new Mesh("box.obj");
        meshRenderer = new MeshRenderer(cubeMesh, material);
        this.addComponent(meshRenderer);
    }
}
