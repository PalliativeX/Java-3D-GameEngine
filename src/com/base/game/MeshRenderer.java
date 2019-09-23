package com.base.game;

import com.base.engine.core.GameComponent;
import com.base.engine.core.math.Transform;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.shaders.BasicShader;
import com.base.engine.rendering.shaders.Shader;

public class MeshRenderer extends GameComponent
{
    private Mesh mesh;
    private Material material;

    public MeshRenderer(Mesh mesh, Material material)
    {
        this.mesh = mesh;
        this.material = material;
    }

    @Override
    public void render(Transform transform)
    {
        Shader shader = BasicShader.getInstance();

        shader.bind();
        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();
    }
}
