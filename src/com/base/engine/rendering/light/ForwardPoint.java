package com.base.engine.rendering.light;

import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Transform;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.shaders.Shader;

public class ForwardPoint extends Shader
{
    private static final ForwardPoint instance = new ForwardPoint();

    public static ForwardPoint getInstance()
    {
        return instance;
    }

    private ForwardPoint()
    {
        super();

        addVertexShaderFromFile("forward-point.vert");
        addFragmentShaderFromFile("forward-point.frag");
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

        setUniformPointLight("pointLight", getRenderingEngine().getPointLight());
    }

    private void setUniformBaseLight(String uniformName, BaseLight baseLight)
    {
        setUniformVec3(uniformName + ".color", baseLight.getColor());
        setUniformf(uniformName + ".intensity", baseLight.getIntensity());
    }

    public void setUniformPointLight(String uniformName, PointLight pointLight)
    {
        setUniformBaseLight(uniformName + ".base", pointLight.getBaseLight());
        setUniformf(uniformName + ".attenuation.constant", pointLight.getAttenuation().getConstant());
        setUniformf(uniformName + ".attenuation.linear", pointLight.getAttenuation().getLinear());
        setUniformf(uniformName + ".attenuation.exponent", pointLight.getAttenuation().getExponent());
        setUniformVec3(uniformName + ".position", pointLight.getPosition());
        setUniformf(uniformName + ".range", pointLight.getRange());
    }

}
