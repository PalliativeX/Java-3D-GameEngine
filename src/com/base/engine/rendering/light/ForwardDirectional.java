package com.base.engine.rendering.light;

import com.base.engine.components.BaseLight;
import com.base.engine.components.DirectionalLight;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Transform;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

public class ForwardDirectional extends Shader
{
    private static ForwardDirectional instance;

    public static ForwardDirectional getInstance()
    {
        if (instance == null)
            instance = new ForwardDirectional();
        return instance;
    }

    private ForwardDirectional()
    {
        super();

        addVertexShaderFromFile("forward-directional.vert");
        addFragmentShaderFromFile("forward-directional.frag");

        compileShader();

        addUniform("model");
        addUniform("MVP");

        addUniform("specularIntensity");
        addUniform("specularPower");
        addUniform("eyePos");

        addUniform("directionalLight.base.color");
        addUniform("directionalLight.base.intensity");
        addUniform("directionalLight.direction");
    }

    public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine)
    {
        Matrix4f worldMatrix = transform.getTransformation();
        Matrix4f projectedMatrix = renderingEngine.getMainCamera().getViewProjection().multiply(worldMatrix);

        material.getTexture("diffuse").bind(0);
        material.getTexture("normalMap").bind(1);

        setUniformMat4("model", worldMatrix);
        setUniformMat4("MVP", projectedMatrix);

        setUniformf("specularIntensity", material.getFloat("specularIntensity"));
        setUniformf("specularPower", material.getFloat("specularPower"));
        setUniformVec3("eyePos", renderingEngine.getMainCamera().getTransform().getTransformedPosition());

        setUniformDirLight("directionalLight", (DirectionalLight) renderingEngine.getActiveLight());
    }

    private void setUniformBaseLight(String uniformName, BaseLight baseLight)
    {
        setUniformVec3(uniformName + ".color", baseLight.getColor());
        setUniformf(uniformName + ".intensity", baseLight.getIntensity());
    }

    private void setUniformDirLight(String uniformName, DirectionalLight directionalLight)
    {
        setUniformBaseLight(uniformName + ".base", directionalLight);
        setUniformVec3(uniformName + ".direction", directionalLight.getDirection());
    }
}
