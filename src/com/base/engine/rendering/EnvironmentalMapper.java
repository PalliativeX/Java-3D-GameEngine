package com.base.engine.rendering;

import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Transform;
import com.base.engine.core.math.Vector3f;

public class EnvironmentalMapper extends Shader
{
    private static Skybox skybox;

    private static EnvironmentalMapper instance;

    public static EnvironmentalMapper getInstance()
    {
        if (instance == null)
            instance = new EnvironmentalMapper();
        return instance;
    }

    private EnvironmentalMapper()
    {
        super();

        addVertexShaderFromFile("skyboxReflection.vert");
        addFragmentShaderFromFile("skyboxReflection.frag");
        compileShader();

        addUniform("model");
        addUniform("MVP");
        addUniform("cameraPos");
    }

    public static void setSkybox(Skybox other)
    {
        skybox = other;
    }

    @Override
    public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine)
    {
        Matrix4f worldMatrix = transform.getTransformation();
        Matrix4f projectedMatrix = renderingEngine.getMainCamera().getViewProjection().multiply(worldMatrix);

        skybox.bind();

        Vector3f cameraPos = renderingEngine.getMainCamera().getTransform().getPosition();

        setUniformMat4("MVP", projectedMatrix);
        setUniformMat4("model", worldMatrix);
        setUniformVec3("cameraPos", cameraPos);
    }
}
