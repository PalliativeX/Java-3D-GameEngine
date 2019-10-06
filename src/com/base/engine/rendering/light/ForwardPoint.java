package com.base.engine.rendering.light;

import com.base.engine.components.BaseLight;
import com.base.engine.components.PointLight;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Transform;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

public class ForwardPoint extends Shader
{
    private static ForwardPoint instance;

    public static ForwardPoint getInstance()
    {
        if (instance == null)
            instance = new ForwardPoint();
        return instance;
    }

    private ForwardPoint()
    {
        super();

        addVertexShaderFromFile("forward-point.vert");
        addFragmentShaderFromFile("forward-point.frag");
        compileShader();

        addUniform("model");
        addUniform("MVP");

        addUniform("specularIntensity");
        addUniform("specularPower");
        addUniform("eyePos");

        addUniform("pointLight.base.color");
        addUniform("pointLight.base.intensity");
        addUniform("pointLight.attenuation.constant");
        addUniform("pointLight.attenuation.linear");
        addUniform("pointLight.attenuation.exponent");
        addUniform("pointLight.position");
        addUniform("pointLight.range");
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

        setUniformPointLight("pointLight", (PointLight)renderingEngine.getActiveLight());
    }

    private void setUniformBaseLight(String uniformName, BaseLight baseLight)
    {
        setUniformVec3(uniformName + ".color", baseLight.getColor());
        setUniformf(uniformName + ".intensity", baseLight.getIntensity());
    }

    public void setUniformPointLight(String uniformName, PointLight pointLight)
    {
        setUniformBaseLight(uniformName + ".base", pointLight);
        setUniformf(uniformName + ".attenuation.constant", pointLight.getAttenuation().getConstant());
        setUniformf(uniformName + ".attenuation.linear", pointLight.getAttenuation().getLinear());
        setUniformf(uniformName + ".attenuation.exponent", pointLight.getAttenuation().getExponent());
        setUniformVec3(uniformName + ".position", pointLight.getTransform().getPosition());
        setUniformf(uniformName + ".range", pointLight.getRange());
    }

}
