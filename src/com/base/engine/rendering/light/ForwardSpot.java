package com.base.engine.rendering.light;

import com.base.engine.components.*;
import com.base.engine.core.math.*;
import com.base.engine.rendering.*;

public class ForwardSpot extends Shader
{
    private static ForwardSpot instance;

    public static ForwardSpot getInstance()
    {
        if (instance == null)
            instance = new ForwardSpot();
        return instance;
    }

    private ForwardSpot()
    {
        super();

        addVertexShaderFromFile("forward-spot.vert");
        addFragmentShaderFromFile("forward-spot.frag");
        compileShader();

        addUniform("model");
        addUniform("MVP");

        addUniform("specularIntensity");
        addUniform("specularPower");
        addUniform("eyePos");

        addUniform("spotLight.pointLight.base.color");
        addUniform("spotLight.pointLight.base.intensity");
        addUniform("spotLight.pointLight.attenuation.constant");
        addUniform("spotLight.pointLight.attenuation.linear");
        addUniform("spotLight.pointLight.attenuation.exponent");
        addUniform("spotLight.pointLight.position");
        addUniform("spotLight.pointLight.range");
        addUniform("spotLight.direction");
        addUniform("spotLight.cutOff");
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

        setUniformSpotLight("spotLight", (SpotLight)renderingEngine.getActiveLight());
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

    public void setUniformSpotLight(String uniformName, SpotLight spotLight)
    {
        setUniformPointLight(uniformName + ".pointLight", spotLight);
        setUniformVec3(uniformName + ".direction", spotLight.getDirection());
        setUniformf(uniformName + ".cutOff", spotLight.getCutOff());
    }

}
