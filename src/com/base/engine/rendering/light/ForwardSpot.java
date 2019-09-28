package com.base.engine.rendering.light;

import com.base.engine.components.BaseLight;
import com.base.engine.components.PointLight;
import com.base.engine.components.SpotLight;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Transform;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.shaders.*;

public class ForwardSpot extends Shader
{
    private static final ForwardSpot instance = new ForwardSpot();

    public static ForwardSpot getInstance()
    {
        return instance;
    }

    private ForwardSpot()
    {
        super();

        addVertexShaderFromFile("forward-spot.vert");
        addFragmentShaderFromFile("forward-spot.frag");
        compileShader();
    }

    public void updateUniforms(Transform transform, Material material)
    {
        Matrix4f worldMatrix = transform.getTransformation();
        Matrix4f projectedMatrix = getRenderingEngine().getMainCamera().getViewProjection().multiply(worldMatrix);

        material.getTexture().bind();

        setUniformMat4("model", worldMatrix);
        setUniformMat4("MVP", projectedMatrix);

        setUniformf("specularIntensity", material.getSpecularIntensity());
        setUniformf("specularPower", material.getSpecularPower());
        setUniformVec3("eyePos", getRenderingEngine().getMainCamera().getPos());

        setUniformSpotLight("spotLight", (SpotLight)getRenderingEngine().getActiveLight());
    }

    private void setUniformBaseLight(String uniformName, BaseLight baseLight)
    {
        setUniformVec3(uniformName + ".color", baseLight.getColor());
        setUniformf(uniformName + ".intensity", baseLight.getIntensity());
    }

    public void setUniformPointLight(String uniformName, PointLight pointLight)
    {
        setUniformBaseLight(uniformName + ".base", pointLight);
        setUniformf(uniformName + ".attenuation.constant", pointLight.getConstant());
        setUniformf(uniformName + ".attenuation.linear", pointLight.getLinear());
        setUniformf(uniformName + ".attenuation.exponent", pointLight.getExponent());
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
