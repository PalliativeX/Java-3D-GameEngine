package com.base.engine.rendering.light;

import com.base.engine.components.BaseLight;
import com.base.engine.components.DirectionalLight;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Transform;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.shaders.Shader;

public class ForwardDirectional extends Shader
{
    private static final ForwardDirectional instance = new ForwardDirectional();

    public static ForwardDirectional getInstance()
    {
        return instance;
    }

    private ForwardDirectional()
    {
        super();

        addVertexShaderFromFile("forward-directional.vert");
        addFragmentShaderFromFile("forward-directional.frag");
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
        setUniformVec3("eyePos", getRenderingEngine().getMainCamera().getTransform().getTransformedPosition());

        setUniformDirLight("directionalLight", (DirectionalLight)getRenderingEngine().getActiveLight());
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
